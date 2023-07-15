package ec.com.company.microservices.microservicioinformes.reports.util;

import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class ResourcesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourcesUtil.class);

    private ResourcesUtil() {
        // Private constructor to prevent direct instantiation
    }

    /**
     * Get the singleton instance of ResourcesUtil.
     *
     * @return The ResourcesUtil instance.
     */
    public static ResourcesUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ResourcesUtil INSTANCE = new ResourcesUtil();
    }

    /**
     * Get the InputStream for a Jasper file.
     *
     * @param archivosReportesRelativePath The relative path of the archivosReportes directory.
     * @param jasperFilename               The name of the Jasper file.
     * @return The InputStream for the Jasper file.
     * @throws coreException If the file is not found or an error occurs.
     */
    public InputStream getJasperFile(Path archivosReportesRelativePath, String jasperFilename) throws coreException {
        String jasperFileRelativePath = archivosReportesRelativePath.resolve(jasperFilename).toString();
        LOGGER.debug("Getting resource: {}", jasperFileRelativePath);
        InputStream jasperFile = getResourceAsFile(jasperFileRelativePath);
        if (jasperFile == null) {
            String errMsg = "Archivo no encontrado en el servidor: " + jasperFileRelativePath;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return jasperFile;
    }

    /**
     * Get the InputStream for a resource file.
     *
     * @param relativeFilePath The relative path of the resource file.
     * @return The InputStream for the resource file.
     * @throws coreException If the file is not found or an error occurs.
     */
    public InputStream getResourceAsFile(String relativeFilePath) throws coreException {
        LOGGER.debug("Getting resource: {}", relativeFilePath);
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(relativeFilePath);
            if (inputStream == null) {
                String errMsg = "Recurso no encontrado en el servidor: " + relativeFilePath;
                LOGGER.error(errMsg);
                throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return inputStream;
        } catch (Exception e) {
            String errMsg = "Error al obtener el recurso: " + relativeFilePath;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the relative path for report files.
     *
     * @param reportType   The type of the report.
     * @param fechaCalculo The calculation date.
     * @return The relative path for report files.
     */
    public Path getReportFilesRelativePath(ReportType reportType, Date fechaCalculo) {
        String reportPath = getReportPath(reportType);
        int anioValoracion = getYearValue(fechaCalculo);
        Path reportFilesPath = Paths.get(ReportResourcesConstants.REPORTS_PATHNAME,
                String.valueOf(anioValoracion),
                reportPath);
        LOGGER.debug("Report files relative path: {}", reportFilesPath);
        return reportFilesPath;
    }

    private String getReportPath(ReportType reportType) {
        return switch (reportType) {
            case ESTUDIO_companyL -> ReportResourcesConstants.ESTUDIO_company_REPORT_PATHNAME;
            case ACTA_FINIQUITO -> ReportResourcesConstants.ACTA_FINIQUITO_REPORT_PATHNAME;
        };
    }

    private int getYearValue(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}