package ec.com.company.microservices.microservicioinformes.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import ec.com.company.microservices.microservicioinformes.reports.constants.ReportResourcesConstants;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class ReportBlueprint {
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_BONOS_EEUU_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> bonosEeuu;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_BONOS_LOCALES_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> bonosLocales;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_PYME_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> pyme;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_NEC_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> nec;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_NIC12_NAME)
    @Getter
    @Setter
    private Map<String, Object> nic12;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_ACTA_FINIQUITO_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> actaFiniquito;
    @JsonProperty(ReportResourcesConstants.REPORT_TYPE_ACTAS_RESUMEN_EXPLICATIVO_NAME)
    @Getter
    @Setter
    private Map<String, Map<String, Object>> actaResumenExplicativo;
    @JsonProperty(ReportResourcesConstants.REPORT_IMAGES_RESOURCES)
    @Getter
    @Setter
    private List<String> imagenes;
    @JsonProperty(ReportResourcesConstants.REPORT_REVISION_DEPENDENT_IMAGES_RESOURCES)
    @Getter
    @Setter
    private List<String> revisionDependentImages;
    @Getter
    @Setter
    private Map<String, String> subReportes;
    @Getter
    @Setter
    private Map<String, Map<String, String>> reportes;
}
