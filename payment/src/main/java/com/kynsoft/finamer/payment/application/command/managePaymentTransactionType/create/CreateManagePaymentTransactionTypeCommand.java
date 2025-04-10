package com.kynsoft.finamer.payment.application.command.managePaymentTransactionType.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.payment.application.query.http.setting.paymenteTransactionType.ManagePaymentTransactionTypeResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class CreateManagePaymentTransactionTypeCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private Boolean cash;
    private Boolean deposit;
    private Boolean applyDeposit;
    private Boolean remarkRequired;
    private Integer minNumberOfCharacter;
    private String defaultRemark;
    private boolean defaults;
    private Boolean paymentInvoice;
    private Boolean debit;
    private boolean expenseToBooking;
    private Boolean negative;

    public CreateManagePaymentTransactionTypeCommand(UUID id,
                                                     String code,
                                                     String name,
                                                     String status,
                                                     Boolean cash,
                                                     Boolean deposit,
                                                     Boolean applyDeposit,
                                                     Boolean remarkRequired,
                                                     Integer minNumberOfCharacter,
                                                     String defaultRemark,
                                                     Boolean defaults,
                                                     Boolean paymentInvoice,
                                                     Boolean debit,
                                                     boolean expenseToBooking,
                                                     Boolean negative) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.cash = cash;
        this.deposit = deposit;
        this.applyDeposit = applyDeposit;
        this.remarkRequired = remarkRequired;
        this.minNumberOfCharacter = minNumberOfCharacter;
        this.defaultRemark = defaultRemark;
        this.defaults = Objects.nonNull(defaults) ? defaults : false;
        this.paymentInvoice = paymentInvoice;
        this.debit = debit;
        this.expenseToBooking = expenseToBooking;
        this.negative = negative;
    }

    public static CreateManagePaymentTransactionTypeCommand fromRequest(ManagePaymentTransactionTypeResponse response) {
        return new CreateManagePaymentTransactionTypeCommand(
                response.getId(),
                response.getCode(),
                response.getName(),
                response.getStatus(),
                response.getCash(),
                response.getDeposit(),
                response.getApplyDeposit(),
                response.getRemarkRequired(),
                response.getMinNumberOfCharacter(),
                response.getDefaultRemark(),
                response.getDefaults(),
                response.getPaymentInvoice(),
                response.getDebit(),
                response.isExpenseToBooking(),
                response.getNegative()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManagePaymentTransactionTypeMessage(id);
    }
}
