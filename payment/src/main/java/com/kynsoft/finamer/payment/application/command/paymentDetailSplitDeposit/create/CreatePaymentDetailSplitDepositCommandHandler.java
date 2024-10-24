package com.kynsoft.finamer.payment.application.command.paymentDetailSplitDeposit.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.rules.paymentDetail.*;
import com.kynsoft.finamer.payment.domain.services.*;
import org.springframework.stereotype.Component;

@Component
public class CreatePaymentDetailSplitDepositCommandHandler implements ICommandHandler<CreatePaymentDetailSplitDepositCommand> {

    private final IPaymentDetailService paymentDetailService;
    private final IManagePaymentTransactionTypeService paymentTransactionTypeService;
    private final IPaymentService paymentService;

    public CreatePaymentDetailSplitDepositCommandHandler(IPaymentDetailService paymentDetailService,
            IManagePaymentTransactionTypeService paymentTransactionTypeService,
            IPaymentService paymentService) {
        this.paymentDetailService = paymentDetailService;
        this.paymentTransactionTypeService = paymentTransactionTypeService;
        this.paymentService = paymentService;
    }

    @Override
    public void handle(CreatePaymentDetailSplitDepositCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getEmployee(), "id", "Employee ID cannot be null."));

        //El valor ingresado no debe ser cero.
        RulesChecker.checkRule(new CheckPaymentDetailAmountSplitGreaterThanZeroRule(command.getAmount()));

        ManagePaymentTransactionTypeDto paymentTransactionTypeDto = this.paymentTransactionTypeService.findById(command.getTransactionType());
        RulesChecker.checkRule(new CheckDepositTransactionTypeRule(paymentTransactionTypeDto.getDeposit()));

        PaymentDetailDto paymentDetailDto = this.paymentDetailService.findById(command.getPaymentDetail());
        //Valida que la trx seleccionada sea de tipo deposit
        RulesChecker.checkRule(new CheckDepositToSplitRule(paymentDetailDto.getTransactionType().getDeposit()));
        //Valida que la trx seleccionada tenga balance al momento de aplicar el split.
        RulesChecker.checkRule(new CheckSplitAmountRule(command.getAmount(), paymentDetailDto.getAmount()));
        //Valida que no exista un Apply Deposit asociado a dicho deposito.
        RulesChecker.checkRule(new CheckPaymentDetailDepositTypeIsApplyRule(paymentDetailDto.getChildren()));

        ConsumerUpdate updatePayment = new ConsumerUpdate();
        UpdateIfNotNull.updateDouble(paymentDetailDto::setAmount, paymentDetailDto.getAmount() + command.getAmount(), updatePayment::setUpdate);

        PaymentDetailDto split = new PaymentDetailDto(
                command.getId(),
                command.getStatus(),
                paymentDetailDto.getPayment(),
                paymentTransactionTypeDto,
                command.getAmount() * -1,
                command.getRemark(),
                null,
                null,
                null,
                paymentDetailDto.getTransactionDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                false
        );

        split.setApplyDepositValue(command.getAmount());
        split.setPaymentDetailId(this.paymentDetailService.queryForNextPaymentDetailId());
        paymentDetailService.create(split);
        paymentDetailService.update(paymentDetailDto);

        command.setPaymentResponse(this.paymentService.findById(paymentDetailDto.getPayment().getId()));
    }

}
