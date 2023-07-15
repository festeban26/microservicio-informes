package ec.com.company.microservices.microservicioinformes.reports.util;

import ec.com.company.microservices.microservicioinformes.constants.AppConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ActaReportType;

/**
 * A resolver class that resolves the full name of an acta file in Google Cloud Storage based on some inputs
 */
public abstract class ActasGcsFilenameResolver {
    public static final String ACTA_BASE_FILE_PATH = "actasFiniquito" + AppConstants.CLOUD_STORAGE_DELIMITER
            + "%s" + AppConstants.CLOUD_STORAGE_DELIMITER
            + "entregables" + AppConstants.CLOUD_STORAGE_DELIMITER
            + "%s" + AppConstants.CLOUD_STORAGE_DELIMITER;

    public static String getFilename(String numeroProceso,
                                      String identificacionPersona,
                                     String versionInforme,
                                      ActaReportType actaReportType) {

        String baseFilePath = String.format(ACTA_BASE_FILE_PATH, numeroProceso, identificacionPersona);
        String filename = switch (actaReportType) {
            case REPORTE_PRINCIPAL -> "acta-version-v" + versionInforme + ".pdf";
            case ANEXO_RESUMEN_EXPLICATIVO -> "anexo-resumen-explicativo-version-v" + versionInforme + ".pdf";
        };
        return baseFilePath + filename;
    }
}
