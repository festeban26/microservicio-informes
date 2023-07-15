package ec.com.company.microservices.microservicioinformes.reports;

import ec.com.company.core.actas.model.Acta;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ActaReportType;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportType;
import ec.com.company.microservices.microservicioinformes.reports.factory.HojaEnBlancoSecion;
import ec.com.company.microservices.microservicioinformes.reports.factory.ReportSection;
import ec.com.company.microservices.microservicioinformes.reports.util.DiskUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ReportUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ResourcesUtil;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class ActaReport {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActaReport.class);

    // Since actas reportes do not change over time, the path to the report files is hardcoded
    public static final Date ACTAS_DATE =  new GregorianCalendar(2022, Calendar.DECEMBER, 31).getTime();

    private File actaReport;

    public File getActaReport(Acta actaFiniquito, ActaReportType actaReportType) throws coreException {

        if (this.actaReport == null) {
            this.actaReport = buildActaReport(actaFiniquito, actaReportType);
        }
        return this.actaReport;
    }

    private File buildActaReport(Acta actaFiniquito, ActaReportType actaReportType) throws coreException {

        LOGGER.info("Building acta report...");

        final long startTime = System.nanoTime();

        Path reportFilesRelativePath = ResourcesUtil.getInstance().getReportFilesRelativePath(ReportType.ACTA_FINIQUITO, ACTAS_DATE);
        ReportBlueprint reportBlueprint = ReportUtil.getReportBlueprint(reportFilesRelativePath);
        Map<String, Map<String, Object>> actaBlueprint = ReportUtil.getActaBlueprint(actaReportType, reportBlueprint);
        HojaEnBlancoSecion hojaEnBlanco = ReportUtil.getHojaEnBlanco(actaBlueprint, reportBlueprint, reportFilesRelativePath);

        var blueprintSections = actaBlueprint.get(ReportResourcesConstants.BLUEPRINT_YML_SECCIONES);

        List<ReportSection<?>> reportSections = new ArrayList<>();

        for (Map.Entry<String, Object> blueprintSection : blueprintSections.entrySet()) {
            reportSections.add(ReportUtil.createSections(reportFilesRelativePath, actaFiniquito, reportBlueprint, blueprintSection));
            HojaEnBlancoSecion hojaEnBlancoSecion = ReportUtil.addBlankSheetIfRequired(blueprintSection, hojaEnBlanco);
            if(hojaEnBlancoSecion != null){
                reportSections.add(hojaEnBlancoSecion);
            }
        }

        PDFMergerUtility pdfMergerUtility = ReportUtil.createPdfMergerUtility(reportSections);

        String tempFilename = ReportUtil.createTempFilename(actaFiniquito);
        File reportFile = DiskUtil.createTempFile(tempFilename + "_", ".pdf");

        ReportUtil.mergeFilesToCreatePdfReport(pdfMergerUtility, reportFile);

        ReportUtil.logOperationTime(startTime);
        return reportFile;
    }




}
