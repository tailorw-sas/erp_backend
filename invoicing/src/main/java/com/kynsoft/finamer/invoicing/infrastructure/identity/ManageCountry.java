package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kynsoft.finamer.invoicing.domain.dto.ManagerCountryDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_country")
public class ManageCountry implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(unique = true)
    private String code;

    private String name;
    private String description;

    private Boolean isDefault;

    @Enumerated(EnumType.STRING)
    private Status status;

    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_language_id")
    private ManageLanguage managerLanguage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updateAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deleteAt;


    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ManageAgency> agencies;

    private String iso3;

    public ManageCountry(ManagerCountryDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.isDefault = dto.getIsDefault();
        this.managerLanguage = dto.getManagerLanguage() != null ? new ManageLanguage(dto.getManagerLanguage()) : null;
        this.iso3 = dto.getIso3();
    }

    public ManagerCountryDto toAggregate() {
        return new ManagerCountryDto(
                id, 
                code, 
                name, 
                description,
                isDefault,
                status,
                managerLanguage.toAggregate(),
                iso3
        );
    }

}