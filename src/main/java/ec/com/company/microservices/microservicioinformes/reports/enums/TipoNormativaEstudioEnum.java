package ec.com.company.microservices.microservicioinformes.reports.enums;

import ec.com.company.microservices.microservicioinformes.exception.coreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Enumeración que representa los diferentes tipos de normativas.
 */
public enum TipoNormativaEstudioEnum {
    BONOS_EEUU("CE"), // Representa los bonos de EEUU
    BONOS_LOCALES("CL"), // Representa los bonos locales
    PYME("PY"), // Representa las PYMES
    NEC("NC"); //  Representa las NEC
    private static final Logger LOGGER = LoggerFactory.getLogger(TipoNormativaEstudioEnum.class);
    private static final Map<String, TipoNormativaEstudioEnum> CODE_MAP = new HashMap<>();

    static {
        // Se inicializa el mapa de códigos con los valores de los enumeradores
        for (TipoNormativaEstudioEnum tipoNormativaEstudioEnum : values()) {
            CODE_MAP.put(tipoNormativaEstudioEnum.getCode().toUpperCase(), tipoNormativaEstudioEnum);
        }
    }

    public final String code;

    /**
     * Constructor de la enumeración TipoNormativaEstudioEnum.
     *
     * @param code El código asociado al tipo de normativa.
     */
    TipoNormativaEstudioEnum(String code) {
        this.code = code;
    }

    /**
     * Obtiene el código asociado al tipo de normativa.
     *
     * @return El código asociado al tipo de normativa.
     */
    private String getCode() {
        return code;
    }

    /**
     * Obtiene el valor TipoNormativaEstudioEnum correspondiente al tipo de normativa proporcionado.
     *
     * @param tipoNormativa El tipo de normativa.
     * @return El valor TipoNormativaEstudioEnum correspondiente.
     * @throws IllegalArgumentException Si se proporciona un tipo de normativa inválido, se lanza esta excepción.
     */
    public static TipoNormativaEstudioEnum getValueOf(String tipoNormativa)
            throws coreException {
        LOGGER.debug("Attempting to get TipoNormativaEstudioEnum for tipoNormativa: {}", tipoNormativa);
        TipoNormativaEstudioEnum tipoNormativaEnum = CODE_MAP.get(tipoNormativa.toUpperCase());
        if (tipoNormativaEnum == null) {
            String errMsg = "Invalid tipoNormativa value: " + tipoNormativa + ". Valid values are: " + Arrays.toString(values());
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.BAD_REQUEST);
        }
        LOGGER.debug("Retrieved TipoNormativaEstudioEnum: {}", tipoNormativaEnum);
        return tipoNormativaEnum;
    }
}
