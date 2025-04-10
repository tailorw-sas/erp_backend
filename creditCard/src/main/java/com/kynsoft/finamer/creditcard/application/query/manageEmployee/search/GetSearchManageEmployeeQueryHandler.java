package com.kynsoft.finamer.creditcard.application.query.manageEmployee.search;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.creditcard.domain.services.IManageEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class GetSearchManageEmployeeQueryHandler implements IQueryHandler<GetSearchManageEmployeeQuery, PaginatedResponse> {
    private final IManageEmployeeService service;
    
    public GetSearchManageEmployeeQueryHandler(IManageEmployeeService service) {
        this.service = service;
    }

    @Override
    public PaginatedResponse handle(GetSearchManageEmployeeQuery query) {

        return this.service.search(query.getPageable(),query.getFilter());
    }
}
