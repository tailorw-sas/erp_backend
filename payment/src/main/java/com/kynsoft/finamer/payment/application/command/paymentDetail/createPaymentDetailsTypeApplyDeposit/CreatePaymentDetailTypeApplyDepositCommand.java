package com.kynsoft.finamer.payment.application.command.paymentDetail.createPaymentDetailsTypeApplyDeposit;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePaymentDetailTypeApplyDepositCommand implements ICommand {

    private PaymentDto payment;
    private double amount;
    private PaymentDetailDto parentDetailDto;
    private UUID id;
    private boolean applyPayment;

    private PaymentDetailDto newDetailDto;

    public CreatePaymentDetailTypeApplyDepositCommand(PaymentDto payment, double amount, PaymentDetailDto parentDetailDto, boolean applyPayment) {
        this.payment = payment;
        this.amount = amount;
        this.parentDetailDto = parentDetailDto;
        this.id = UUID.randomUUID();
        this.applyPayment = applyPayment;
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreatePaymentDetailTypeApplyDepositMessage(newDetailDto);
    }
}
