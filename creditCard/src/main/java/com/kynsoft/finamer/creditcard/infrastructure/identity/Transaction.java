package com.kynsoft.finamer.creditcard.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.MethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vcc_transaction")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "vcc_transaction",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_uuid", unique = true)
    private UUID transactionUuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_merchant_id")
    private ManageMerchant merchant;

    @Enumerated(EnumType.STRING)
    private MethodType methodType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_hotel_id")
    private ManageHotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_agency_id")
    private ManageAgency agency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_language_id")
    private ManageLanguage language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_merchant_currency_id")
    private ManagerMerchantCurrency merchantCurrency;

    private Double amount;

    private LocalDateTime checkIn;

    private String reservationNumber;

    private String referenceNumber;

    private String hotelContactEmail;

    private String guestName;

    private String email;

    private String enrolleCode;

    private String cardNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_credit_card_type_id")
    private ManageCreditCardType creditCardType;

    private Double commission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_status_id")
    private ManageTransactionStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_parent_id")
    private Transaction parent;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_category_id")
    private ManageVCCTransactionType transactionCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_sub_category_id")
    private ManageVCCTransactionType transactionSubCategory;

    private Double netAmount;

    @Column(name = "permit_refund")
    private Boolean permitRefund = true;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean manual;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean adjustment;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_reconciliation")
    private ManageBankReconciliation reconciliation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean hasAttachments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_payment")
    private HotelPayment hotelPayment;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @Column(name = "authorization_code")
    private String authorizationCode;

    public Transaction(TransactionDto dto) {
        this.id = dto.getId();
        this.merchant = dto.getMerchant() != null ? new ManageMerchant(dto.getMerchant()) : null;
        this.methodType = dto.getMethodType();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.language = dto.getLanguage() != null ? new ManageLanguage(dto.getLanguage()) : null;
        this.amount = dto.getAmount();
        this.checkIn = dto.getCheckIn();
        this.reservationNumber = dto.getReservationNumber();
        this.referenceNumber = dto.getReferenceNumber();
        this.hotelContactEmail = dto.getHotelContactEmail();
        this.guestName = dto.getGuestName();
        this.email = dto.getEmail();
        this.enrolleCode = dto.getEnrolleCode();
        this.cardNumber = dto.getCardNumber();
        this.creditCardType = dto.getCreditCardType() != null ? new ManageCreditCardType(dto.getCreditCardType()) : null;
        this.commission = dto.getCommission();
        this.status = dto.getStatus() != null ? new ManageTransactionStatus(dto.getStatus()) : null;
        this.parent = dto.getParent() != null ? new Transaction(dto.getParent()) : null;
        this.transactionCategory = dto.getTransactionCategory() != null ? new ManageVCCTransactionType(dto.getTransactionCategory()) : null;
        this.transactionSubCategory = dto.getTransactionSubCategory() != null ? new ManageVCCTransactionType(dto.getTransactionSubCategory()) : null;
        this.netAmount = dto.getNetAmount();
        if(dto.getPermitRefund() != null){
            this.permitRefund = dto.getPermitRefund();
        }
        this.merchantCurrency = dto.getMerchantCurrency() != null ? new ManagerMerchantCurrency(dto.getMerchantCurrency()) : null;
        transactionUuid= dto.getTransactionUuid();
        this.manual = dto.isManual();
        this.paymentDate = dto.getPaymentDate();
        this.reconciliation = dto.getReconciliation() != null ? new ManageBankReconciliation(dto.getReconciliation()) : null;
        this.adjustment = dto.isAdjustment();
        this.attachments = dto.getAttachments() != null
                ? dto.getAttachments().stream().map(attachmentDto -> {
                        Attachment attachment = new Attachment(attachmentDto);
                        attachment.setTransaction(this);
                        return attachment;
                    }).collect(Collectors.toList())
                : null;
        this.hotelPayment = dto.getHotelPayment() != null ? new HotelPayment(dto.getHotelPayment()) : null;
        this.transactionDate = dto.getTransactionDate();
        this.authorizationCode = dto.getAuthorizationCode();
    }

    public TransactionDto toAggregateParent() {
        return new TransactionDto(
                id,transactionUuid, checkIn, reservationNumber, referenceNumber,
                transactionDate, authorizationCode);
    }

    public TransactionDto toAggregate(){
        return new TransactionDto(
                id,
                transactionUuid,
                merchant != null ? merchant.toAggregate() : null,
                methodType,
                hotel != null ? hotel.toAggregate() : null,
                agency != null ? agency.toAggregate() : null,
                language != null ? language.toAggregate() : null,
                amount,
                checkIn, reservationNumber, referenceNumber, hotelContactEmail,
                guestName, email, enrolleCode, cardNumber,
                creditCardType != null ? creditCardType.toAggregate() : null,
                commission,
                status != null ? status.toAggregate() : null,
                parent != null ? parent.toAggregateParent() : null,
                transactionDate,
                transactionCategory != null ? transactionCategory.toAggregate() : null,
                transactionSubCategory != null ? transactionSubCategory.toAggregate() : null,
                netAmount, permitRefund, merchantCurrency != null ? merchantCurrency.toAggregate() : null,
                manual,
                adjustment,
                paymentDate,
                reconciliation != null ? reconciliation.toAggregateSimple() : null,
                attachments != null ? attachments.stream().map(Attachment::toAggregate).collect(Collectors.toList()) : null,
                hotelPayment != null ? hotelPayment.toAggregateSimple() : null,
                authorizationCode
        );
    }

    @PostLoad
    public void initDefaultValue() {
        hasAttachments = (attachments != null && !attachments.isEmpty());
        transactionDate = transactionDate != null ? transactionDate : createdAt;
    }
}
