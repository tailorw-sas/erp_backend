package com.kynsoft.finamer.insis.domain.dto;

import jakarta.persistence.Column;
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
public class ManageB2BPartnerTypeDto {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private LocalDateTime updatedAt;
}
