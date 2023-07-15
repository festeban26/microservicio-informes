package ec.com.company.core.estudio.model;

import ec.com.company.core.estudio.model.anexos.DataAnexoContable;
import ec.com.company.core.estudio.model.anexos.DataAnexoPerdidasGananacias;
import ec.com.company.core.estudio.model.desahucio.ResultadoDesahucio;
import ec.com.company.core.estudio.model.enums.MetodologiaTasaFinancieraDescuentoEnum;
import ec.com.company.core.estudio.model.itemsTablasInforme.ItemTablaTasasDescuentoPorTfPromedio;
import ec.com.company.core.estudio.model.jubilacion.ResultadoJubilacion;
import ec.com.company.core.interfaces.Valoracioncompanyl;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.reports.util.DateUtils;
import ec.com.company.microservices.microservicioinformes.reports.util.GeneradorTablaTasasDescuento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record Estudio(

        @Valid
        Parametros parametros,
        @NotNull
        String numeroProceso,
        @NotNull
        String codigoNormativa,

        @NotNull
        String codigoEstadoEmpresa,
        @NotNull
        String nombreCliente,
        @Valid
        @NotNull
        ActualizacionTasaFinanciera actualizacionTasaFinanciera,
        @Valid
        List<TasasFinancierasDescuento> tasasFinancierasDescuento,

        @Valid
        List<ItemsTablaTasasDescuentoPorDuracionDelPlan> tasasFinancierasDescuentoPorDuracion,
        String variableReferenciaTasaFinanciera,
        @Valid
        @NotNull
        Destinatario destinatario,

        @NotNull
        String nombrePortada,

        @NotNull
        String nombreAlternoEncabezado,
        @NotNull
        String nombreAnexos,
        @NotNull
        String politicaPagoJubilacion,
        @NotNull
        String reconocimientoSalidas,
        String nombreNorma,
        String nombreSeccion,
        String descripcionTasaDescuento,
        @NotNull
        String tipoTraspaso,

        @NotNull
        Boolean esCalculoDesahucioPorFlujos,
        @NotNull
        Boolean usaTasaRotacionPorCentroDeCosto,
        @NotNull
        Boolean agruparPorCentroDeCosto,
        // Remove enum from name
        @NotNull
        MetodologiaTasaFinancieraDescuentoEnum metodologiaTasaFinancieraDescuento,
        @Valid
        @NotNull
        Hipotesiscompanyl hipotesiscompanyl,
        @Valid
        Hipotesiscompanyl hipotesiscompanylAnterior,
        @Valid
        @NotNull
        InformacionDemografica informacionDemografica,
        @Valid
        InformacionDemografica informacionDemograficaAnterior,
        ResultadoJubilacion resultadoJubilacion,
        ResultadoDesahucio resultadoDesahucio,
        Estudio estudioAnterior,
        Double proporcionCorte,
        DataAnexoPerdidasGananacias dataAnexoPerdidasGananaciasJubilacion,
        DataAnexoPerdidasGananacias dataAnexoPerdidasGananaciasDesahucio,
        DataAnexoContable dataAnexoContableJubilacion,
        DataAnexoContable dataAnexoContableDesahucio

) implements Valoracioncompanyl {

    public Date fechaValoracion() {
        return parametros.getFechaCalculo();
    }

    public String periodoUtilizacion() {
        return DateUtils.getPeriodoUtilizacionString(parametros.getFechaEstimacion(), parametros.getFechaCalculo());
    }

    public String periodoUtilizacionProyectado() {
        return DateUtils.getPeriodoUtilizacionString(parametros.getFechaCalculo(), parametros.getFechaProyectada());
    }

    public List<ItemTablaTasasDescuentoPorTfPromedio> itemsTablaTasasDescuentoPorTfPromedio() {
        return GeneradorTablaTasasDescuento.getItemsPorTfPromedio(this.tasasFinancierasDescuento);
    }

    public List<ItemsTablaTasasDescuentoPorDuracionDelPlan> itemsTablaTasasDescuentoPorDuracionDelPlanFirstHalf()
            throws coreException {
        return GeneradorTablaTasasDescuento.getItemsPorDuracionDelPlanFirstHalf(this.tasasFinancierasDescuentoPorDuracion);

    }

    public List<ItemsTablaTasasDescuentoPorDuracionDelPlan> itemsTablaTasasDescuentoPorDuracionDelPlanSecondHalf()
            throws coreException {
        return GeneradorTablaTasasDescuento.getItemsPorDuracionDelPlanSecondtHalf(this.tasasFinancierasDescuentoPorDuracion);

    }

    @Override
    public String getNumeroProceso() {
        return numeroProceso;
    }
}
