package com.kynsoft.finamer.payment.infrastructure.excel.event.applyDeposit;

import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.paymentDetailApplyDeposit.create.CreatePaymentDetailApplyDepositCommand;
import com.kynsoft.finamer.payment.application.command.paymentImport.detail.applyDeposit.CreatePaymentDetailApplyDepositFromFileCommand;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplyDepositEventHandler implements ApplicationListener<ApplyDepositEvent> {

    private final IMediator mediator;

    public ApplyDepositEventHandler(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void onApplicationEvent(ApplyDepositEvent event) {
        CreatePaymentDetailApplyDepositFromFileCommand createPaymentDetailApplyDepositCommand = (CreatePaymentDetailApplyDepositFromFileCommand) event.getSource();
        mediator.send(createPaymentDetailApplyDepositCommand);
    }
}
