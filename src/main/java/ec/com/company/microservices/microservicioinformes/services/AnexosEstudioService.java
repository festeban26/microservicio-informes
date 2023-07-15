package ec.com.company.microservices.microservicioinformes.services;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.AnexoEstudio;
import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
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
public class AnexosEstudioService extends BaseReportService {

    @Autowired
    public AnexosEstudioService(Storage storage) {
        super(storage);
    }

    public URL getReportUrl(AnexoEstudioEnum anexoEstudioEnum, String numeroProceso, String versionInforme, String filename,
                            ReportFileExtensionEnum reportFileExtensionEnum)
            throws FileNotFoundException, coreException {
        BlobInfo blobInfo = getBlobInfo(anexoEstudioEnum, numeroProceso, versionInforme, reportFileExtensionEnum);
        if (blobInfo == null) {
            throw new FileNotFoundException("File not found");
        }
        return getSignedUrl(blobInfo, filename, reportFileExtensionEnum);
    }

    public URL createReport(Estudio estudio, String anexoEstudio, String numeroProceso, String versionInforme,
                            String fileToDownloadName, String fileExtension)
            throws coreException {
        // Determine file type based on fileExtension
        ReportFileExtensionEnum reportFileExtensionEnum = switch (fileExtension) {
            case "pdf" -> ReportFileExtensionEnum.PDF;
            case "xlsx" -> ReportFileExtensionEnum.EXCEL;
            // If neither pdf nor xlsx, throw IllegalArgumentException
            default -> {
                String errMsg = "File extension " + fileExtension + " is not supported";
                LOGGER.error(errMsg);
                throw new coreException(errMsg, HttpStatus.BAD_REQUEST);
            }
        };
        // Convert anexoEstudio string to AnexoEstudioEnum type
        AnexoEstudioEnum anexoEstudioEnum = AnexoEstudioEnum.valueOf(anexoEstudio);

        BlobInfo blobInfo = getBlobInfo(anexoEstudioEnum, numeroProceso, versionInforme, reportFileExtensionEnum);
        if (blobInfo != null) {
            String errMsg = "File " + blobInfo.getBlobId().getName() + " already exists on the bucket " + bucketName;
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.CONFLICT);
        }

        // Create the report
        File report = new AnexoEstudio().getReport(estudio, anexoEstudioEnum, reportFileExtensionEnum);
        blobInfo = uploadAnexoReport(report, numeroProceso, versionInforme, anexoEstudioEnum, reportFileExtensionEnum);
        return getSignedUrl(blobInfo, fileToDownloadName, reportFileExtensionEnum);
    }

    protected BlobInfo getBlobInfo(AnexoEstudioEnum anexoEstudioEnum, String numeroProceso, String versionInforme, ReportFileExtensionEnum reportFileExtensionEnum) {
        String filename = getFileNameResolver().get(numeroProceso, versionInforme, anexoEstudioEnum, reportFileExtensionEnum);
        return retrieveBlobInfo(filename);
    }

    private BlobInfo uploadAnexoReport(File report, String numeroProceso, String versionInforme, AnexoEstudioEnum anexoEstudioEnum,
                                       ReportFileExtensionEnum reportFileExtensionEnum) throws coreException {
        String fileName = getFileNameResolver().get(numeroProceso, versionInforme, anexoEstudioEnum, reportFileExtensionEnum);
        String contentType = switch (reportFileExtensionEnum) {
            case PDF -> "application/pdf";
            case EXCEL -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        };
        return uploadReport(report, fileName, contentType);
    }

    @Override
    protected ReportFileNameResolver getFileNameResolver() {
        return new EstudiosGcsFilenameResolver();
    }
}