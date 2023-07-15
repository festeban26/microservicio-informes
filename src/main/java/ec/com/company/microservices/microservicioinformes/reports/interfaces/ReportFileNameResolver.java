package ec.com.company.microservices.microservicioinformes.reports.interfaces;

import ec.com.company.microservices.microservicioinformes.reports.enums.AnexoEstudioEnum;
import ec.com.company.microservices.microservicioinformes.reports.enums.ReportFileExtensionEnum;

public interface ReportFileNameResolver {
    String get(String numeroProceso, String versionInforme, AnexoEstudioEnum anexoEstudioEnum, ReportFileExtensionEnum reportFileExtensionEnum);
    String get(String numeroProceso, String versionInforme);
}