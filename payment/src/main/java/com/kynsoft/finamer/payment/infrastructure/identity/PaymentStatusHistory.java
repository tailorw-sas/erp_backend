package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.PaymentStatusHistoryDto;
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
@Table(name = "payment_attachment_status_history")
public class PaymentStatusHistory implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private ManageEmployee employee;

    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public PaymentStatusHistory(PaymentStatusHistoryDto dto) {
        this.id = dto.getId();
        this.payment = dto.getPayment() != null ? new Payment(dto.getPayment()) : null;
        this.employee = dto.getEmployee() != null ? new ManageEmployee(dto.getEmployee()) : null;
        this.description = dto.getDescription();
        this.status = dto.getStatus();
    }

    public PaymentStatusHistoryDto toAggregate(){
        return new PaymentStatusHistoryDto(
                id, 
                status, 
                payment != null ? payment.toAggregate() : null, 
                employee != null ? employee.toAggregate() : null, 
                description, 
                createdAt, 
                updatedAt
        );
    }
}
