package ec.com.company.microservices.microservicioinformes.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.builders.BlobUrlSigner;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.interfaces.ReportFileNameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public abstract class BaseReportService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseReportService.class);

    protected final Storage storage;

    @Value("${bucket.name}")
    protected String bucketName;

    public BaseReportService(Storage storage) {
        this.storage = storage;
    }

    protected BlobInfo retrieveBlobInfo(String filename) {
        BlobId blobId = BlobId.of(bucketName, filename);
        Blob blob = storage.get(blobId);
        return blob != null ? blob.toBuilder().build() : null;
    }

    protected BlobInfo uploadReport(File report, String fileName, String contentType)
            throws coreException {
        try (FileInputStream fis = new FileInputStream(report)) {
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(contentType)
                    .build();
            storage.create(blobInfo, fis.readAllBytes());
            return blobInfo;
        } catch (IOException e) {
            var errMsg = "Error reading file " + report.getPath() + " from disk";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected URL getSignedUrl(BlobInfo blobInfo, String filename, ReportFileExtensionEnum extension)
            throws coreException {

        String defaultFilename = switch (extension) {
            case PDF -> ReportResourcesConstants.ESTUDIOS_companyLES_PDF_DEFAULT_FILENAME;
            case EXCEL -> ReportResourcesConstants.ESTUDIOS_companyLES_EXCEL_DEFAULT_FILENAME;
        };
        return BlobUrlSigner.signUrl(storage, blobInfo, filename, defaultFilename, extension);
    }

    protected abstract ReportFileNameResolver getFileNameResolver();
}
