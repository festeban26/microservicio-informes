package ec.com.company.microservices.microservicioinformes.reports.util;

import ec.com.company.core.estudio.model.ItemsTablaTasasDescuentoPorDuracionDelPlan;
import ec.com.company.core.estudio.model.TasasFinancierasDescuento;
import ec.com.company.core.estudio.model.itemsTablasInforme.ItemTablaTasasDescuentoPorTfPromedio;
import ec.com.company.microservices.microservicioinformes.exception.coreException;
import ec.com.company.microservices.microservicioinformes.services.BaseReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class GeneradorTablaTasasDescuento {

    public static final Logger LOGGER = LoggerFactory.getLogger(GeneradorTablaTasasDescuento.class);

    public static List<ItemTablaTasasDescuentoPorTfPromedio> getItemsPorTfPromedio(
            List<TasasFinancierasDescuento> tasasFinancieraDescuento) {

        // Compares two TasaFinancieraDescuento objects by tf_desde
        Comparator<TasasFinancierasDescuento> compareByTfDesde = Comparator.comparingInt(TasasFinancierasDescuento::tfDesde);

        // Sort the list
        tasasFinancieraDescuento.sort(compareByTfDesde);

        // build objects
        ArrayList<ItemTablaTasasDescuentoPorTfPromedio> itemsTablaTasaDescuentoDiferenciada = new ArrayList<>();
        int i = 0;
        for (TasasFinancierasDescuento tasa : tasasFinancieraDescuento) {

            // En el informe este texto se despliega como porcentaje, por ello en este
            // caso se va a mostrar como porcentaje
            BigDecimal tasaDeDescuento = tasa.tasa().multiply(BigDecimal.valueOf(100));

            BigDecimal tfDesde = BigDecimal.valueOf(tasa.tfDesde());
            tfDesde = tfDesde.setScale(0, RoundingMode.HALF_UP);
            String tfdString = String.valueOf(tfDesde);

            BigDecimal tfHasta = BigDecimal.valueOf(tasa.tfHasta());
            tfHasta = tfHasta.subtract(BigDecimal.valueOf(0.01));
            tfHasta = tfHasta.setScale(2, RoundingMode.HALF_UP);
            String tfhString = String.valueOf(tfHasta);

            String duracionDelPlan = tfdString + " ";

            // if last iteration
            if (i++ == tasasFinancieraDescuento.size() - 1) {
                duracionDelPlan += "años en adelante";
            } else {
                duracionDelPlan += " a " + tfhString + " años";
            }

            ItemTablaTasasDescuentoPorTfPromedio item
                    = new ItemTablaTasasDescuentoPorTfPromedio(duracionDelPlan, tasaDeDescuento);
            itemsTablaTasaDescuentoDiferenciada.add(item);
        }

        return itemsTablaTasaDescuentoDiferenciada;
    }

    public static List<ItemsTablaTasasDescuentoPorDuracionDelPlan> getItemsPorDuracionDelPlanFirstHalf(
            List<ItemsTablaTasasDescuentoPorDuracionDelPlan> tasas) throws coreException {

        if (tasas != null && !tasas.isEmpty()) {
            int halfIndex = tasas.size() / 2;
            return tasas.subList(0, halfIndex);
        } else {
            String errMsg = "Error. No es posible dividir la lista de tasas financieras de descuento por duración " +
                    "pues la lista está vacía.";
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.BAD_REQUEST);
        }
    }

    public static List<ItemsTablaTasasDescuentoPorDuracionDelPlan> getItemsPorDuracionDelPlanSecondtHalf(
            List<ItemsTablaTasasDescuentoPorDuracionDelPlan> tasas) throws coreException {

        if (tasas != null && !tasas.isEmpty()) {
            int halfIndex = tasas.size() / 2;
            return tasas.subList(halfIndex, tasas.size());
        } else {
            String errMsg = "Error. No es posible dividir la lista de tasas financieras de descuento por duración " +
                    "pues la lista está vacía.";
            LOGGER.error(errMsg);
            throw new coreException(errMsg, HttpStatus.BAD_REQUEST);
        }
    }
}
