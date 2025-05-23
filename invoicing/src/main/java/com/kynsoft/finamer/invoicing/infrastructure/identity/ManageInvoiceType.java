package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceTypeDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_invoice_type")
public class ManageInvoiceType implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    private String code;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean deleted = false;

    private String name;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean invoice;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean credit;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean income;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean enabledToPolicy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    public ManageInvoiceType(ManageInvoiceTypeDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.invoice = dto.isInvoice();
        this.credit = dto.isCredit();
        this.income = dto.isIncome();
        this.status = dto.getStatus();
        this.enabledToPolicy = dto.isEnabledToPolicy();
    }

    public ManageInvoiceTypeDto toAggregate() {
        return new ManageInvoiceTypeDto(
                id, code, name, invoice, credit, income, status, enabledToPolicy
        );
    }

}
