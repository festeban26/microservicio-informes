package ec.com.company.microservices.microservicioinformes.reports.factory;

import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.util.ResourcesUtil;
import lombok.Builder;
import lombok.NonNull;

import java.io.InputStream;

public class PdfSection implements ReportSection<InputStream> {
    // The parameters required for building the JasperSection
    private final String relativePath;

    @Builder
    public PdfSection(@NonNull String relativePath) {
        this.relativePath = relativePath;
    }

    private InputStream pdfFile;

    @Override
    public InputStream getFilePath() throws coreException {
        if (this.pdfFile == null) {
            this.pdfFile = ResourcesUtil.getInstance().getResourceAsFile(relativePath);
        }
        return this.pdfFile;
    }
}
