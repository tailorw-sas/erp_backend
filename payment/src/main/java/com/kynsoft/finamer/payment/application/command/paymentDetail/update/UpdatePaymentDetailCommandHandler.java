package com.kynsoft.finamer.payment.application.command.paymentDetail.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.payment.domain.services.IPaymentDetailService;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import java.util.UUID;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

@Component
public class UpdatePaymentDetailCommandHandler implements ICommandHandler<UpdatePaymentDetailCommand> {

    private final IPaymentDetailService paymentDetailService;
    private final IManagePaymentTransactionTypeService paymentTransactionTypeService;
    private final IPaymentService paymentService;

    public UpdatePaymentDetailCommandHandler(IPaymentDetailService paymentDetailService, IManagePaymentTransactionTypeService paymentTransactionTypeService, IPaymentService paymentService) {
        this.paymentDetailService = paymentDetailService;
        this.paymentTransactionTypeService = paymentTransactionTypeService;
        this.paymentService = paymentService;
    }

    @Override
    public void handle(UpdatePaymentDetailCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Payment Detail ID cannot be null."));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getPayment(), "payment", "Payment ID cannot be null."));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getTransactionType(), "transactionPayment", "Manage Payment Transaction Type ID cannot be null."));

        PaymentDetailDto paymentDetailDto = this.paymentDetailService.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(paymentDetailDto::setRemark, command.getRemark(), paymentDetailDto.getRemark(), update::setUpdate);
        UpdateIfNotNull.updateDouble(paymentDetailDto::setAmount, command.getAmount(), paymentDetailDto.getAmount(), update::setUpdate);

        this.updateStatus(paymentDetailDto::setStatus, command.getStatus(), paymentDetailDto.getStatus(), update::setUpdate);
        this.updatePayment(paymentDetailDto::setPayment, command.getPayment(), paymentDetailDto.getPayment().getId(), update::setUpdate);
        this.updateManagePaymentTransactionType(paymentDetailDto::setTransactionType, command.getTransactionType(), paymentDetailDto.getTransactionType().getId(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.paymentDetailService.update(paymentDetailDto);
        }

    }

    private boolean updatePayment(Consumer<PaymentDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            PaymentDto paymentDto = this.paymentService.findById(newValue);
            setter.accept(paymentDto);
            update.accept(1);

            return true;
        }
        return false;
    }

    private boolean updateManagePaymentTransactionType(Consumer<ManagePaymentTransactionTypeDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManagePaymentTransactionTypeDto attachmentStatusDto = this.paymentTransactionTypeService.findById(newValue);
            setter.accept(attachmentStatusDto);
            update.accept(1);

            return true;
        }
        return false;
    }

    private void updateStatus(Consumer<Status> setter, Status newValue, Status oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);

        }
    }

}
