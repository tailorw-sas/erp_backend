package com.kynsoft.finamer.payment.infrastructure.identity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsof.share.utils.BankerRounding;
import com.kynsof.share.utils.ScaleAmount;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_detail")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "payment_detail",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class PaymentDetail implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition="serial", name = "payment_detail_gen_id")
    @Generated(event = EventType.INSERT)
    private Long paymentDetailId;

    private Long parentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    @JsonBackReference
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_type_id")
    private ManagePaymentTransactionType transactionType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_booking_id")
    private Booking manageBooking;

    private Long reverseFrom;

    private Long reverseFromParentId;//Esta variable es para poder controlar el undo luego de realizar un reverse.

    private Double amount;
    private Double applyDepositValue;
    private String remark;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean reverseTransaction;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean canceledTransaction;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean createByCredit;

    private Double bookingId;
    private String invoiceId;
    private OffsetDateTime transactionDate;
    private String firstName;
    private String lastName;
    private String reservation;
    private String couponNo;
    private Integer adults;
    private Integer children;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "payment_detail_payment_detail",
            joinColumns = @JoinColumn(name = "paymentdetail_id"),
            inverseJoinColumns = @JoinColumn(name = "children_id")
    )
    private List<PaymentDetail> paymentDetails = new ArrayList<>();

    //@CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private OffsetDateTime updatedAt;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean applyPayment;

    @Column(nullable = true, updatable = true)
    private OffsetDateTime appliedAt;

    @Column(nullable = true, updatable = true)
    private OffsetDateTime effectiveDate;

    @PrePersist
    protected void prePersist() {
        this.createdAt = OffsetDateTime.now(ZoneId.of("UTC"));
    }

    public PaymentDetail(PaymentDetailDto dto) {
        this.id = dto.getId();
        this.payment = dto.getPayment() != null ? new Payment(dto.getPayment()) : null;
        this.transactionType = dto.getTransactionType() != null ? new ManagePaymentTransactionType(dto.getTransactionType()) : null;
        this.amount = BankerRounding.round(dto.getAmount());
        this.remark = dto.getRemark();
        this.status = dto.getStatus();
        if (dto.getPaymentDetails() != null) {
            this.paymentDetails = dto.getPaymentDetails().stream()
                    .map(PaymentDetail::new)
                    .collect(Collectors.toList());
        }
        this.bookingId = dto.getBookingId() != null ? dto.getBookingId() : null;
        this.invoiceId = dto.getInvoiceId() != null ? dto.getInvoiceId() : null;
        this.transactionDate = dto.getTransactionDate() != null ? dto.getTransactionDate() : null;
        this.firstName = dto.getFirstName() != null ? dto.getFirstName() : null;
        this.lastName = dto.getLastName() != null ? dto.getLastName() : null;
        this.reservation = dto.getReservation() != null ? dto.getReservation() : null;
        this.couponNo = dto.getCouponNo() != null ? dto.getCouponNo() : null;
        this.adults = dto.getAdults() != null ? dto.getAdults() : null;
        //this.children = dto.getChildren().toString() != null ? dto.getChildren().toString() : null;
        this.parentId = dto.getParentId() != null ? dto.getParentId() : null;
        this.applyDepositValue = dto.getApplyDepositValue() != null ? BankerRounding.round(dto.getApplyDepositValue()) : null;
        this.manageBooking = dto.getManageBooking() != null ? new Booking(dto.getManageBooking()) : null;
        this.reverseFrom = dto.getReverseFrom();
        this.reverseFromParentId = dto.getReverseFromParentId();
        this.reverseTransaction = dto.isReverseTransaction();
        this.createByCredit = dto.isCreateByCredit();
        this.canceledTransaction = dto.isCanceledTransaction();
        this.applyPayment = dto.getApplyPayment();
        this.appliedAt = dto.getAppliedAt();
        this.effectiveDate = dto.getEffectiveDate();
    }

    public PaymentDetailDto toAggregate() {

        return new PaymentDetailDto(
                id,
                status,
                payment != null ? payment.toAggregate() : null,
                transactionType.toAggregate(),
                amount,
                remark,
                paymentDetails != null ? paymentDetails.stream().map(PaymentDetail::toAggregateSimple).toList() : null,
                manageBooking != null ? manageBooking.toAggregate() : null,
                bookingId,
                invoiceId,
                transactionDate,
                firstName,
                lastName,
                reservation,
                couponNo,
                adults,
                children,
                createdAt,
                paymentDetailId,
                parentId,
                applyDepositValue,
                reverseFrom != null ? reverseFrom : null,
                reverseFromParentId != null ? reverseFromParentId : null,
                reverseTransaction,
                createByCredit,
                canceledTransaction,
                applyPayment,
                appliedAt,
                effectiveDate
        );
    }

    public PaymentDetailDto toAggregateSimple() {

        return new PaymentDetailDto(
                id,
                status,
                payment != null ? payment.toAggregate() : null,
                transactionType.toAggregate(),
                amount,
                remark,
                null,
                manageBooking != null ? manageBooking.toAggregate() : null,
                bookingId != null ? bookingId : null,
                invoiceId != null ? invoiceId : null,
                transactionDate != null ? transactionDate : null,
                firstName != null ? firstName : null,
                lastName != null ? lastName : null,
                reservation != null ? reservation : null,
                couponNo != null ? couponNo : null,
                adults != null ? adults : null,
                children != null ? children : null,
                createdAt,
                paymentDetailId,
                parentId,
                applyDepositValue,
                null,
                reverseFromParentId != null ? reverseFromParentId : null,
                reverseTransaction,
                createByCredit,
                canceledTransaction,
                applyPayment,
                appliedAt,
                effectiveDate
        );
    }

    public PaymentDetailDto toAggregateSimpleNotPayment() {

        return new PaymentDetailDto(
                id,
                status,
                null,
                transactionType.toAggregate(),
                amount,
                remark,
                null,
                manageBooking != null ? manageBooking.toAggregate() : null,
                bookingId != null ? bookingId : null,
                invoiceId != null ? invoiceId : null,
                transactionDate != null ? transactionDate : null,
                firstName != null ? firstName : null,
                lastName != null ? lastName : null,
                reservation != null ? reservation : null,
                couponNo != null ? couponNo : null,
                adults != null ? adults : null,
                children != null ? children : null,
                createdAt,
                paymentDetailId,
                parentId,
                applyDepositValue,
                reverseFrom != null ? reverseFrom : null,
                reverseFromParentId != null ? reverseFromParentId : null,
                reverseTransaction,
                createByCredit,
                canceledTransaction,
                applyPayment,
                appliedAt,
                effectiveDate
        );
    }
}
