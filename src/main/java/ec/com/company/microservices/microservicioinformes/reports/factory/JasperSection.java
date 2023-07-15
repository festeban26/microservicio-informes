package ec.com.company.microservices.microservicioinformes.reports.factory;

import ec.com.company.core.interfaces.Valoracioncompanyl;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.ReportBlueprint;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.util.JasperReportBuilderUtil;
import ec.com.company.microservices.microservicioinformes.reports.util.ProveedorParametrosUtil;
import lombok.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class JasperSection implements ReportSection<String> {
    // The parameters required for building the JasperSection
    private final Valoracioncompanyl valoracioncompanyl;
    private final ReportBlueprint reportBlueprint;
    private final String sectionName;
    private final ReportFileExtensionEnum anexoFileExtension;
    private final Path reportFilesRelativePath;
    private final Map<String, Object> parametrosExtras;

    @Builder
    public JasperSection(@NonNull Valoracioncompanyl valoracioncompanyl,
                         @NonNull ReportBlueprint reportBlueprint,
                         @NonNull String sectionName,
                         @NonNull ReportFileExtensionEnum anexoFileExtension,
                         @NonNull Path reportFilesRelativePath,
                         // Son los parámetros específicos de la sección, por ejemplo, el número de anexo en
                         // los estudios companyles
                         Map<String, Object> parametrosExtras) {
        this.valoracioncompanyl = valoracioncompanyl;
        this.reportBlueprint = reportBlueprint;
        this.sectionName = sectionName;
        this.anexoFileExtension = anexoFileExtension;
        this.reportFilesRelativePath = reportFilesRelativePath;
        this.parametrosExtras = parametrosExtras;
    }

    private String filePath;

    @Override
    public String getFilePath() throws coreException {
        if (this.filePath == null) {
            Map<String, Object> parametrosSeccion = ProveedorParametrosUtil
                    .getJasperSectionParameters(valoracioncompanyl, reportBlueprint, reportFilesRelativePath);

            if (parametrosExtras != null) {
                parametrosSeccion.putAll(parametrosExtras);
            }

            File section = switch (anexoFileExtension) {
                case PDF -> JasperReportBuilderUtil.getInstance().generateJasperSectionPdf(
                        sectionName,
                        reportFilesRelativePath,
                        parametrosSeccion);
                case EXCEL -> JasperReportBuilderUtil.getInstance().generateJasperSectionExcel(
                        sectionName,
                        reportFilesRelativePath,
                        parametrosSeccion);
            };

            this.filePath = section.getPath();
        }
        return this.filePath;
    }

    public static File getFileFromPath(String filePath) {
        return Paths.get(filePath).toFile();
    }
}


