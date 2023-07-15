package ec.com.company.microservices.microservicioinformes.reports.factory;

import ec.com.company.microservices.microservicioinformes.exception.coreException;

public interface ReportSection<T> {

    // Can be a String(path) or BufferedInputStream (in case of Pdfs)
    T getFilePath() throws coreException;

}
