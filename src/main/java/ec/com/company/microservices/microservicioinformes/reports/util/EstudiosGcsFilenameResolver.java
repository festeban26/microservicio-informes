package ec.com.company.microservices.microservicioinformes.reports.util;


import ec.com.company.microservices.microservicioinformes.constants.AppConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.interfaces.ReportFileNameResolver;

/**
 * A resolver class that resolves the full name of an estudio file in Google Cloud Storage based on some inputs
 */
public class EstudiosGcsFilenameResolver implements ReportFileNameResolver {
    public static final String ESTUDIO_BASE_FILE_PATH = "estudioscompanyles" + AppConstants.CLOUD_STORAGE_DELIMITER
            + "%s" + AppConstants.CLOUD_STORAGE_DELIMITER
            + "entregables" + AppConstants.CLOUD_STORAGE_DELIMITER;

    public static final String ANEXOS_BASE_FILE_PATH = ESTUDIO_BASE_FILE_PATH + "anexos" + AppConstants.CLOUD_STORAGE_DELIMITER;

    public static String getPdfReportFilename(String numeroProceso,
                                              String versionInforme) {

        String baseFilePath = String.format(ESTUDIO_BASE_FILE_PATH, numeroProceso);
        String filename = "estudio-version-v" + versionInforme + ".pdf";
        return baseFilePath + filename;
    }

    public static String getAnexoPdfReportFilename(String numeroProceso,
                                                   String versionInforme,
                                                   AnexoEstudioEnum anexoEstudioEnum,
                                                   ReportFileExtensionEnum reportFileExtensionEnum) {

        String baseFilePath = String.format(ANEXOS_BASE_FILE_PATH, numeroProceso);
        String fileExtension = switch (reportFileExtensionEnum) {
            case PDF -> ".pdf";
            case EXCEL -> ".xlsx";
        };
        String filename = anexoEstudioEnum.name() + "-version-v" + versionInforme + fileExtension;
        return baseFilePath + filename;
    }

    @Override
    public String get(String numeroProceso, String versionInforme) {
        return getPdfReportFilename(numeroProceso, versionInforme);
    }

    @Override
    public String get(String numeroProceso, String versionInforme, AnexoEstudioEnum anexoEstudioEnum, ReportFileExtensionEnum reportFileExtensionEnum) {
        return getAnexoPdfReportFilename(numeroProceso, versionInforme, anexoEstudioEnum, reportFileExtensionEnum);
    }
}
