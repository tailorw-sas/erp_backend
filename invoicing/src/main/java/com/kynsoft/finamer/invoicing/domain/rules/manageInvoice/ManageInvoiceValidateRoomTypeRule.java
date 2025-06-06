package com.kynsoft.finamer.invoicing.domain.rules.manageInvoice;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;
import com.kynsoft.finamer.invoicing.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageRoomTypeDto;

public class ManageInvoiceValidateRoomTypeRule extends BusinessRule {

    private final ManageHotelDto hotelDto;
    private final ManageRoomTypeDto manageRoomTypeDto;
    private final String hotelBookingNumber;

    public ManageInvoiceValidateRoomTypeRule(ManageHotelDto hotelDto, ManageRoomTypeDto manageRoomTypeDto, String hotelBookingNumber) {
        super(
                DomainErrorMessage.HOTEL_ROOM_TYPE_NOT_FOUND, 
                new ErrorField("roomType", DomainErrorMessage.HOTEL_ROOM_TYPE_NOT_FOUND.getReasonPhrase() +
                        " Hotel Booking Number: "+hotelBookingNumber)
        );
        this.hotelDto = hotelDto;
        this.manageRoomTypeDto = manageRoomTypeDto;
        this.hotelBookingNumber = hotelBookingNumber;
    }

    @Override
    public boolean isBroken() {
        return !hotelDto.getCode().equals(manageRoomTypeDto.getHotel().getCode());
    }

}
