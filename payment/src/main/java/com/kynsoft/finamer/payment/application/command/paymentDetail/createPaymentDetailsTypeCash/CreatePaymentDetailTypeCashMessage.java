package com.kynsoft.finamer.payment.application.command.paymentDetail.createPaymentDetailsTypeCash;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePaymentDetailTypeCashMessage implements ICommandMessage {

    private UUID id;

    public CreatePaymentDetailTypeCashMessage(UUID id) {
        this.id = id;
    }

}
