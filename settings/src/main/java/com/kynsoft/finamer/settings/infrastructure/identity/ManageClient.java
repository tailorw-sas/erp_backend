package com.kynsoft.finamer.settings.infrastructure.identity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kynsoft.finamer.settings.domain.dto.ManageAgencyDto;
import com.kynsoft.finamer.settings.domain.dto.ManageClientDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_client")
public class ManageClient implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(unique = true)
    private String code;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<ManageAgency> agencies;

    private Boolean isNightType;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updateAt;

    public ManageClient(ManageClientDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.agencies = dto.getAgencies() != null ? dto.getAgencies().stream().map(ManageAgency::new).toList() : null;
        this.isNightType = dto.getIsNightType();
    }

    public ManageClientDto toAggregate() {
        List<ManageAgencyDto> agencies = this.agencies != null ? this.agencies.stream().map(ManageAgency::toAggregateSample).toList() : null;
        ManageClientDto manageClientDto = new ManageClientDto(id, code, name, description, status, isNightType);
        manageClientDto.setAgencies(agencies);
        return manageClientDto;
    }



}