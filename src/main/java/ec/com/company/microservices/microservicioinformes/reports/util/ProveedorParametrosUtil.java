package ec.com.company.microservices.microservicioinformes.reports.util;

import ec.com.company.core.actas.model.Acta;
import ec.com.company.core.estudio.model.ActualizacionTasaFinanciera;
import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.core.interfaces.Valoracioncompanyl;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.ReportBlueprint;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import net.sf.jasperreports.engine.JRParameter;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

// TODO EMFA improve logging
public class ProveedorParametrosUtil<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProveedorParametrosUtil.class);

    private static final String PATH_ACTUALIZACION_TASA_FINANCIERA_DATE_FORMAT = "yyyy-MM-dd";

    public static Map<String, Object> getImagesParams(ReportBlueprint reportBlueprint, Path reportFilesRelativePath)
            throws coreException {
        LOGGER.debug("Getting image parameters");

        Map<String, Object> imageParams = new HashMap<>();
        if (reportBlueprint.getImagenes() != null) {
            for (String imageFilename : reportBlueprint.getImagenes()) {
                var image = Map.ofEntries(getImageParam(imageFilename, reportFilesRelativePath));
                imageParams.putAll(image);
            }
        }
        return imageParams;
    }

    private static Map.Entry<String, Object> getImageParam(String imageFilename, Path reportFilesRelativePath)
            throws coreException {

        String imageFileRelativePath = reportFilesRelativePath
                .resolve(ReportResourcesConstants.REPORT_IMAGES_RESOURCES)
                .resolve(imageFilename)
                .toString();
        return getImageEntry(imageFilename, imageFileRelativePath);
    }

    private static Map.Entry<String, Object> getImageEntry(String imageFilename, String imageFileRelativePath)
            throws coreException {
        LOGGER.debug("Reading image: {} from path: {}", imageFilename, imageFileRelativePath);
        InputStream imageFile = ResourcesUtil.getInstance().getResourceAsFile(imageFileRelativePath);

        try {
            BufferedImage image = ImageIO.read(imageFile);
            // Remove the image extension
            String imageKeyname = FilenameUtils.removeExtension(imageFilename);
            LOGGER.debug("Image {} read successfully", imageFilename);
            return new AbstractMap.SimpleEntry<>(imageKeyname, image);
        } catch (IOException e) {
            String errMsg = "Error while providing images params. " +
                    "It was not posible to read image '" + imageFileRelativePath + "'";
            LOGGER.error(errMsg, e);
            throw new coreException(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // For now section name is ignored, but it will be used in the future in order
    // to generate specific params for each section
    public static Map<String, Object> getJasperSectionParameters(Valoracioncompanyl valoracioncompanyl,
                                                                 ReportBlueprint reportBlueprint,
                                                                 Path reportFilesRelativePath)
            throws coreException {
        LOGGER.debug("Getting Jasper section parameters");
        Map<String, Object> sectionParams = new HashMap<>();

        sectionParams.put(JRParameter.REPORT_LOCALE, ReportResourcesConstants.REPORTS_LOCALE);
        if (valoracioncompanyl instanceof Acta acta) {
            sectionParams.put(ReportResourcesConstants.ACTA_FINIQUITO_JASPER_PARAMETER_NAME, acta);
        } else if (valoracioncompanyl instanceof Estudio estudio) {
            sectionParams.put(ReportResourcesConstants.ESTUDIO_JASPER_PARAMETER_NAME, estudio);
        }
        // TODO. Do the same for and nic12

        Map<String, Object> imageParams = getImagesParams(reportBlueprint, reportFilesRelativePath);
        sectionParams.putAll(imageParams);

        // Append actualizacion tasa financiera images params
        if (valoracioncompanyl instanceof Estudio estudio) {
            ActualizacionTasaFinanciera atf = estudio.actualizacionTasaFinanciera();
            Map<String, Object> revisionDependentImages = getRevisionDependentImagesParams(reportBlueprint, reportFilesRelativePath, atf);
            sectionParams.putAll(revisionDependentImages);
        }

        return sectionParams;
    }

    /*
Applies only to estudio model
 */
    public static Map<String, Object> getRevisionDependentImagesParams(ReportBlueprint reportBlueprint,
                                                                       Path reportFilesRelativePath,
                                                                       ActualizacionTasaFinanciera actualizacionTasaFinanciera)
            throws coreException {
        LOGGER.debug("Getting revision-dependent image parameters");

        Map<String, Object> imageParams = new HashMap<>();
        // Image params
        if (reportBlueprint.getRevisionDependentImages() != null) {
            for (String imageFilename : reportBlueprint.getRevisionDependentImages()) {
                var image = Map.ofEntries(
                        getRevisionDependentImageParam(imageFilename, reportFilesRelativePath, actualizacionTasaFinanciera));
                imageParams.putAll(image);
            }
        }
        return imageParams;
    }

    private static Map.Entry<String, Object> getRevisionDependentImageParam(String imageFilename,
                                                                            Path reportFilesRelativePath,
                                                                            ActualizacionTasaFinanciera actualizacionTasaFinanciera)
            throws coreException {
        DateFormat dateFormat = new SimpleDateFormat(PATH_ACTUALIZACION_TASA_FINANCIERA_DATE_FORMAT);
        String formattedDate = dateFormat.format(actualizacionTasaFinanciera.fecha());

        Path pathArchivosActualizacionTasaFinanciera = reportFilesRelativePath
                .resolve(ReportResourcesConstants.REPORT_REVISION_DEPENDENT_IMAGES_RESOURCES)
                .resolve(formattedDate);
        String imageFileRelativePath = pathArchivosActualizacionTasaFinanciera.resolve(imageFilename).toString();

        return getImageEntry(imageFilename, imageFileRelativePath);
    }
}
