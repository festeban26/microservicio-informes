package ec.com.company.core.estudio.model.jubilacion;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

public record ResultadoAplicacion(

        BigDecimal beneficiosEsperadosPorPagar,
        BigDecimal beneficiosPagados,
        BigDecimal contribucionesEmpleador,
        BigDecimal contribucionesParticipesPlan,
        BigDecimal costoLaboral,
        BigDecimal costoLaboralProyectado,
        BigDecimal costoNeto,
        BigDecimal costoNetoProyectado,
        BigDecimal costoServiciosPasados,
        BigDecimal costoTrabajadoresActivosTsMayor10,
        BigDecimal costoTrabajadoresActivosTsMenor10,
        BigDecimal efectoNetoOri,
        BigDecimal efectoReduccionesLiquidaciones,
        BigDecimal impactoObdIncrementoSalarialMas,
        BigDecimal impactoObdIncrementoSalarialMenos,
        BigDecimal impactoObdRotacionMas,
        BigDecimal impactoObdRotacionMenos,
        BigDecimal impactoObdTasaDescuentoMas,
        BigDecimal impactoObdTasaDescuentoMenos,
        BigDecimal interesNeto,
        BigDecimal interesNetoProyectado,
        @JsonAlias("obdActual")
        BigDecimal obd,
        BigDecimal obdAnterior,
        BigDecimal pasivoReserva,
        BigDecimal pasivoReservaAnterior,
        BigDecimal pasivoReservaProyectado,
        BigDecimal pgcompanylAjustesExperiencia,
        BigDecimal pgcompanylCambiosSupuestosFinancieros,
        BigDecimal pgcompanylesOriActual,
        BigDecimal servicioPasadoTotalLiquidaciones,
        BigDecimal servicioPasadoTotalLiquidacionesProyectado,
        BigDecimal transferenciasEmpleados,
        BigDecimal valorMercadoActivosPlanFinActual,
        BigDecimal valorMercadoActivosPlanFinAnterior,
        BigDecimal valorMercadoActivosPlanInicio,
        BigDecimal variacionObdIncrementoSalarialMas,
        BigDecimal variacionObdIncrementoSalarialMenos,
        BigDecimal variacionObdRotacionMas,
        BigDecimal variacionObdRotacionMenos,
        BigDecimal variacionObdTasaDescuentoMas,
        BigDecimal variacionObdTasaDescuentoMenos,
        BigDecimal variacionReservasNoRegularizadas,
        BigDecimal variacionReservasNoRegularizadasAnterior
) {
    public String getObdAnteriorTexto() {
        DecimalFormat decimalFormat = new DecimalFormat("US$ ###,###,##0;(US$ ###,###,##0)");
        String obdAnteriorTexto = decimalFormat.format(this.obdAnterior);
        return obdAnteriorTexto;
    }

    // Caution if decided to delete, nulls may be generated on the report.
    public BigDecimal getObdAnterior() {
        return Objects.requireNonNullElse(this.obdAnterior, BigDecimal.ZERO);
    }

}
