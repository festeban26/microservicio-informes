package ec.com.company.core.estudio.model.jubilacion;

import com.fasterxml.jackson.annotation.JsonAlias;
import ec.com.company.core.estudio.model.ResultadoSalidas;
import ec.com.company.core.estudio.model.ResultadoTraspasos;

public record ResultadoJubilacion(
        String notaJustificacionPgAjustesExperiencia,
        ResultadoAplicacion resultadoAplicacion,
        @JsonAlias("actual")
        ResultadoActual resultadoActual,
        @JsonAlias("salidas")
        ResultadoSalidas resultadoSalidas,
        @JsonAlias("traspasos")
        ResultadoTraspasos resultadoTraspasos
) {

}
