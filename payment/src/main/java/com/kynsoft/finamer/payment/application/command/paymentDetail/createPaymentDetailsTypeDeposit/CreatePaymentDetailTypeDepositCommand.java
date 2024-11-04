package com.kynsoft.finamer.payment.application.command.paymentDetail.createPaymentDetailsTypeDeposit;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePaymentDetailTypeDepositCommand implements ICommand {

    private PaymentDto payment;
    private UUID id;
    private boolean createByCredit;

    private PaymentDetailDto newDetailDto;

    public CreatePaymentDetailTypeDepositCommand(PaymentDto payment, boolean createByCredit) {
        this.id = UUID.randomUUID();
        this.payment = payment;
        this.createByCredit = createByCredit;
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreatePaymentDetailTypeDepositMessage(newDetailDto);
    }
}
