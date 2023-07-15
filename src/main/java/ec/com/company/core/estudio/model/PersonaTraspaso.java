package ec.com.company.core.estudio.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

public record PersonaTraspaso(
        String identificacion,
        BigDecimal costoLaboral,
        BigDecimal edad,
        @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Guayaquil")
        Date fechaTraspaso,
        BigDecimal interesNeto,
        BigDecimal obd,
        BigDecimal obdAcumulado,
        BigDecimal ts,
        Integer tipo,
        String nombreCompleto,
        String nombreClienteOrigen,
        String nombreClienteDestino,
        String sexo
) {
    public PersonaTraspaso getPersona() {
        return this;
    }
}
