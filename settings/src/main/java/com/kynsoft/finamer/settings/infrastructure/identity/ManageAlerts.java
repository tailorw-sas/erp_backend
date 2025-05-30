package com.kynsoft.finamer.settings.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.settings.domain.dto.ManageAlertsDto;
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
@Table(name = "manage_alert")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "manage_alert",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManageAlerts implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(unique = true)
    private String code;

    private String name;
    @Column(name = "popup")
    private Boolean popup;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public ManageAlerts(ManageAlertsDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.popup = dto.getPopup();
        this.status = dto.getStatus();
        this.description = dto.getDescription();
    }

    public ManageAlertsDto toAggregate() {
        return new ManageAlertsDto(this.id, this.code, this.name, this.popup, this.status, this.description);
    }
}
