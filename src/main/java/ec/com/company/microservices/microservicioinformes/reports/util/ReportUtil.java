package ec.com.company.microservices.microservicioinformes.reports.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ec.com.company.core.interfaces.Valoracioncompanyl;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.ReportBlueprint;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ActaReportType;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.TipoNormativaEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.factory.HojaEnBlancoSecion;
import ec.com.company.microservices.microservicioinformes.reports.factory.JasperSection;
import ec.com.company.microservices.microservicioinformes.reports.factory.PdfSection;
import ec.com.company.microservices.microservicioinformes.reports.factory.ReportSection;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReportUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

    public static ReportBlueprint getReportBlueprint(Path reportFilesRelativePath) throws coreException {
        // blueprint.yml path
        Path blueprintRelativePath = Paths.get(reportFilesRelativePath
                + File.separator + ReportResourcesConstants.REPORTS_BLUEPRINT_FILENAME);
        // blueprint.yml file
        InputStream reportBlueprintFile = ResourcesUtil.getInstance()
                .getResourceAsFile(blueprintRelativePath.toString());
        // Parse to DTO Object
        return parseBlueprint(reportBlueprintFile);
    }

    public static Map<String, Map<String, Object>> getActaBlueprint(ActaReportType actaReportType, ReportBlueprint reportBlueprint) {
        return switch (actaReportType) {
            case REPORTE_PRINCIPAL -> reportBlueprint.getActaFiniquito();
            case ANEXO_RESUMEN_EXPLICATIVO -> reportBlueprint.getActaResumenExplicativo();
        };
    }

    public static Map<String, Map<String, Object>> getEstudioBlueprint(TipoNormativaEstudioEnum tipoNormativaEstudio, ReportBlueprint reportBlueprint) {

        return switch (tipoNormativaEstudio) {
            case BONOS_EEUU -> reportBlueprint.getBonosEeuu();
            case BONOS_LOCALES -> reportBlueprint.getBonosLocales();
            case NEC -> reportBlueprint.getNec();
            case PYME -> reportBlueprint.getPyme();
        };
    }

    public static HojaEnBlancoSecion getHojaEnBlanco(Map<String, Map<String, Object>> actaBlueprint,
                                                     ReportBlueprint reportBlueprint, Path reportFilesRelativePath) {
        var blueprintSections = actaBlueprint.get(ReportResourcesConstants.BLUEPRINT_YML_SECCIONES).entrySet();
        for (Map.Entry<String, Object> blueprintSection : blueprintSections) {
            LinkedHashMap<String, Object> condicionales = (LinkedHashMap<String, Object>) blueprintSection.getValue();
            if (condicionales != null && condicionales.containsKey(ReportResourcesConstants.BLUEPRINT_TEXT_HOJA_EN_BLANCO)) {
                return HojaEnBlancoSecion.builder()
                        .reportBlueprint(reportBlueprint)
                        .reportFilesRelativePath(reportFilesRelativePath)
                        .build();
            }
        }
        return null;
    }

    public static ReportSection<?> createSections(Path reportFilesRelativePath,
                                                  Valoracioncompanyl valoracioncompanyl,
                                                  ReportBlueprint reportBlueprint,
                                                  Map.Entry<String, Object> blueprintSection) {
        return createSections(reportFilesRelativePath, valoracioncompanyl, reportBlueprint, blueprintSection,
                null);
    }

    public static ReportSection<?> createSections(Path reportFilesRelativePath,
                                                  Valoracioncompanyl valoracioncompanyl,
                                                  ReportBlueprint reportBlueprint,
                                                  Map.Entry<String, Object> blueprintSection,
                                                  Map<String, Object> parametrosExtras) {
        String sectionName = blueprintSection.getKey();
        if (sectionName.contains(ReportResourcesConstants.PDF_EXTENSION)) {
            String sectionPdfFilePath = reportFilesRelativePath + File.separator + sectionName;
            return PdfSection.builder()
                    .relativePath(sectionPdfFilePath)
                    .build();
        } else if (sectionName.contains(ReportResourcesConstants.COMPILED_JASPER_REPORT_EXTENSION)) {
            return JasperSection.builder()
                    .valoracioncompanyl(valoracioncompanyl)
                    .reportBlueprint(reportBlueprint)
                    .sectionName(sectionName)
                    .anexoFileExtension(ReportFileExtensionEnum.PDF)
                    .reportFilesRelativePath(reportFilesRelativePath)
                    .parametrosExtras(parametrosExtras)
                    .build();
        } else return null;
    }

    public static HojaEnBlancoSecion addBlankSheetIfRequired(Map.Entry<String, Object> blueprintSection,
                                                             HojaEnBlancoSecion hojaEnBlanco) {
        LinkedHashMap<String, Object> condicionales = (LinkedHashMap<String, Object>) blueprintSection.getValue();
        // If condicional contains hoja en blanco, generate one
        if (condicionales != null && condicionales.containsKey(ReportResourcesConstants.BLUEPRINT_TEXT_HOJA_EN_BLANCO)) {
            return hojaEnBlanco;
        }
        return null;
    }

    public static PDFMergerUtility createPdfMergerUtility(List<ReportSection<?>> reportSections) throws coreException {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        for (var section : reportSections) {
            addSectionToPdfMerger(section, pdfMergerUtility);
        }
        return pdfMergerUtility;
    }

    public static PDFMergerUtility createPdfMergerUtility(ReportSection<?> reportSection) throws coreException {
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        addSectionToPdfMerger(reportSection, pdfMergerUtility);
        return pdfMergerUtility;
    }

    public static void addSectionToPdfMerger(ReportSection<?> section, PDFMergerUtility pdfMergerUtility)
            throws coreException {
        Object pdfFile = section.getFilePath();
        try {
            // Significa que getFile() es el path al archivo
            if (pdfFile instanceof String) {
                pdfMergerUtility.addSource((String) pdfFile);
            }
            // Significa que getFile() retorna el InputStream de un PDF
            else if (pdfFile instanceof InputStream) {
                pdfMergerUtility.addSource((InputStream) pdfFile);
            }
        } catch (FileNotFoundException e) {
            String errMsg = "Failed to merge PDF file because '" + pdfFile + "' does not exist.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String createTempFilename(Valoracioncompanyl valoracioncompanyl) {
        // Convert it to a string
        String randomString = UUID.randomUUID().toString();
        return "P" + valoracioncompanyl.getNumeroProceso() + "_" + randomString;
    }


    public static void mergeFilesToCreatePdfReport(PDFMergerUtility pdfMergerUtility, File reportFile)
            throws coreException {
        try (OutputStream out = new FileOutputStream(reportFile)) {
            pdfMergerUtility.setDestinationStream(out);
            pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
        } catch (FileNotFoundException e) {
            String errMsg = "Failed to create output stream because file '" + reportFile.getPath() + "' does not exist.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            String errMsg = "Failed to merge documents.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static void logOperationTime(long startTime) {
        final long estimatedTime = System.nanoTime() - startTime;
        long estimatedTime_inSeconds = TimeUnit.SECONDS.convert(estimatedTime, TimeUnit.NANOSECONDS);
        LOGGER.info("Successfully built report. Operation took " + estimatedTime_inSeconds + " seconds");
    }


    private static ReportBlueprint parseBlueprint(InputStream reportBlueprint) throws coreException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(reportBlueprint, ReportBlueprint.class);
        } catch (Exception e) {
            String errMsg = "Failed to parse report blueprint.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
