package ec.com.company.microservices.microservicioinformes.reports.enums;

import java.util.Arrays;

public enum AnexoEstudioEnum {

    JUBILACION_ANEXO_RESUMEN_APLICACION
            ("moduloJubilacionPatronalAnexoResumenAplicacion.jasper", 1),
    JUBILACION_ANEXO_COMPOSICION_DEMOGRAFICA
            ("moduloJubilacionPatronalAnexoComposicionDemografica.jasper", 2),
    JUBILACION_ANEXO_COMPOSICION_DEMOGRAFICA_NEC
            ("moduloJubilacionPatronalAnexoComposicionDemograficaNec.jasper", 2),
    // TODO EMFA fix bug on numero de anexos
    JUBILACION_ANEXO_SALIDAS
            ("moduloJubilacionPatronalAnexoSalidas.jasper", 3),
    JUBILACION_ANEXO_SALIDAS_NEC
            ("moduloJubilacionPatronalAnexoSalidasNec.jasper",3),
    ANEXO_TRASPASOS_JUBILACION
            ("anexoTraspasosJubilacion.jasper", 3),
    DESAHUCIO_ANEXO_RESUMEN_APLICACION
            ("moduloBonificacionDesahucioAnexoResumenAplicacion.jasper", 1),
    DESAHUCIO_ANEXO_COMPOSICION_DEMOGRAFICA
            ("moduloBonificacionDesahucioAnexoComposicionDemografica.jasper", 2),
    DESAHUCIO_ANEXO_COMPOSICION_DEMOGRAFICA_NEC
            ("moduloBonificacionDesahucioAnexoComposicionDemograficaNec.jasper",2),
    DESAHUCIO_ANEXO_SALIDAS
            ("moduloBonificacionDesahucioAnexoSalidas.jasper",3),
    DESAHUCIO_ANEXO_SALIDAS_NEC
            ("moduloBonificacionDesahucioAnexoSalidasNec.jasper",3),
    ANEXO_TRASPASOS_DESAHUCIO
            ("anexoTraspasosDesahucio.jasper", 3),
    ANEXO_CONTABLE_JUBILACION
            ("anexoContableJubilacion.jasper", 0),
    ANEXO_CONTABLE_DESAHUCIO
            ("anexoContableDesahucio.jasper", 0),
    ANEXO_PERDIDAS_GANANCIAS_JUBILACION
            ("anexoPerdidasGananciasJubilacion.jasper", 0),
    ANEXO_PERDIDAS_GANANCIAS_DESAHUCIO
            ("anexoPerdidasGananciasDesahucio.jasper", 0);


    private final String nombreArchivo;
    private final Integer numeroAnexo;

    AnexoEstudioEnum(String nombreArchivo, Integer numeroAnexo) {
        this.nombreArchivo = nombreArchivo;
        this.numeroAnexo = numeroAnexo;
    }

    @Override
    public String toString() {
        return nombreArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public Integer getNumeroAnexo() {
        return numeroAnexo;
    }

    // Method that returns the names of the enum constants
    public static String[] names() {
        return Arrays.stream(AnexoEstudioEnum.values()).map(Enum::name).toArray(String[]::new);
    }
    }
