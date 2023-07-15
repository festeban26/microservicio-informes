package ec.com.company.core.estudio.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PersonaAnexoPerdidasGanancia(
        String identificacion,
        String clase,
        Integer tipoCalculado,
        String nombreCompletoPersona,
        String sexoPersona,
        BigDecimal remuneracion,
        BigDecimal edad,
        BigDecimal ts,
        BigDecimal tf,
        BigDecimal tw,
        BigDecimal costoLaboralAnterior,
        BigDecimal interesNetoAnterior,
        BigDecimal obdAnterior,
        BigDecimal obdActual,
        BigDecimal obdTraspaso,
        String centroDeCosto
) {
    // To match <field name="persona".. on JasperReport
    public PersonaAnexoPerdidasGanancia getPersona() {
        return this;
    }

    /* This method prevents the jasper to print nulls*/
    public BigDecimal getObdTraspaso() {
        return Objects.requireNonNullElse(this.obdTraspaso, BigDecimal.ZERO);
    }

    public BigDecimal getCostoLaboralAnterior() {
        return Objects.requireNonNullElse(this.costoLaboralAnterior, BigDecimal.ZERO);
    }

    public BigDecimal getInteresNetoAnterior() {
        return Objects.requireNonNullElse(this.interesNetoAnterior, BigDecimal.ZERO);
    }

    public BigDecimal getObdAnterior() {
        return Objects.requireNonNullElse(this.obdAnterior, BigDecimal.ZERO);
    }
}
