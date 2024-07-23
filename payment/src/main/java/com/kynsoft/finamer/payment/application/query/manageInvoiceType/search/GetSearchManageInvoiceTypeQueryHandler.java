package com.kynsoft.finamer.payment.application.query.manageInvoiceType.search;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceTypeService;
import org.springframework.stereotype.Component;

@Component
public class GetSearchManageInvoiceTypeQueryHandler implements IQueryHandler<GetSearchManageInvoiceTypeQuery, PaginatedResponse> {
    private final IManageInvoiceTypeService service;
    
    public GetSearchManageInvoiceTypeQueryHandler(IManageInvoiceTypeService service) {
        this.service = service;
    }

    @Override
    public PaginatedResponse handle(GetSearchManageInvoiceTypeQuery query) {

        return this.service.search(query.getPageable(),query.getFilter());
    }
}
