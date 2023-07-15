package ec.com.company.core.estudio.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ItemsTablaTasasDescuentoPorDuracionDelPlan(
        @NotNull
        BigDecimal duracion,
        @NotNull
        BigDecimal tasa) {

    public String getDuracion() {
        // Always print duracion with one decimal place
        String formatted = duracion.setScale(1, RoundingMode.HALF_UP).toPlainString();
        return formatted + " años";
    }

    public BigDecimal getTasa() {
        // Since values are expected to be in decimal format, multiply by 100 to get percentage
        return tasa.multiply(BigDecimal.valueOf(100));
    }
}
