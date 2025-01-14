package com.kynsoft.finamer.insis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageHotelDto {
    private UUID id;
    private String code;
    private String name;
    private String status;
    private boolean deleted;
    private LocalDateTime updatedAt;
    private ManageTradingCompanyDto manageTradingCompany;
}
