package com.kynsoft.finamer.settings.application.command.managePaymentTransactionType.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateManagePaymentTransactionTypeCommand implements ICommand {

    private UUID id;
    private Status status;
    private String name;
    private String description;

    private Boolean cash;
    private Boolean agencyRateAmount;
    private Boolean negative;
    private Boolean policyCredit;
    private Boolean remarkRequired;
    private Integer minNumberOfCharacter;
    private String defaultRemark;
    private Boolean deposit;
    private Boolean applyDeposit;
    private Boolean defaults;
    private Boolean antiToIncome;
    private Boolean incomeDefault;
    private Boolean paymentInvoice;
    private Boolean debit;
    private boolean expenseToBooking;

    public UpdateManagePaymentTransactionTypeCommand(UUID id, Status status, 
            String name, String description,  Boolean cash, Boolean agencyRateAmount, 
            Boolean negative, Boolean policyCredit, Boolean remarkRequired, 
            Integer minNumberOfCharacter, String defaultRemark, Boolean deposit, Boolean applyDeposit,
            Boolean defaults, Boolean antiToIncome, Boolean incomeDefault, Boolean paymentInvoice,
            Boolean debit, boolean expenseToBooking) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.cash = cash;
        this.agencyRateAmount = agencyRateAmount;
        this.negative = negative;
        this.policyCredit = policyCredit;
        this.remarkRequired = remarkRequired;
        this.minNumberOfCharacter = minNumberOfCharacter;
        this.defaultRemark = defaultRemark;
        this.deposit = deposit;
        this.applyDeposit = applyDeposit;
        this.defaults = defaults;
        this.antiToIncome = antiToIncome;
        this.incomeDefault = incomeDefault;
        this.paymentInvoice = paymentInvoice;
        this.debit = debit;
        this.expenseToBooking = expenseToBooking;
    }

    public static UpdateManagePaymentTransactionTypeCommand fromRequest(UpdateManagePaymentTransactionTypeRequest request, UUID id) {
        return new UpdateManagePaymentTransactionTypeCommand(
                id,
                request.getStatus(),
                request.getName(),
                request.getDescription(),
                request.getCash(), request.getAgencyRateAmount(), 
                request.getNegative(), request.getPolicyCredit(), 
                request.getRemarkRequired(), request.getMinNumberOfCharacter(), 
                request.getDefaultRemark(),
                request.getDeposit(),
                request.getApplyDeposit(),
                request.getDefaults(),
                request.getAntiToIncome(),
                request.getIncomeDefault(),
                request.getPaymentInvoice(),
                request.getDebit(),
                request.isExpenseToBooking()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdateManagePaymentTransactionTypeMessage(id);
    }
}
