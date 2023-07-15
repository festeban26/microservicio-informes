package ec.com.company.microservices.microservicioinformes.reports.factory;

import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.ReportBlueprint;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.util.JasperReportBuilderUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ProveedorParametrosUtil;
import lombok.Builder;
import lombok.NonNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class HojaEnBlancoSecion implements ReportSection<String> {
    // The parameters required for building the JasperSection
    private final ReportBlueprint reportBlueprint;
    private final Path reportFilesRelativePath;

    @Builder
    public HojaEnBlancoSecion(@NonNull ReportBlueprint reportBlueprint,
                              @NonNull Path reportFilesRelativePath) {
        this.reportBlueprint = reportBlueprint;
        this.reportFilesRelativePath = reportFilesRelativePath;
    }

    private String filePath;

    @Override
    public String getFilePath() throws coreException {
        if (this.filePath == null) {
            Map<String, Object> imageParams = ProveedorParametrosUtil
                    .getImagesParams(reportBlueprint, reportFilesRelativePath);
            File section = JasperReportBuilderUtil.getInstance().generateJasperSectionPdf(
                    ReportResourcesConstants.BLUEPRINT_HOJA_EN_BLANCO_SECTION_FILENAME,
                    reportFilesRelativePath,
                    imageParams);
            this.filePath = section.getPath();
        }
        return this.filePath;
    }
}
