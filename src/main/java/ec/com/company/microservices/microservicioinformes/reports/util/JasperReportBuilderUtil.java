package ec.com.company.microservices.microservicioinformes.reports.util;


import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.configs.JasperXlsxReportConfiguration;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

public class JasperReportBuilderUtil<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportBuilderUtil.class);

    private static JasperReportBuilderUtil<?> instance;

    private JasperReportBuilderUtil() {
    }

    public static JasperReportBuilderUtil<?> getInstance() {
        if (instance == null) {
            instance = new JasperReportBuilderUtil<>();
        }
        return instance;
    }

    // Returns the report file
    public File generateJasperSectionPdf(String sectionName,
                                         Path reportFilesRelativePath,
                                         Map<String, Object> parametrosSeccion)
            throws coreException {

        LOGGER.info("Building jasper section " + sectionName + "...");
        JasperPrint jasperPrint = getJasperPrint(sectionName, reportFilesRelativePath, parametrosSeccion);
        return exportJasperSection(jasperPrint, sectionName, ReportFileExtensionEnum.PDF);
    }

    public File generateJasperSectionExcel(String sectionName,
                                           Path reportFilesRelativePath,
                                           Map<String, Object> parametrosSeccion)
            throws coreException {

        LOGGER.info("Building jasper section " + sectionName + "...");
        JasperPrint jasperPrint = getJasperPrint(sectionName, reportFilesRelativePath, parametrosSeccion);
        return exportJasperSection(jasperPrint, sectionName, ReportFileExtensionEnum.EXCEL);
    }

    private JasperPrint getJasperPrint(String sectionName,
                                       Path reportFilesRelativePath,
                                       Map<String, Object> parametrosSeccion)
            throws coreException {

        JasperPrint jasperPrint;
        try (InputStream jasperFile = ResourcesUtil.getInstance()
                .getJasperFile(reportFilesRelativePath, sectionName)) {
            jasperPrint = JasperFillManager.fillReport(
                    jasperFile,
                    parametrosSeccion,
                    new JREmptyDataSource());
        } catch (IOException e) {
            String errMsg = "Failed to get jasper file '" + sectionName + "'. " + e;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JRException e) {
            String errMsg = "Failed to fill jasper section '" + sectionName + "'. " + e;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return jasperPrint;
    }

    private File exportJasperSection(JasperPrint jasperPrint, String sectionName, ReportFileExtensionEnum reportFileExtensionEnum)
            throws coreException {
        UUID uuid = UUID.randomUUID();
        String randomString = uuid.toString();

        String tempFilename = sectionName.replace(ReportResourcesConstants.COMPILED_JASPER_REPORT_EXTENSION, "")
                + "_" + randomString
                + "_";

        String extension = switch (reportFileExtensionEnum) {
            case PDF -> ".pdf";
            case EXCEL -> ".xlsx";
        };

        File tempFile = DiskUtil.createTempFile(tempFilename, extension);

        try (OutputStream out = new FileOutputStream(tempFile)) {

            switch (reportFileExtensionEnum) {
                case PDF:
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                case EXCEL:
                    SimpleXlsxReportConfiguration config = JasperXlsxReportConfiguration.getSimpleXlsxReportConfiguration();
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                    exporter.setConfiguration(config);
                    exporter.exportReport();
                    out.flush();
            }

            LOGGER.info(String.format("Successfully generated jasper section into %s file. File saved on: '%s'",
                    reportFileExtensionEnum.name().toUpperCase(), tempFile.getPath()));
            return tempFile;
        } catch (FileNotFoundException e) {
            String errMsg = "Failed to create output stream because file '" + tempFile.getPath() + "' does not exist.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            String errMsg = "Failed to create output stream because of an IO exception. " + e;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JRException e) {
            String errMsg = String.format("JRException while trying to export report %s to %s stream. %s",
                    sectionName, reportFileExtensionEnum.name(), e);
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
