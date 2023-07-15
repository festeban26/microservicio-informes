package ec.com.company.core.estudio.model.jubilacion;

import ec.com.company.core.estudio.model.PersonaComposicionDemografica;

import java.util.List;

public record ResultadoActual(
        List<PersonaComposicionDemografica> personas) {

    // TODO
    public Boolean requiereNotaJubilacionEnAnexo() {
        return true;
    }

    /* TODO when real data on resultadoActual
    public Boolean validarRequiereNotaJubilacionAnexo(Estudio estudio) {
        return estudio.getPersonaEstudioList().stream().anyMatch(pe -> {
            BigDecimal ts = pe.getTsJubilacion();
            return (ts.compareTo(BigDecimal.valueOf(20.0)) > 0);
        });
    }*/
}
