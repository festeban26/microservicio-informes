package ec.com.company.core.estudio.model.desahucio;

import ec.com.company.core.estudio.model.PersonaComposicionDemografica;

import java.math.BigDecimal;
import java.util.List;

public record ResultadoActual(
        BigDecimal valorNominal,
        List<PersonaComposicionDemografica> empleados) {
}
