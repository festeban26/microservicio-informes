package ec.com.company.core.estudio.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TasasFinancierasDescuento(@NotNull
                                        BigDecimal tasa,
                                        @NotNull
                                        Integer tfDesde,
                                        @NotNull
                                        Integer tfHasta) {

}
