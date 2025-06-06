package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ManageLanguageDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_language")
public class ManageLanguage {
    @Id
    @Column(name = "id")
    private UUID id;

    private String code;

    private String name;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean defaults;

    private String status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    public ManageLanguage(ManageLanguageDto dto){
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.defaults = dto.getDefaults();
        this.status = dto.getStatus();
    }

    public ManageLanguageDto toAggregate(){
        return new ManageLanguageDto(id,
                code,
                name,
                defaults,
                status);
    }
}
