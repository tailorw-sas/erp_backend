package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
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
@Table(name = "resource_type")
public class ResourceType {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(unique = true)
    private String code;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean defaults;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean invoice;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean invoiceDefault;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public ResourceType(ResourceTypeDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.code = dto.getCode();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.defaults = dto.getDefaults();
        this.invoice = dto.isInvoice();
        this.invoiceDefault = dto.isInvoiceDefault();
    }

    public ResourceTypeDto toAggregate() {
        return new ResourceTypeDto(
                id, code, name, description, status,
                defaults != null ? defaults : null,
                invoice, invoiceDefault
        );
    }
}
