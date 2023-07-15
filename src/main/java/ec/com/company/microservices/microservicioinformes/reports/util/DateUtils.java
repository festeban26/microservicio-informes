package ec.com.company.microservices.microservicioinformes.reports.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static final String CADENA_FORMATO_PERIODO_UTILIZACION = "MMMyy";

    public static String getPeriodoUtilizacionString(Date fechaInicio, Date fechaFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CADENA_FORMATO_PERIODO_UTILIZACION);
        LocalDate fechaInicioLD = localDateFromDate(fechaInicio).plusMonths(1);
        String fechaInicioString = fechaInicioLD.format(formatter).substring(0, 1).toUpperCase() + fechaInicioLD.format(formatter).substring(1);
        // The MMM pattern returns the three-letter abbreviation of the month in the specified locale, which can include
        // a period at the end in some locales (like ene. for enero in Spanish). remove it manually.
        fechaInicioString = fechaInicioString.replace(".", ""); //remove dot
        LocalDate fechaFinLD = localDateFromDate(fechaFin);
        String fechaFinString = fechaFinLD.format(formatter).substring(0, 1).toUpperCase() + fechaFinLD.format(formatter).substring(1);
        fechaFinString = fechaFinString.replace(".", ""); //remove dot
        return fechaInicioString + "/" + fechaFinString;
    }

    public static LocalDate localDateFromDate(Date inputDate) {
        LocalDate resp;
        resp = inputDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return resp;
    }
}
