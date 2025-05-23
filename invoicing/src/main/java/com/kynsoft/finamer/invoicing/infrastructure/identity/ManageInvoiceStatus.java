package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceStatusDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_invoice_status")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "manage_invoice_status",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManageInvoiceStatus implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;

    private String name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    private Boolean enabledToPrint;
    private Boolean enabledToPropagate;
    private Boolean enabledToApply;
    private Boolean enabledToPolicy;
    private Boolean processStatus;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean sentStatus;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean reconciledStatus;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean canceledStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "manage_invoice_status_relations",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<ManageInvoiceStatus> navigate = new ArrayList<>();

    private Boolean showClone;

    public ManageInvoiceStatus(ManageInvoiceStatusDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.status = dto.getStatus();
        this.description = dto.getDescription();
        this.name = dto.getName();
        this.enabledToPrint = dto.getEnabledToPrint();
        this.enabledToPropagate = dto.getEnabledToPropagate();
        this.enabledToApply = dto.getEnabledToApply();
        this.enabledToPolicy = dto.getEnabledToPolicy();
        this.processStatus = dto.getProcessStatus();
        if (dto.getNavigate() != null) {
            this.navigate = dto.getNavigate().stream()
                    .map(ManageInvoiceStatus::new)
                    .collect(Collectors.toList());
        }
        this.showClone = dto.getShowClone();
        this.canceledStatus = dto.isCanceledStatus();
        this.reconciledStatus = dto.isReconciledStatus();
        this.sentStatus = dto.isSentStatus();
    }

    public ManageInvoiceStatusDto toAggregateSimple() {
        return new ManageInvoiceStatusDto(
                id, code, description, status, name, enabledToPrint, enabledToPropagate,
                enabledToApply, enabledToPolicy, processStatus,
                null, showClone, sentStatus, reconciledStatus, canceledStatus
        );
    }

    public ManageInvoiceStatusDto toAggregate() {
        return new ManageInvoiceStatusDto(
                id, code, description, status, name, enabledToPrint, enabledToPropagate,
                enabledToApply, enabledToPolicy, processStatus,
                navigate != null ? navigate.stream().map(ManageInvoiceStatus::toAggregateSimple).toList() : null,
                showClone, sentStatus, reconciledStatus, canceledStatus
        );
    }
}
