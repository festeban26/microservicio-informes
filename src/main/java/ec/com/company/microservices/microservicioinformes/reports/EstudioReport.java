package ec.com.company.microservices.microservicioinformes.reports;

import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.core.estudio.model.jubilacion.ResultadoJubilacion;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportType;
import ec.com.company.microservices.microservicioinformes.reports.enums.TipoNormativaEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.factory.HojaEnBlancoSecion;
import ec.com.company.microservices.microservicioinformes.reports.factory.ReportSection;
import ec.com.company.microservices.microservicioinformes.reports.util.DiskUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ReportUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ResourcesUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstudioReport {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudioReport.class);

    private File estudioReport;

    public File getReport(Estudio estudio) throws coreException {
        if (this.estudioReport == null) {
            this.estudioReport = buildReport(estudio);
        }
        return this.estudioReport;
    }

    private File buildReport(Estudio estudio) throws coreException {

        LOGGER.info("Building estudio report...");

        final long startTime = System.nanoTime();

        Path reportFilesRelativePath = ResourcesUtil.getInstance().getReportFilesRelativePath(ReportType.ESTUDIO_companyL, estudio.parametros().getFechaCalculo());
        ReportBlueprint reportBlueprint = ReportUtil.getReportBlueprint(reportFilesRelativePath);

        TipoNormativaEstudioEnum tipoNormativaEstudio = TipoNormativaEstudioEnum.getValueOf(estudio.codigoNormativa());
        Map<String, Map<String, Object>> estudioBlueprint = ReportUtil.getEstudioBlueprint(tipoNormativaEstudio, reportBlueprint);
        HojaEnBlancoSecion hojaEnBlanco = ReportUtil.getHojaEnBlanco(estudioBlueprint, reportBlueprint, reportFilesRelativePath);

        var blueprintSections = estudioBlueprint.get(ReportResourcesConstants.BLUEPRINT_YML_SECCIONES);

        List<ReportSection<?>> reportSections = new ArrayList<>();

        // Extract estudioResultJubilacion once to avoid multiple calls
        ResultadoJubilacion resultadoJubilacion = estudio.resultadoJubilacion();

        int numeroDeAnexo = 0;
        for (Map.Entry<String, Object> blueprintSection : blueprintSections.entrySet()) {
            String key = blueprintSection.getKey();

            // Check if the estudio has valid salidas
            boolean tieneSalidas = resultadoJubilacion != null
                    && resultadoJubilacion.resultadoSalidas() != null;
            boolean isSalidasSection = key.equalsIgnoreCase(AnexoEstudioEnum.JUBILACION_ANEXO_SALIDAS.getNombreArchivo())
                    || key.equalsIgnoreCase(AnexoEstudioEnum.JUBILACION_ANEXO_SALIDAS_NEC.getNombreArchivo())
                    || key.equalsIgnoreCase(AnexoEstudioEnum.DESAHUCIO_ANEXO_SALIDAS.getNombreArchivo())
                    || key.equalsIgnoreCase(AnexoEstudioEnum.DESAHUCIO_ANEXO_SALIDAS_NEC.getNombreArchivo());

            // Skip the loop iteration if it doesn't have salidas and is a salidas section
            if (!tieneSalidas && isSalidasSection) {
                continue;
            }

            // Check if the estudio has valid traspasos
            boolean tieneTraspasos = resultadoJubilacion != null
                    && resultadoJubilacion.resultadoTraspasos() != null
                    && !CollectionUtils.isEmpty(resultadoJubilacion.resultadoTraspasos().personas());
            boolean isTraspasosSection = key.equalsIgnoreCase("anexoTraspasosJubilacion.jasper")
                    || key.equalsIgnoreCase("anexoTraspasosDesahucio.jasper");

            // Skip the loop iteration if it doesn't have traspasos and is a traspasos section
            if (!tieneTraspasos && isTraspasosSection) {
                continue;
            }

            // Incluir parámetro de número de anexo
            Map<String, Object> parametrosExtras = new HashMap<>();

            // Resetear el contador de anexos para la sección de desahucio
            if (key.toLowerCase().equalsIgnoreCase("moduloBonificacionDesahucioAnexoResumenAplicacion.jasper")
                    || key.toLowerCase().equalsIgnoreCase("moduloBonificacionDesahucioAnexoComposicionDemograficaNec.jasper")) {
                numeroDeAnexo = 0;
            }

            // Si la seccion es un anexo, se incluye el parametro de numero de anexo y se incrementa el contador
            if (key.toLowerCase().contains(ReportResourcesConstants.ESTUDIOS_companyLES_TEXTO_ANEXOS)) {
                parametrosExtras.put(ReportResourcesConstants.ESTUDIOS_companyLES_PARAMETRO_NUMERO_ANEXO, ++numeroDeAnexo);
            }

            reportSections.add(
                    ReportUtil.createSections(reportFilesRelativePath, estudio, reportBlueprint, blueprintSection, parametrosExtras)
            );
            HojaEnBlancoSecion hojaEnBlancoSecion = ReportUtil.addBlankSheetIfRequired(blueprintSection, hojaEnBlanco);
            if (hojaEnBlancoSecion != null) {
                reportSections.add(hojaEnBlancoSecion);
            }
        }

        PDFMergerUtility pdfMergerUtility = ReportUtil.createPdfMergerUtility(reportSections);

        String tempFilename = ReportUtil.createTempFilename(estudio);
        File reportFile = DiskUtil.createTempFile(tempFilename + "_", ".pdf");

        ReportUtil.mergeFilesToCreatePdfReport(pdfMergerUtility, reportFile);

        ReportUtil.logOperationTime(startTime);
        return reportFile;
    }
}
