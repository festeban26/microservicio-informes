package ec.com.company.core.estudio.model.itemsTablasInforme;

import java.math.BigDecimal;

public record ItemTablaTasasDescuentoPorTfPromedio(String duracion,
                                                   BigDecimal tasa) {

    public String getDuracion() {
        return duracion;
    }

    public BigDecimal getTasa() {
        return tasa;
    }
}
