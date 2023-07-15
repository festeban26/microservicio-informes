package ec.com.company.core.estudio.model.anexos;

import java.math.BigDecimal;

public record AsientoContable(AsientosContablesEnum detalleEnum,
                              String descripcion,
                              String nota,
                              String descripcionDebe,
                              BigDecimal valorDebe,
                              String descripcionHaber,
                              BigDecimal valorHaber,
                              String numeracion) {
    // To match <field name="asiento".. on JasperReport
    public AsientoContable getAsiento() {
        return this;
    }
}
