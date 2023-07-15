package ec.com.company.microservices.microservicioinformes.services;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.EstudioReport;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.interfaces.ReportFileNameResolver;
import ec.com.company.microservices.microservicioinformes.reports.util.EstudiosGcsFilenameResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

@Service
public class EstudioReportService extends BaseReportService {

    @Autowired
    public EstudioReportService(Storage storage) {
        super(storage);
    }

    public URL getReportUrl(String numeroProceso, String versionInforme, String filename)
            throws coreException {
        BlobInfo blobInfo = getBlobInfo(numeroProceso, versionInforme);
        if (blobInfo == null) {
            String errMsg = "File " + filename + " not found on the bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }
        return getSignedUrl(blobInfo, filename, ReportFileExtensionEnum.PDF);
    }

    public URL createReport(Estudio estudio, String numeroProceso, String versionInforme,
                            String fileToDownloadName)
            throws coreException {
        BlobInfo blobInfo = getBlobInfo(numeroProceso, versionInforme);
        if (blobInfo != null) {
            String errMsg = "File " + blobInfo.getBlobId().getName() + " already exists on the bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }

        // Create the report
        File report = new EstudioReport().getReport(estudio);
        blobInfo = uploadEstudioReport(report, numeroProceso, versionInforme);
        return getSignedUrl(blobInfo, fileToDownloadName, ReportFileExtensionEnum.PDF);
    }

    protected BlobInfo getBlobInfo(String numeroProceso, String versionInforme) {
        String filename = getFileNameResolver().get(numeroProceso, versionInforme);
        return retrieveBlobInfo(filename);
    }

    private BlobInfo uploadEstudioReport(File report, String numeroProceso, String versionInforme)
            throws coreException {
        String fileName = getFileNameResolver().get(numeroProceso, versionInforme);
        return uploadReport(report, fileName, "application/pdf");
    }

    @Override
    protected ReportFileNameResolver getFileNameResolver() {
        return new EstudiosGcsFilenameResolver();
    }
}