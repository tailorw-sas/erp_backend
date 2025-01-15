package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.invoicing.domain.dto.ManageAdjustmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "adjustment")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "adjustment",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManageAdjustment {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "serial", name = "adjustment_gen_id")
    @Generated(event = EventType.INSERT)
    private Long adjustmentId;

    private Double amount;
    private LocalDateTime date;
    private String description;
    private String employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_type", nullable = true)
    private ManageInvoiceTransactionType transaction;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_payment_type", nullable = true)
    private ManagePaymentTransactionType paymentTransactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manage_room_rate", nullable = true)
    private ManageRoomRate roomRate;

    @Column(nullable = true)
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean deleteInvoice;

    public ManageAdjustment(ManageAdjustmentDto dto) {
        this.id = dto.getId();
        this.amount = dto.getAmount();
        this.date = dto.getDate();
        this.description = dto.getDescription();
        this.employee = dto.getEmployee();
        this.transaction = dto.getTransaction() != null ? new ManageInvoiceTransactionType(dto.getTransaction()) : null;
        this.roomRate = dto.getRoomRate() != null ? new ManageRoomRate(dto.getRoomRate()) : null;
        this.paymentTransactionType = dto.getPaymentTransactionType() != null ? new ManagePaymentTransactionType(dto.getPaymentTransactionType()) : null;
        this.deleteInvoice = dto.isDeleteInvoice();
    }

    public ManageAdjustmentDto toAggregate() {
        return new ManageAdjustmentDto(
                id, 
                adjustmentId, 
                amount, 
                date, 
                description,
                transaction != null ? transaction.toAggregate() : null, 
                paymentTransactionType != null ? paymentTransactionType.toAggregate() : null,
                roomRate != null ? roomRate.toAggregate() : null, 
                employee,
                deleteInvoice
        );
    }

    public ManageAdjustmentDto toAggregateSample() {
        return new ManageAdjustmentDto(
                id, 
                adjustmentId, amount, 
                date, 
                description,
                transaction != null ? transaction.toAggregate() : null, 
                paymentTransactionType != null ? paymentTransactionType.toAggregate() : null,
                null, 
                employee,
                deleteInvoice
        );
    }
}
