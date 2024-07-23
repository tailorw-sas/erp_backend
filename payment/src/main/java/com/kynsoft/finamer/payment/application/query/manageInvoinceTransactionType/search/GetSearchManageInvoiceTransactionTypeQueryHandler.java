package com.kynsoft.finamer.payment.application.query.manageInvoinceTransactionType.search;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceTransactionTypeService;
import org.springframework.stereotype.Component;

@Component
public class GetSearchManageInvoiceTransactionTypeQueryHandler implements IQueryHandler<GetSearchManageInvoiceTransactionTypeQuery, PaginatedResponse> {
    private final IManageInvoiceTransactionTypeService service;
    
    public GetSearchManageInvoiceTransactionTypeQueryHandler(IManageInvoiceTransactionTypeService service) {
        this.service = service;
    }

    @Override
    public PaginatedResponse handle(GetSearchManageInvoiceTransactionTypeQuery query) {

        return this.service.search(query.getPageable(),query.getFilter());
    }
}