package ec.com.company.core.estudio.model.anexos;

import ec.com.company.core.estudio.model.PersonaAnexoPerdidasGanancia;

import java.math.BigDecimal;
import java.util.List;

public record DataAnexoPerdidasGananacias(BigDecimal variacionTotal,
                                          BigDecimal reduccionesLiquidacionesAnticipadas,
                                          BigDecimal transferencias,
                                          BigDecimal beneficiosPagados,
                                          List<PersonaAnexoPerdidasGanancia> personas) {

    public BigDecimal getTotal() {
        return this.variacionTotal.add(
                this.reduccionesLiquidacionesAnticipadas.abs()).add(
                this.beneficiosPagados.abs()).add(this.transferencias);
    }
}
