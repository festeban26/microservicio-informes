package ec.com.company.core.estudio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.company.microservices.microservicioinformes.constants.AppConstants;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ActualizacionTasaFinanciera (
        @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
        @NotNull
        Date fecha,
        @NotNull
        String textoInformeVersion,
        @NotNull
        String textoInformeFinPeriodoInflacion,
        @NotNull
        String textoInformeNumeroObligaciones){
}
