package com.kynsoft.finamer.settings.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.settings.domain.dto.PermissionDto;
import com.kynsoft.finamer.settings.domain.dto.PermissionStatusEnm;
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
@Table(name = "manage_permission")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "manage_permission",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManagePermission implements Serializable {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(unique = true)
    private String code;
    private String description;
    private String action;

    @Enumerated(EnumType.STRING)
    private PermissionStatusEnm status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private ManageModule module;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Boolean isHighRisk;
    private Boolean isIT;
    private String name;

    public ManagePermission(PermissionDto permissionDto) {
        this.id = permissionDto.getId();
        this.code = permissionDto.getCode();
        this.description = permissionDto.getDescription();
        this.action = permissionDto.getAction();
        this.module = new ManageModule(permissionDto.getModule());
        this.status = permissionDto.getStatus();
        this.isHighRisk = permissionDto.getIsHighRisk();
        this.isIT = permissionDto.getIsIT();
        this.name = permissionDto.getName();
    }

    public PermissionDto toAggregate() {
        return new PermissionDto(this.id, this.code, this.description, this.module.toAggregate(), this.status, this.action, createdAt, isHighRisk, isIT, name);
    }
}
