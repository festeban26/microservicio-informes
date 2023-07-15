package ec.com.company.core.estudio.model;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record Hipotesiscompanyl(
        // TODO EMFA, test if these are really not null. If annotation is working.
        @NotNull
        BigDecimal tasaFinancieraDescuento,
        @NotNull
        BigDecimal tasaIncrementoSalarios,
        @NotNull
        BigDecimal porcentajeIncrementoSalarialEstimado,
        @NotNull
        BigDecimal tasaRotacionPromedio,
        @NotNull
        BigDecimal tasaPasivaReferencialBce,

        BigDecimal pensionMinima,
        BigDecimal porcentajeVariacionSensibilidad,
        BigDecimal porcentajeVariacionSensibilidadRotacion,
        BigDecimal decimoCuartoSueldo,
        BigDecimal vidaLaboralRemanente) {
}
