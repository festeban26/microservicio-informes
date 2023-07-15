package ec.com.company.core.estudio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.company.microservices.microservicioinformes.constants.AppConstants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Parametros {

    @NotNull
    @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
    Date fechaCalculo;

    @NotNull
    @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
    Date fechaEstimacion;

    @NotNull
    @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
    @JsonAlias("fechaProyeccion")
    Date fechaProyectada;

    @NotNull
    @JsonFormat(pattern = AppConstants.DATE_PATTERN, timezone = "America/Guayaquil")
    Date fechaDemografica;

    @NotNull
    String versionCalculoFactorProbabilidadJubilacion;

    public Date getFechaValoracion(){
        return this.fechaCalculo;
    }

}
