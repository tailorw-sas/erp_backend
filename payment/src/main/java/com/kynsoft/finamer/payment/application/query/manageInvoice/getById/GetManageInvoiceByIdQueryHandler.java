package com.kynsoft.finamer.payment.application.query.manageInvoice.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageInvoiceResponse;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceService;
import org.springframework.stereotype.Component;

@Component
public class GetManageInvoiceByIdQueryHandler implements IQueryHandler<GetManageInvoiceByIdQuery, ManageInvoiceResponse> {

    private final IManageInvoiceService manageInvoiceService;

    public GetManageInvoiceByIdQueryHandler(IManageInvoiceService manageInvoiceService){
        this.manageInvoiceService = manageInvoiceService;
    }

    @Override
    public ManageInvoiceResponse handle(GetManageInvoiceByIdQuery query) {
        ManageInvoiceResponse response = new ManageInvoiceResponse(manageInvoiceService.findById(query.getId()));
        return response;
    }
}
