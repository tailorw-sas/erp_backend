package com.kynsoft.finamer.creditcard.application.command.hotelPayment.send;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SendHotelPaymentMessage implements ICommandMessage {

    private final String command = "SEND_HOTEL_PAYMENT";
    private final UUID hotelPaymentId;
}
