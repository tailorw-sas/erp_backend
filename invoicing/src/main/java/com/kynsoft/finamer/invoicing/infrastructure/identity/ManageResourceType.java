package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsoft.finamer.invoicing.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_resource_type")
public class ManageResourceType {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(unique = true)
    private String code;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean defaults;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean invoice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public ManageResourceType(ResourceTypeDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.code = dto.getCode();
        this.invoice = dto.isInvoice();
        this.status = dto.getStatus();
        this.defaults = dto.getDefaults();
        this.description = dto.getDescription();
    }

    public ResourceTypeDto toAggregate() {
        return new ResourceTypeDto(
                id, code, name, description, status, defaults, invoice
        );
    }
}
