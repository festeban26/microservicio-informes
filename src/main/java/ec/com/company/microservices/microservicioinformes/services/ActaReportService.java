package ec.com.company.microservices.microservicioinformes.services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.com.company.core.actas.model.Acta;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.ActaReport;
import ec.com.company.microservices.microservicioinformes.reports.builders.BlobUrlSigner;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import ec.com.company.microservices.microservicioinformes.reports.enums.ActaReportType;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.reports.util.ActasGcsFilenameResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

@Service
public class ActaReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActaReportService.class);

    @Autowired
    public ActaReportService(Storage storage) {
        this.storage = storage;
    }

    // Inject the Google Cloud Storage service
    private final Storage storage;

    // inject from application.properties
    @Value("${bucket.name}")
    private String bucketName;


    /**
     * Method to handle @GetMapping requests. On CRUD this will be the READ operation.
     */
    public URL getReportUrl(String numeroProceso,
                            String identificacionPersona,
                            String versionInforme,
                            String filename)
            throws coreException {

        String fileName = ActasGcsFilenameResolver.getFilename(numeroProceso,
                identificacionPersona, versionInforme, ActaReportType.REPORTE_PRINCIPAL);
        BlobId blobId = BlobId.of(bucketName, fileName);
        // Get the blob from Google Cloud Storage by file name
        Blob blob = storage.get(blobId);
        if (blob == null) {
            String errMsg = "File " + filename + " already exists on bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }
        BlobInfo blobInfo = blob.toBuilder().build();

        String defaultFilename = ReportResourcesConstants.ACTAS_DEFAULT_FILENAME;
        return BlobUrlSigner.signUrl(storage, blobInfo, filename, defaultFilename, ReportFileExtensionEnum.PDF);
    }

    /**
     * Method to handle @PostMapping requests. On CRUD this will be the CREATE operation.
     */
    public URL createReport(Acta actaFiniquito,
                            String numeroProceso,
                            String identificacionPersona,
                            String versionInforme,
                            String fileToDownloadName)
            throws coreException {

        String filename = ActasGcsFilenameResolver.getFilename(numeroProceso,
                identificacionPersona, versionInforme, ActaReportType.REPORTE_PRINCIPAL);
        BlobId blobId = BlobId.of(bucketName, filename);
        Blob blob = storage.get(blobId, Storage.BlobGetOption.fields());
        // If file already exists.
        if (blob != null && blob.exists()) {
            String errMsg = "File " + filename + " already exists on bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }
        // Create the acta report
        File report = new ActaReport().getActaReport(actaFiniquito, ActaReportType.REPORTE_PRINCIPAL);
        LOGGER.debug("Report created: " + report.getPath());
        try (FileInputStream fis = new FileInputStream(report)) {
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            storage.create(blobInfo, fis.readAllBytes());

            String defaultFilename = ReportResourcesConstants.ACTAS_DEFAULT_FILENAME;
            return BlobUrlSigner.signUrl(storage, blobInfo, fileToDownloadName, defaultFilename, ReportFileExtensionEnum.PDF);

        } catch (IOException e) {
            var errMsg = "Error creating file " + filename;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method to handle @PutMapping requests. On CRUD this will be the PUT operation.
     * TODO
     */
    /*public URL updateActaFiniquitoReport(Acta actaFiniquito,
                                         String numeroProceso,
                                         String identificacionPersona,
                                         int versionInforme,
                                         String filename)
            throws FileNotFoundException, ServiceException {

        String fileName = getStandardFileName(numeroProceso, identificacionPersona, versionInforme);
        BlobId blobId = BlobId.of(bucketName, fileName);
        Blob blob = storage.get(blobId, Storage.BlobGetOption.fields());
        // If file does not exist.
        if (blob == null || !blob.exists()) {
            var msg = fileName + " does not exist on the bucket " + bucketName;
            throw new FileNotFoundException(msg);
        }
        // Create the report
        File actaReport = new ActaReport().buildActaReport(actaFiniquito);
        try(FileInputStream fis = new FileInputStream(actaReport)){
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            storage.create(blobInfo, fis.readAllBytes());
            return getUrlFromBlobInfo(blobInfo, filename);

        } catch (IOException e) {
            // use a more descriptive message and include the file name
            var msg = "Error reading file " + actaReport.getPath() + " from disk";
            logger.error(msg, e);
            throw new ServiceException(msg + e);
        }
    }
    private static String getStandardFileName(String numeroProceso, String identificacionPersona, int versionInforme) {
        return "actasFiniquito" + AppConstants.CLOUD_STORAGE_DELIMITER
                + numeroProceso + AppConstants.CLOUD_STORAGE_DELIMITER
                + "entregables" + AppConstants.CLOUD_STORAGE_DELIMITER
                + identificacionPersona + AppConstants.CLOUD_STORAGE_DELIMITER
                + "version" + versionInforme + ".pdf";
    }*/
}




































