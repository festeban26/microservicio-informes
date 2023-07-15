package ec.com.company.core.estudio.model;

import java.math.BigDecimal;

public record InformacionDemografica(
        BigDecimal edadPromedioActivos,
        BigDecimal ingresoMensualPromedioActivos,
        BigDecimal ingresoNomina,
        BigDecimal nominaTotalActivos,
        BigDecimal nominaTotalJubilados,
        BigDecimal pensionMensualPromedioJubilados,
        BigDecimal tfPromedioActivos,
        BigDecimal tsPromedioActivos,
        BigDecimal vidaLaboralPromedioRemanente,
        Integer trabajadoresActivos,
        Integer trabajadoresJubilados,
        Integer trabajadoresJubiladosPendientes,
        Integer trabajadoresSalidos,
        Integer trabajadoresTipo1Hombres,
        Integer trabajadoresTipo1Mujeres,
        Integer trabajadoresTipo2Hombres,
        Integer trabajadoresTipo2Mujeres,
        Integer trabajadoresTipo3Hombres,
        Integer trabajadoresTipo3Mujeres,
        Integer trabajadoresTipo4Hombres,
        Integer trabajadoresTipo4Mujeres) {
}
