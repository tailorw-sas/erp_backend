package com.kynsoft.finamer.payment.application.query.manageHotel.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageHotelResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.payment.domain.services.IManageHotelService;
import org.springframework.stereotype.Component;

@Component
public class GetManageHotelByIdQueryHandler implements IQueryHandler<GetManageHotelByIdQuery, ManageHotelResponse> {

    private final IManageHotelService hotelService;

    public GetManageHotelByIdQueryHandler(IManageHotelService hotelService){
        this.hotelService = hotelService;
    }

    @Override
    public ManageHotelResponse handle(GetManageHotelByIdQuery query) {
        ManageHotelDto hotelDto = hotelService.findById(query.getId());
        return new ManageHotelResponse(hotelDto);
    }
}