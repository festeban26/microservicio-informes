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
public class ActaAnnexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActaAnnexService.class);

    @Autowired
    public ActaAnnexService(Storage storage) {
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
    public URL getAnnexUrl(String numeroProceso,
                           String identificacionPersona,
                           String versionInforme,
                           String filename)
            throws coreException {

        String fileName = ActasGcsFilenameResolver.getFilename(numeroProceso,
                identificacionPersona, versionInforme, ActaReportType.ANEXO_RESUMEN_EXPLICATIVO);
        BlobId blobId = BlobId.of(bucketName, fileName);
        // Get the blob from Google Cloud Storage by file name
        Blob blob = storage.get(blobId);
        if (blob == null) {
            String errMsg = "File " + fileName + " not found on bucket " + bucketName;
            throw new coreException(errMsg, HttpStatus.NOT_FOUND);
        }
        BlobInfo blobInfo = blob.toBuilder().build();

        String defaultFilename = ReportResourcesConstants.ACTAS_ANEXO_DEFAULT_FILENAME;
        return BlobUrlSigner.signUrl(storage, blobInfo, filename, defaultFilename, ReportFileExtensionEnum.PDF);
    }

    /**
     * Method to handle @PostMapping requests. On CRUD this will be the CREATE operation.
     */
    public URL createAnnex(Acta actaFiniquito,
                           String numeroProceso,
                           String identificacionPersona,
                           String versionInforme,
                           String fileToDownloadName)
            throws coreException {

        String filename = ActasGcsFilenameResolver.getFilename(numeroProceso,
                identificacionPersona, versionInforme, ActaReportType.ANEXO_RESUMEN_EXPLICATIVO);
        BlobId blobId = BlobId.of(bucketName, filename);
        Blob blob = storage.get(blobId, Storage.BlobGetOption.fields());
        // If file already exists.
        if (blob != null && blob.exists()) {
            String errMsg = "File " + filename + " already exists on bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }
        // Create the report
        File report = new ActaReport().getActaReport(actaFiniquito, ActaReportType.ANEXO_RESUMEN_EXPLICATIVO);
        LOGGER.debug("Report created: " + report.getPath());
        try (FileInputStream fis = new FileInputStream(report)) {
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            storage.create(blobInfo, fis.readAllBytes());

            String defaultFilename = ReportResourcesConstants.ACTAS_ANEXO_DEFAULT_FILENAME;
            return BlobUrlSigner.signUrl(storage, blobInfo, fileToDownloadName, defaultFilename, ReportFileExtensionEnum.PDF);

        } catch (IOException e) {
            var errMsg = "Error creating file " + filename;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




































