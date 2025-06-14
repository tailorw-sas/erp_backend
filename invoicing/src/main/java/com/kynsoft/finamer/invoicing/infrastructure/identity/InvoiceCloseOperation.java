package com.kynsoft.finamer.invoicing.infrastructure.identity;

//import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
//import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kynsoft.finamer.invoicing.domain.dto.InvoiceCloseOperationDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invoice_close_operation")
//@EntityListeners(AuditEntityListener.class)
//@RemoteAudit(name = "invoice_close_operation",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class InvoiceCloseOperation implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(optional = false)  // Define la relación uno a uno
    @JoinColumn(name = "hotel_id", nullable = false, unique = true)  // Hace la relación obligatoria
    @JsonBackReference
    private ManageHotel hotel;

    private LocalDate beginDate;
    private LocalDate endDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public InvoiceCloseOperation(InvoiceCloseOperationDto dto) {
        this.id = dto.getId();
        this.status = dto.getStatus();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.beginDate = dto.getBeginDate() != null ? dto.getBeginDate() : null;
        this.endDate = dto.getEndDate() != null ? dto.getEndDate() : null;
    }

    public InvoiceCloseOperationDto toAggregate() {
        return new InvoiceCloseOperationDto(
                id, 
                status, 
                hotel != null ? hotel.toAggregate() : null, 
                beginDate, 
                endDate
        );
    }
}
