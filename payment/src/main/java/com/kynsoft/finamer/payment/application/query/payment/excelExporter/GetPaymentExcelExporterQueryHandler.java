package com.kynsoft.finamer.payment.application.query.payment.excelExporter;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.payment.infrastructure.services.ExcelExporterService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class GetPaymentExcelExporterQueryHandler implements IQueryHandler<GetPaymentExcelExporterQuery, PaymentExcelExporterResponse> {
    private final ExcelExporterService service;
    
    public GetPaymentExcelExporterQueryHandler(ExcelExporterService service) {
        this.service = service;
    }

    @Override
    public PaymentExcelExporterResponse handle(GetPaymentExcelExporterQuery query) {

        try {
            byte[] response = this.service.exportToExcel(query.getPageable(),query.getFilter(), query.getExportEnum());
            return new PaymentExcelExporterResponse(response);
        } catch (Exception ex) {
            Logger.getLogger(GetPaymentExcelExporterQueryHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}