package ec.com.company.microservices.microservicioinformes.reports.configs;

import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.util.HashMap;
import java.util.Map;

public abstract class JasperXlsxReportConfiguration {

    public static SimpleXlsxReportConfiguration getSimpleXlsxReportConfiguration() {
        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();

        config.setDetectCellType(true);
        config.setCollapseRowSpan(false);
        config.setWhitePageBackground(true);
        config.setOnePagePerSheet(false);
        config.setMaxRowsPerSheet(Integer.MAX_VALUE);
        config.setWrapText(true);
        config.setRemoveEmptySpaceBetweenRows(true);
        config.setRemoveEmptySpaceBetweenColumns(true);
        config.setIgnoreGraphics(false);

        Map<String, String> formatPatternsMap = new HashMap<>();
        //formatPatternsMap.put("Como me vino","Como quiero que quede");
        formatPatternsMap.put("MMM 'de' yyyy", "mmm YYY"); // Fecha
        formatPatternsMap.put("$###,###,###", "$###,###,###0"); // Fecha
        config.setFormatPatternsMap(formatPatternsMap);

        return config;
    }
}
