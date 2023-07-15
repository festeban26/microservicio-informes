package ec.com.company.microservices.microservicioinformes.reports;

import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportType;
import ec.com.company.microservices.microservicioinformes.reports.factory.JasperSection;
import ec.com.company.microservices.microservicioinformes.reports.factory.ReportSection;
import ec.com.company.microservices.microservicioinformes.reports.util.DiskUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ReportUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ResourcesUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AnexoEstudio {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnexoEstudio.class);

    private File report;

    public File getReport(Estudio estudio, AnexoEstudioEnum anexoEstudio, ReportFileExtensionEnum anexoFileExtension)
            throws coreException {
        if (this.report == null) {
            this.report = buildReport(estudio, anexoEstudio, anexoFileExtension);
        }
        return this.report;
    }

    private File buildReport(Estudio estudio, AnexoEstudioEnum anexoEstudio, ReportFileExtensionEnum anexoFileExtension)
            throws coreException {

        LOGGER.info("Building anexo estudio " + anexoEstudio.getNombreArchivo() + "...");

        final long startTime = System.nanoTime();

        Path reportFilesRelativePath = ResourcesUtil.getInstance()
                .getReportFilesRelativePath(ReportType.ESTUDIO_companyL, estudio.parametros().getFechaCalculo());
        ReportBlueprint reportBlueprint = ReportUtil.getReportBlueprint(reportFilesRelativePath);

        // Incluir parámetro de número de anexo
        Map<String, Object> parametrosExtras = new HashMap<>();
        parametrosExtras.put(ReportResourcesConstants.ESTUDIOS_companyLES_PARAMETRO_NUMERO_ANEXO,
                anexoEstudio.getNumeroAnexo());

        ReportSection<String> anexo = JasperSection.builder()
                .valoracioncompanyl(estudio)
                .reportBlueprint(reportBlueprint)
                .sectionName(anexoEstudio.getNombreArchivo())
                .anexoFileExtension(anexoFileExtension)
                .reportFilesRelativePath(reportFilesRelativePath)
                .parametrosExtras(parametrosExtras)
                .build();

        // There is a difference on how files are created sin as Jun2023, the pdf report holds multiple jasper sections
        // while the excel report holds only one jasper section.
        File reportFile = switch (anexoFileExtension) {
            case PDF -> {
                String tempFilename = ReportUtil.createTempFilename(estudio);
                var tempFile = DiskUtil.createTempFile(tempFilename + "_", ".pdf");
                PDFMergerUtility pdfMergerUtility = ReportUtil.createPdfMergerUtility(anexo);
                ReportUtil.mergeFilesToCreatePdfReport(pdfMergerUtility, tempFile);
                yield tempFile;
            }
            case EXCEL -> {
                var tempFilepath = anexo.getFilePath();
                yield JasperSection.getFileFromPath(tempFilepath);
            }
        };

        ReportUtil.logOperationTime(startTime);
        return reportFile;
    }

}
