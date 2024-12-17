package com.kynsoft.finamer.creditcard.application.query.manageMerchantHotelEnrolle.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.ManageMerchantHotelEnrolleResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageMerchantHotelEnrolleDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageMerchantHotelEnrolleService;
import org.springframework.stereotype.Component;

@Component
public class FindManageMerchantHotelEnrolleByIdQueryHandler implements IQueryHandler<FindManageMerchantHotelEnrolleByIdQuery, ManageMerchantHotelEnrolleResponse> {

    private final IManageMerchantHotelEnrolleService service;

    public FindManageMerchantHotelEnrolleByIdQueryHandler(IManageMerchantHotelEnrolleService service) {
        this.service = service;
    }

    @Override
    public ManageMerchantHotelEnrolleResponse handle(FindManageMerchantHotelEnrolleByIdQuery query) {
        ManageMerchantHotelEnrolleDto dto = service.findById(query.getId());

        return new ManageMerchantHotelEnrolleResponse(dto);
    }
}
