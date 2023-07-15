package ec.com.company.core.estudio.model.anexos;



public enum AsientosContablesEnum {

    INCREMENTO_RESERVA_DESAHUCIO_NEC(
            AsientosContablesConstants.DESCRIPCION_INCREMENTO_RESERVA_DESAHUCIO_NEC,
            null,
            AsientosContablesConstants.GASTO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    DECREMENTO_RESERVA_DESAHUCIO_NEC(
            AsientosContablesConstants.DESCRIPCION_DECREMENTO_RESERVA_DESAHUCIO_NEC,
            null,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.OTROS_INGRESOS
    ),
    INCREMENTO_RESERVA_JUBILACION_NEC(
            AsientosContablesConstants.DESCRIPCION_INCREMENTO_RESERVA_JUBILACION_NEC,
            null,
            AsientosContablesConstants.GASTO_JUBILACION_PATRONAL,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    DECREMENTO_RESERVA_JUBILACION_NEC(
            AsientosContablesConstants.DESCRIPCION_DECREMENTO_RESERVA_JUBILACION_NEC,
            null,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.OTROS_INGRESOS
    ),
    VARIACION_RESERVAS_DESAHUCIO_MAYOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_DESAHUCIO,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    VARIACION_RESERVAS_DESAHUCIO_MENOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_DESAHUCIO,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS
    ),
    VARIACION_RESERVAS_DESAHUCIO_NEC_MAYOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_DESAHUCIO,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS_NEC,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    VARIACION_RESERVAS_DESAHUCIO_NEC_MENOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_DESAHUCIO,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS_NEC
    ),
    VARIACION_RESERVAS_JUBILACION_MAYOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_JUBILACION,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    VARIACION_RESERVAS_JUBILACION_MENOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_JUBILACION,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS
    ),
    VARIACION_RESERVAS_JUBILACION_NEC_MAYOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_JUBILACION,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS_NEC,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    VARIACION_RESERVAS_JUBILACION_NEC_MENOR(
            AsientosContablesConstants.DESCRIPCION_VARIACION_RESERVAS,
            AsientosContablesConstants.NOTA_ASIENTO_VARIACION_RESERVAS_JUBILACION,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.VARIACION_RESERVAS_NO_REGULARIZADAS_NEC
    ),
    EFECTO_REDUCCIONES_LIQUIDACIONES_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_EFECTO_REDUCCIONES_LIQUIDACIONES,
            null,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.OTROS_INGRESOS
    ),
    EFECTO_REDUCCIONES_LIQUIDACIONES_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_EFECTO_REDUCCIONES_LIQUIDACIONES,
            null,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.OTROS_INGRESOS
    ),
    COSTO_SERVICIOS_PASADOS_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_COSTO_SERVICIOS_PASADOS_DESAHUCIO,
            null,
            AsientosContablesConstants.COSTO_SERVICIOS_PASADOS,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    BENEFICIOS_PAGADOS_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_PAGOS_EFECTUADOS_DESAHUCIO,
            null,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.CUENTAS_POR_PAGAR
    ),
    BENEFICIOS_PAGADOS_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_PAGOS_EFECTUADOS_JUBILACION,
            null,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.CUENTAS_POR_PAGAR
    ),
    COSTO_LABORAL_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_GASTO_COSTO_LABORAL_DESAHUCIO,
            null,
            AsientosContablesConstants.GASTO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    COSTO_LABORAL_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_GASTO_COSTO_LABORAL,
            null,
            AsientosContablesConstants.GASTO_JUBILACION_PATRONAL,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    INTERES_NETO_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_GASTO_INTERES_NETO_DESAHUCIO,
            null,
            AsientosContablesConstants.GASTO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    INTERES_NETO_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_GASTO_INTERES_NETO,
            null,
            AsientosContablesConstants.GASTO_JUBILACION_PATRONAL,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    PERDIDA_RECONOCIDA_ORI_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_PERDIDA_RECONOCIDA_ORI,
            null,
            AsientosContablesConstants.PERDIDA_companyL_ORI,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    GANANCIA_RECONOCIDA_ORI_DESAHUCIO(
            AsientosContablesConstants.DESCRIPCION_GANANCIA_RECONOCIDA_ORI,
            null,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO,
            AsientosContablesConstants.GANANCIA_ACTUAL_ORI
    ),
    PERDIDA_RECONOCIDA_ORI_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_PERDIDA_RECONOCIDA_ORI,
            null,
            AsientosContablesConstants.PERDIDA_companyL_ORI,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    GANANCIA_RECONOCIDA_ORI_JUBILACION(
            AsientosContablesConstants.DESCRIPCION_GANANCIA_RECONOCIDA_ORI,
            null,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL,
            AsientosContablesConstants.GANANCIA_ACTUAL_ORI
    ),
    TRANSFERENCIAS_DESAHUCIO_MAYOR(
            AsientosContablesConstants.DESCRIPCION_TRANSFERENCIAS,
            null,
            AsientosContablesConstants.TRANSFERENCIAS_CUENTAS_POR_COBRAR,
            AsientosContablesConstants.PASIVO_BONIFICACION_DESAHUCIO
    ),
    TRANSFERENCIAS_DESAHUCIO_MENOR(
            AsientosContablesConstants.DESCRIPCION_TRANSFERENCIAS,
            null,
            AsientosContablesConstants.TRANSFERENCIAS_CUENTAS_POR_PAGAR,
            AsientosContablesConstants.OTROS_INGRESOS
    ),
    TRANSFERENCIAS_JUBILACION_MAYOR(
            AsientosContablesConstants.DESCRIPCION_TRANSFERENCIAS,
            null,
            AsientosContablesConstants.TRANSFERENCIAS_CUENTAS_POR_COBRAR,
            AsientosContablesConstants.PASIVO_JUBILACION_PATRONAL
    ),
    TRANSFERENCIAS_JUBILACION_MENOR(
            AsientosContablesConstants.DESCRIPCION_TRANSFERENCIAS,
            null,
            AsientosContablesConstants.TRANSFERENCIAS_CUENTAS_POR_PAGAR,
            AsientosContablesConstants.OTROS_INGRESOS
    );

    private final String descripcionBase;
    private final String nota;
    private final String descripcionDebe;
    private final String descripcionHaber;

    AsientosContablesEnum(String descripcionBase, String nota, String descripcionDebe, String descripcionHaber) {
        this.descripcionBase = descripcionBase;
        this.nota = nota;
        this.descripcionDebe = descripcionDebe;
        this.descripcionHaber = descripcionHaber;
    }

    public String getDescripcionBase() {
        return descripcionBase;
    }

    public String getNota() {
        return this.nota;
    }

    public String getDescripcionDebe() {
        return descripcionDebe;
    }

    public String getDescripcionHaber() {
        return descripcionHaber;
    }
}
