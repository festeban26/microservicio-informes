package ec.com.company.core.estudio.model;

import java.math.BigDecimal;

public record PersonaSalida(
        Integer id,
        String identificacion,
        BigDecimal costoLaboral,
        BigDecimal edad,
        BigDecimal interesNeto,
        BigDecimal obd,
        BigDecimal ts,
        Integer tipo,
        String nombreCompleto
) {
    public PersonaSalida getPersona() {
        return this;
    }
}

