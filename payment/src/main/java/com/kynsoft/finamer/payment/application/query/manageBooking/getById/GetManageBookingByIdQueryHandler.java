package com.kynsoft.finamer.payment.application.query.manageBooking.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageBookingResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.services.IManageBookingService;
import org.springframework.stereotype.Component;

@Component
public class GetManageBookingByIdQueryHandler implements IQueryHandler<GetManageBookingByIdQuery, ManageBookingResponse> {

    private final IManageBookingService manageBookingService;

    public GetManageBookingByIdQueryHandler(IManageBookingService manageBookingService){
        this.manageBookingService = manageBookingService;
    }

    @Override
    public ManageBookingResponse handle(GetManageBookingByIdQuery query) {
        ManageBookingDto bookingDto = this.manageBookingService.findById(query.getId());
        return new ManageBookingResponse(bookingDto);
    }
}
