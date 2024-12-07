package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentSourceDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_payment_source")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "manage_payment_source", id = "7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManagePaymentSource implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    private String code;

    private String name;
    private String status;
    private Boolean expense;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean isBank;

    public ManagePaymentSource(ManagePaymentSourceDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
        this.expense = dto.getExpense();
        this.isBank = dto.getIsBank();
    }

    public ManagePaymentSourceDto toAggregate() {
        return new ManagePaymentSourceDto(id, code, name, status, expense, isBank);
    }
}
