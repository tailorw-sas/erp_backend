package com.kynsoft.finamer.settings.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.settings.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_payment_transaction_type")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "manage_payment_transaction_type",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManagePaymentTransactionType implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(unique = true)
    private String code;

    private String name;

    private String description;

    private Boolean cash;
    private Boolean agencyRateAmount;
    private Boolean negative;
    private Boolean policyCredit;
    private Boolean remarkRequired;
    @Column(nullable = true)
        private Boolean deposit;
    @Column(nullable = true)
    private Boolean applyDeposit;
    private Boolean defaults;
    private Boolean antiToIncome;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean paymentInvoice;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean debit;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean expenseToBooking;

    private Integer minNumberOfCharacter;
    private String defaultRemark;


    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean incomeDefault;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updateAt;

    public ManagePaymentTransactionType(ManagePaymentTransactionTypeDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.cash = dto.getCash();
        this.agencyRateAmount = dto.getAgencyRateAmount();
        this.negative = dto.getNegative();
        this.policyCredit = dto.getPolicyCredit();
        this.remarkRequired = dto.getRemarkRequired();
        this.minNumberOfCharacter = dto.getMinNumberOfCharacter();
        this.defaultRemark = dto.getDefaultRemark();
        this.deposit = dto.getDeposit();
        this.applyDeposit = dto.getApplyDeposit();
        this.defaults = dto.getDefaults();
        this.antiToIncome = dto.getAntiToIncome();
        this.incomeDefault = dto.getIncomeDefault();
        this.paymentInvoice = dto.getPaymentInvoice();
        this.debit = dto.getDebit();
        this.expenseToBooking = dto.isExpenseToBooking();
    }

    public ManagePaymentTransactionTypeDto toAggregate(){
        return new ManagePaymentTransactionTypeDto(id,code, description, status, name,  cash,
                agencyRateAmount,
                negative,
                policyCredit,
                remarkRequired,
                minNumberOfCharacter,
                defaultRemark,
                deposit,
                applyDeposit,
                defaults,
                antiToIncome,
                incomeDefault,
                paymentInvoice,
                debit,
                expenseToBooking
        );
    }

}
