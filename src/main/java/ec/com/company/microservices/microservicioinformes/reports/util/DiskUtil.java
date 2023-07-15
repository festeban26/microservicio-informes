package ec.com.company.microservices.microservicioinformes.reports.util;

import ec.com.company.microservices.microservicioinformes.exception.coreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;

public class DiskUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskUtil.class);

    public static File createTempFile(String prefix, String suffix) throws coreException {
        try {
            return File.createTempFile(prefix + "_", suffix);
        } catch (IOException e) {
            String errMsg = "Failed to create temp file '" + prefix + suffix + "'.";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
