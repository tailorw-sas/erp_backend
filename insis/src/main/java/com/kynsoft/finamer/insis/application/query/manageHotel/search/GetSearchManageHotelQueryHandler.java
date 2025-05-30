package com.kynsoft.finamer.insis.application.query.manageHotel.search;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.insis.domain.services.IManageHotelService;
import org.springframework.stereotype.Component;

@Component
public class GetSearchManageHotelQueryHandler implements IQueryHandler<GetSearchManageHotelQuery, PaginatedResponse> {

    private final IManageHotelService service;

    public GetSearchManageHotelQueryHandler(IManageHotelService service){
        this.service = service;
    }


    @Override
    public PaginatedResponse handle(GetSearchManageHotelQuery query) {
        return service.search(query.getPageable(), query.getFilter());
    }
}
