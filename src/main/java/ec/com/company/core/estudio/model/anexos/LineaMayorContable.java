package ec.com.company.core.estudio.model.anexos;

import java.math.BigDecimal;

public record LineaMayorContable(
        BigDecimal debe,
        BigDecimal haber,
        String referencia) {

    // To match <field name="asiento".. on JasperReport
    public LineaMayorContable getItemMayor() {
        return this;
    }
}
