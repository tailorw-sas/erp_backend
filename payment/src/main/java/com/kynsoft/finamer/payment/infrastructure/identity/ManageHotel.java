package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "manage_hotel")
public class ManageHotel implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    private String code;

    @Column(nullable = true)
    private Boolean deleted = false;

    private String name;
    private String status;
    @Column(nullable = true)
    private Boolean applyByTradingCompany;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    public ManageHotel(ManageHotelDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.applyByTradingCompany = dto.getApplyByTradingCompany();
        this.name = dto.getName();
        this.status = dto.getStatus();
    }

    public ManageHotelDto toAggregate() {
        return new ManageHotelDto(
                id, code, name, status, applyByTradingCompany
        );
    }
}
