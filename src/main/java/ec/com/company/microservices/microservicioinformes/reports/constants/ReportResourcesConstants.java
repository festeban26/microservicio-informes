package ec.com.company.microservices.microservicioinformes.reports.constants;

import java.time.LocalDate;
import java.util.Locale;

public class ReportResourcesConstants {

    public static final String REPORTS_PATHNAME = "reports";

    public static final Locale REPORTS_LOCALE = new Locale("es", "US");

    public static final String REPORT_IMAGES_RESOURCES = "images";
    // Imagenes dependientes de actualizaciones como tasa de financiera, etc.
    public static final String REPORT_REVISION_DEPENDENT_IMAGES_RESOURCES = "revisionDependentImages";

    public static final String REPORTS_BLUEPRINT_FILENAME = "blueprint.yml";
    public static final String ESTUDIO_company_REPORT_PATHNAME = "estudiocompanyl";
    public static final String NIC12_REPORT_PATHNAME = "nic12";
    public static final String ACTA_FINIQUITO_REPORT_PATHNAME = "actasFiniquito";

    public static final LocalDate ACTA_FINIQUITO_REPORTS_FECHA_VALORACION_PATHNAME
            = LocalDate.of(2022, 12, 31);

    public static final String ESTUDIO_JASPER_PARAMETER_NAME = "estudio";
    public static final String ESTUDIO_NIC12_JASPER_PARAMETER_NAME = "estudioNic12";
    public static final String ACTA_FINIQUITO_JASPER_PARAMETER_NAME = "acta";

    public static final String REPORT_TYPE_BONOS_EEUU_NAME = "BONOS_EEUU";
    public static final String REPORT_TYPE_BONOS_LOCALES_NAME = "BONOS_LOCALES";
    public static final String REPORT_TYPE_PYME_NAME = "PYME";
    public static final String REPORT_TYPE_NEC_NAME = "NEC";
    public static final String REPORT_TYPE_NIC12_NAME = "NIC12";
    public static final String REPORT_TYPE_ACTA_FINIQUITO_NAME = "ACTA_FINIQUITO";
    public static final String REPORT_TYPE_ACTAS_RESUMEN_EXPLICATIVO_NAME = "RESUMEN_EXPLICATIVO";

    public static final String BLUEPRINT_HOJA_EN_BLANCO_SECTION_FILENAME = "hojaBlanco.jasper";
    public static final String BLUEPRINT_TEXT_HOJA_EN_BLANCO = "hojaBlanco.jasper";

    // ============================= Blueprint.yml Constants =============================================
    public static final String BLUEPRINT_YML_SECCIONES = "secciones";

    public static final String PDF_EXTENSION = ".pdf";
    public static final String COMPILED_JASPER_REPORT_EXTENSION = ".jasper";

    public static final String ESTUDIOS_companyLES_PDF_DEFAULT_FILENAME = "estudio";
    public static final String ESTUDIOS_companyLES_EXCEL_DEFAULT_FILENAME = "anexo";
    public static final String ACTAS_DEFAULT_FILENAME = "acta";
    public static final String ACTAS_ANEXO_DEFAULT_FILENAME = "anexo-acta";

    public static final String ESTUDIOS_companyLES_TEXTO_ANEXOS = "anexo";

    public static final String ESTUDIOS_companyLES_PARAMETRO_NUMERO_ANEXO = "numeroAnexo";

}
