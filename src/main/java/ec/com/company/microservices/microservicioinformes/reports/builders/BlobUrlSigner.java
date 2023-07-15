package ec.com.company.microservices.microservicioinformes.reports.builders;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import ec.com.company.microservices.microservicioinformes.constants.AppConstants;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.util.HashMap;

/**
 * This class provides a method to generate a signed URL for a given blob in a storage.
 */
public class BlobUrlSigner {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlobUrlSigner.class);

    /**
     * Returns a signed URL for a blob.
     * If any argument is null, this method will throw an IllegalArgumentException.
     * If filename is null or empty, this method will use defaultFilename.
     *
     * @param storage                 the Storage instance
     * @param blobInfo                the BlobInfo instance
     * @param filename                the name of the file
     * @param defaultFilename         the default name of the file
     * @param reportFileExtensionEnum the file extension
     * @return the signed URL
     * @throws IllegalArgumentException if storage, blobInfo, defaultFilename, or reportFileExtensionEnum is null
     */
    public static URL signUrl(Storage storage, BlobInfo blobInfo, String filename,
                              String defaultFilename, ReportFileExtensionEnum reportFileExtensionEnum)
            throws coreException {

        validateArguments(storage, blobInfo, defaultFilename, reportFileExtensionEnum);
        String validFilename = getFilename(filename, defaultFilename);
        String fileExtension = getFileExtension(reportFileExtensionEnum);

        LOGGER.info("Signing URL for file: " + validFilename + fileExtension);
        return signUrlWithBlob(storage, blobInfo, validFilename, fileExtension);
    }

    private static void validateArguments(Storage storage, BlobInfo blobInfo, String defaultFilename,
                                          ReportFileExtensionEnum reportFileExtensionEnum)
            throws coreException {
        if (storage == null || blobInfo == null || defaultFilename == null || reportFileExtensionEnum == null) {
            String errMsg = "One or more arguments are null on BlobUrlSigner.validateArguments. Please provide valid arguments.";
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String getFilename(String filename, String defaultFilename) {
        return (StringUtils.hasText(filename)) ? filename : defaultFilename;
    }

    private static String getFileExtension(ReportFileExtensionEnum reportFileExtensionEnum) {
        return switch (reportFileExtensionEnum) {
            case PDF -> ".pdf";
            case EXCEL -> ".xlsx";
        };
    }

    private static URL signUrlWithBlob(Storage storage, BlobInfo blobInfo, String validFilename, String fileExtension)
            throws coreException {
        String dispositionNotEncoded = "inline; filename=\"" + validFilename + fileExtension + "\"";
        var queryParams = new HashMap<String, String>();
        queryParams.put("response-content-disposition", dispositionNotEncoded);

        try {
            return storage.signUrl(blobInfo,
                    AppConstants.URL_TIMEOUT,
                    AppConstants.URL_TIMEOUT_TIME_UNITS,
                    Storage.SignUrlOption.withV4Signature(),
                    Storage.SignUrlOption.withQueryParams(queryParams));
        } catch (Exception e) {
            String errMsg = "Failed to sign URL for file: " + validFilename + fileExtension;
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
