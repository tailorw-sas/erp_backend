package com.kynsoft.finamer.settings.application.query.manageBankAccount.search;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.settings.domain.services.IManageBankAccountService;
import org.springframework.stereotype.Component;

@Component
public class GetSearchManageBankAccountQueryHandler implements IQueryHandler<GetSearchManageBankAccountQuery, PaginatedResponse> {

    private final IManageBankAccountService service;

    public GetSearchManageBankAccountQueryHandler(IManageBankAccountService service) {
        this.service = service;
    }

    @Override
    public PaginatedResponse handle(GetSearchManageBankAccountQuery query) {
        return service.search(query.getPageable(), query.getFilter());
    }
}
