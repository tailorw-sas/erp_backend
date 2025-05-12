package com.kynsoft.finamer.payment.application.query.manageAgency.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageAgencyResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageAgencyDto;
import com.kynsoft.finamer.payment.domain.services.IManageAgencyService;
import org.springframework.stereotype.Component;

@Component
public class GetManageAgencyByIdQueryHandler implements IQueryHandler<GetManageAgencyByIdQuery, ManageAgencyResponse> {

    private final IManageAgencyService manageAgencyService;

    public GetManageAgencyByIdQueryHandler(IManageAgencyService manageAgencyService){
        this.manageAgencyService = manageAgencyService;
    }

    @Override
    public ManageAgencyResponse handle(GetManageAgencyByIdQuery query) {
        ManageAgencyDto agencyDto = this.manageAgencyService.findById(query.getId());
        return new ManageAgencyResponse(agencyDto);
    }
}
