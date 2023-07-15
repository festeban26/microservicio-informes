package ec.com.company.core.estudio.model.anexos;

import java.util.List;

public record DataAnexoContable(List<AsientoContable> asientosContables,
                                MayorContable mayorPasivo,
                                MayorContable mayorCuentasPorPagar,
                                MayorContable mayorGasto,
                                MayorContable mayorOtrosResultadosIntegrales,
                                MayorContable mayorCostoLaboral,
                                MayorContable mayorInteresNeto
) {
}
