package ec.com.company.core.estudio.model.anexos;

import java.math.BigDecimal;
import java.util.List;

public record MayorContable(String descripcion,
                            List<LineaMayorContable> items,
                            BigDecimal totalDebe,
                            BigDecimal totalHaber,
                            BigDecimal saldoDebe,
                            BigDecimal saldoHaber) {
}
