package ec.com.company;

import ec.com.company.core.actas.model.Acta;
import ec.com.company.core.estudio.model.Estudio;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.models.MicroserviceResponse;
import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;
import ec.com.company.microservices.microservicioinformes.services.ActaAnnexService;
import ec.com.company.microservices.microservicioinformes.services.ActaReportService;
import ec.com.company.microservices.microservicioinformes.services.AnexosEstudioService;
import ec.com.company.microservices.microservicioinformes.services.EstudioReportService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.time.Duration;

/**
 * An application based on CRUD operations on a Report (acta, estudio, nic12).
 * Create (HTTP POST), Read (GET), Update (PUT), Delete (DELETE).
 * <p>
 * Swagger REST API documentation can be found on:
 * <a href="http://localhost:8083/swagger-ui/index.html">...</a>
 *
 * @author festeban26 & CHATGPT 4.0 XD
 */
@SpringBootApplication
public class MicroservicioInformesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioInformesApplication.class, args);
    }

    @RestController
    @RequestMapping("/") // The base path for all the endpoints in this controller
    public static class ReportController {

        private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

        @Autowired
        private EstudioReportService estudioReportService;

        @Autowired
        private AnexosEstudioService anexosEstudioService;
        @Autowired
        private ActaReportService actaReportService;
        @Autowired
        private ActaAnnexService actaAnnexService;

        /**
         * Retrieves the URL of the estudio companyl report.
         *
         * @param numeroProceso  The process number associated with the estudio companyl.
         * @param versionInforme The version of the document.
         * @param filename       (Optional) The filename of the report. If specified, it will be used to
         *                       retrieve the specific file with the provided filename. If no filename is
         *                       provided, the default file associated with the URL will be retrieved.
         * @param request        The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the estudio companyl report.
         */
        @CrossOrigin
        @GetMapping("/v1/estudio-companyl/numero-proceso/{numeroProceso}" +
                "/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> getEstudiocompanylReportUrl(
                @PathVariable String numeroProceso,
                @PathVariable String versionInforme,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received GET request to retrieve an estudio companyl report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "versionInforme: {}",
                    request.getRequestURI(), numeroProceso, versionInforme);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = estudioReportService.getReportUrl(numeroProceso, versionInforme, filename);
                return generateSuccessfulGetResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        /**
         * Creates the report of the estudio companyl and returns the URL.
         *
         * @param numeroProceso  The process number associated with the estudio companyl.
         * @param versionInforme The version of the document.
         * @param estudio        The estudio companyl object.
         * @param filename       (Optional) The filename of the report. If specified, it will be used as
         *                       the filename of the file returned in the URL. If no filename is provided,
         *                       a default filename will be assigned to the file accessed through the URL.
         * @param request        The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the created report.
         */
        @CrossOrigin
        @PostMapping("/v1/estudio-companyl/numero-proceso/{numeroProceso}" +
                "/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> createEstudiocompanylReport(
                @PathVariable String numeroProceso,
                @PathVariable String versionInforme,
                @RequestBody Estudio estudio,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received POST request to create an estudio companyl report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "versionInforme: {}",
                    request.getRequestURI(), numeroProceso, versionInforme);
            LOGGER.debug("Estudio: {}", estudio);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = estudioReportService.createReport(estudio, numeroProceso,
                        versionInforme, filename);
                return generateSuccessfulPostResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        @CrossOrigin
        // Specify the POST HTTP method for the given URI
        @PostMapping("/v1/estudio-companyl/numero-proceso/{numeroProceso}" +
                "/anexo/{anexoEstudio}/version-documento/{versionInforme}/{fileExtension}")
        public ResponseEntity<MicroserviceResponse<?>> createEstudiocompanylAnnex(
                @PathVariable String numeroProceso,  // Process number from URI
                @PathVariable String anexoEstudio,  // Study appendix from URI
                @PathVariable String versionInforme,  // Document version from URI
                @RequestBody Estudio estudio,  // Study from request body
                @RequestParam(required = false) String filename,  // Optional filename from query parameters
                @PathVariable String fileExtension,  // File extension from URI
                HttpServletRequest request) {  // HTTP servlet request
            LOGGER.info("Received POST request to create an estudio companyl annex report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "versionInforme: {}, anexoEstudio: {}, " +
                            "fileExtension: {}",
                    request.getRequestURI(), numeroProceso, versionInforme, anexoEstudio, fileExtension);
            LOGGER.debug("Estudio: {}", estudio);
            final long startTime = System.nanoTime();

            try (var ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = anexosEstudioService.createReport(estudio, anexoEstudio, numeroProceso,
                        versionInforme, filename, fileExtension);
                return generateSuccessfulPostResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }


        /**
         * Retrieves the URL for the report of the acta finiquito.
         *
         * @param numeroProceso         The process number associated with the acta finiquito.
         * @param identificacionPersona The identification of the person related to the acta finiquito.
         * @param versionInforme        The version of the document.
         * @param filename              (Optional) The filename of the annex report. If specified, it will be used as
         *                              the filename of the file returned in the URL. If no filename is provided,
         *                              a default filename will be assigned to the file accessed through the URL.
         * @param request               The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the report.
         */
        @CrossOrigin
        @GetMapping("/v1/acta-finiquito/numero-proceso/{numeroProceso}" +
                "/identificacion-persona/{identificacionPersona}/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> getActaFiniquitoMainReportUrl(
                @PathVariable String numeroProceso,
                @PathVariable String identificacionPersona,
                @PathVariable String versionInforme,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received GET request to retrieve an acta finiquito report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "identificacionPersona: {}, versionInforme: {}",
                    request.getRequestURI(), numeroProceso, identificacionPersona, versionInforme);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = actaReportService.getReportUrl(numeroProceso, identificacionPersona,
                        versionInforme, filename);
                return generateSuccessfulPostResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        /**
         * Creates the report of the acta finiquito and returns the URL.
         *
         * @param numeroProceso         The process number associated with the acta finiquito.
         * @param identificacionPersona The identification of the person related to the acta finiquito.
         * @param versionInforme        The version of the document.
         * @param actaFiniquito         The acta finiquito object.
         * @param filename              (Optional) The filename of the report. If specified, it will be used as
         *                              the filename of the file returned in the URL. If no filename is provided,
         *                              a default filename will be assigned to the file accessed through the URL.
         * @param request               The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the created report.
         */
        @CrossOrigin
        @PostMapping("/v1/acta-finiquito/numero-proceso/{numeroProceso}" +
                "/identificacion-persona/{identificacionPersona}/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> createActaFiniquitoMainReport(
                @PathVariable String numeroProceso,
                @PathVariable String identificacionPersona,
                @PathVariable String versionInforme,
                @RequestBody Acta actaFiniquito,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received POST request to create an acta finiquito report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "identificacionPersona: {}, versionInforme: {}",
                    request.getRequestURI(), numeroProceso, identificacionPersona, versionInforme);
            LOGGER.debug("Acta: {}", actaFiniquito);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = actaReportService.createReport(actaFiniquito, numeroProceso,
                        identificacionPersona, versionInforme, filename);
                return generateSuccessfulPostResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        /**
         * Retrieves the URL for the annex report of the acta finiquito.
         *
         * @param numeroProceso         The process number associated with the acta finiquito.
         * @param identificacionPersona The identification of the person related to the acta finiquito.
         * @param versionInforme        The version of the document.
         * @param filename              (Optional) The filename of the annex report. If specified, it will be used as
         *                              the filename of the file returned in the URL. If no filename is provided,
         *                              a default filename will be assigned to the file accessed through the URL.
         * @param request               The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the annex report.
         */
        @CrossOrigin
        @GetMapping("/v1/acta-finiquito/anexo-resumen-explicativo/numero-proceso/{numeroProceso}" +
                "/identificacion-persona/{identificacionPersona}/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> getActaFiniquitoAnnexReportUrl(
                @PathVariable String numeroProceso,
                @PathVariable String identificacionPersona,
                @PathVariable String versionInforme,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received GET request to retrieve an annex acta finiquito report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "identificacionPersona: {}, versionInforme: {}",
                    request.getRequestURI(), numeroProceso, identificacionPersona, versionInforme);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = actaAnnexService.getAnnexUrl(numeroProceso, identificacionPersona, versionInforme, filename);
                return generateSuccessfulGetResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        /**
         * Creates the annex report of the acta finiquito and returns the URL.
         *
         * @param numeroProceso         The process number associated with the acta finiquito.
         * @param identificacionPersona The identification of the person related to the acta finiquito.
         * @param versionInforme        The version of the document.
         * @param actaFiniquito         The acta finiquito object.
         * @param filename              (Optional) The filename of the annex report. If specified, it will be used as
         *                              the filename of the file returned in the URL. If no filename is provided,
         *                              a default filename will be assigned to the file accessed through the URL.
         * @param request               The HttpServletRequest object containing the request details.
         * @return A ResponseEntity containing a MicroserviceResponse with the URL of the created annex report.
         */
        @CrossOrigin
        @PostMapping("/v1/acta-finiquito/anexo-resumen-explicativo/numero-proceso/{numeroProceso}" +
                "/identificacion-persona/{identificacionPersona}/version-documento/{versionInforme}")
        public ResponseEntity<MicroserviceResponse<?>> createActaFiniquitoAnnexReport(
                @PathVariable String numeroProceso,
                @PathVariable String identificacionPersona,
                @PathVariable String versionInforme,
                @RequestBody Acta actaFiniquito,
                @RequestParam(required = false) String filename,
                HttpServletRequest request) {
            LOGGER.info("Received POST request to create an annex acta finiquito report. " +
                            "Request details - URI: {}, numeroProceso: {}, " +
                            "identificacionPersona: {}, versionInforme: {}",
                    request.getRequestURI(), numeroProceso, identificacionPersona, versionInforme);
            LOGGER.debug("Acta: {}", actaFiniquito);
            final long startTime = System.nanoTime();

            try (MDC.MDCCloseable ignored = MDC.putCloseable("numeroProceso", numeroProceso)) {
                URL reportUrl = actaAnnexService.createAnnex(actaFiniquito, numeroProceso,
                        identificacionPersona, versionInforme, filename);
                return generateSuccessfulPostResponse(reportUrl, startTime);
            } catch (coreException e) {
                return handlecoreException(e, startTime);
            }
        }

        private static void logOperationTime(long startTime) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            LOGGER.info("Operation completed in {} milliseconds.", elapsedTime);
        }

        private static ResponseEntity<MicroserviceResponse<?>> handlecoreException(
                coreException e, long startTime) {
            MicroserviceResponse<String> errorResponse = createErrorResponse(e);
            logOperationTime(startTime);
            LOGGER.error("coreException occurred: {}", e.getMessage());
            return ResponseEntity.status(e.getErrorCode()).body(errorResponse);
        }

        private ResponseEntity<MicroserviceResponse<?>> generateSuccessfulGetResponse(
                URL reportUrl, long startTime) {
            MicroserviceResponse<URL> response = createSuccessfulResponse(reportUrl);
            logOperationTime(startTime);
            LOGGER.info("Successful GET response generated.");
            return ResponseEntity.ok(response);
        }

        private ResponseEntity<MicroserviceResponse<?>> generateSuccessfulPostResponse(
                URL reportUrl, long startTime) {
            MicroserviceResponse<URL> response = createSuccessfulResponse(reportUrl);
            logOperationTime(startTime);
            LOGGER.info("Successful POST response generated.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        private static MicroserviceResponse<URL> createSuccessfulResponse(URL url) {
            int statusCode = 0; // Blame coworker for returning 0 instead of HttpStatus.OK.value() XD
            String statusMessage = HttpStatus.OK.getReasonPhrase();
            LOGGER.info("Sending the response back with status code: {}", statusCode);
            return MicroserviceResponse.<URL>builder()
                    .status(statusCode)
                    .message(statusMessage)
                    .content(url)
                    .build();
        }

        private static MicroserviceResponse<String> createErrorResponse(coreException coreException) {
            int statusCode = coreException.getErrorCode().value();
            String statusMessage = coreException.getErrorCode().getReasonPhrase();
            String errorMessage = coreException.getMessage();
            LOGGER.error("Sending the response back with status code: {}", statusCode);
            return MicroserviceResponse.<String>builder()
                    .status(statusCode)
                    .message(statusMessage)
                    .content(errorMessage)
                    .build();
        }

    /*
        // Create a method to handle DELETE /reports/{id} requests
        @DeleteMapping("/reports/{id}")
        public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
            // Find the report by id using reportRepository
            Optional<Report> report = reportRepository.findById(id);
            // If the report exists, delete it using reportRepository and return status code 204
            if (report.isPresent()) {
                reportRepository.delete(report.get());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            // Otherwise, return an empty body with status code 404
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }*/
    }
}
