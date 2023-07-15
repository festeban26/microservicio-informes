package ec.com.company.core.estudio.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum MetodologiaTasaFinancieraDescuentoEnum {

    //@JsonProperty("D")
    CALCULO_DURACION_PLAN("D", "Cálculo con Duración del Plan"),
    //@JsonProperty("T")
    CALCULO_TF("T", "Cálculo con TF Promedio");

    private final String code;
    private final String description;

    MetodologiaTasaFinancieraDescuentoEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // Serialization
    @JsonValue
    public String getCode() {
        return code;
    }

    // Deserialization
    @JsonCreator
    public static MetodologiaTasaFinancieraDescuentoEnum forValue(String code) {
        return Arrays.stream(MetodologiaTasaFinancieraDescuentoEnum.values())
                .filter(op -> op.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(); // depending on requirements: can be .orElse(null);
    }

}
