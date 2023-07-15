package ec.com.company.core.estudio.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PersonaComposicionDemografica(
        String identificacion,
        BigDecimal costoLaboral,
        BigDecimal costoLaboralAnterior,
        BigDecimal edad,
        BigDecimal interesNeto,
        BigDecimal interesNetoAnterior,
        BigDecimal obd,
        BigDecimal obdAnterior,
        BigDecimal obdTraspaso,
        BigDecimal pasivoNeto,
        BigDecimal remuneracionPromedio,
        BigDecimal tf,
        BigDecimal ts,
        BigDecimal tw,
        BigDecimal valorNominal,
        Integer tipoCalculado,
        String centroDeCosto,
        String nombreCompleto,
        String sexo
) {

    /*
    To match
    <queryString>
			<![CDATA[]]>
	</queryString>
	<field name="persona" class="ec.com.company.core.estudio.model.PersonaComposicionDemografica"/>

	On iReport
     */
    public PersonaComposicionDemografica getPersona() {
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
