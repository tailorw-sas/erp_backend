package com.tailorw.tcaInnsist.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageHotelDto {
    private UUID id;
    private String code;
    private String name;
    private String roomType;
    private UUID tradingCompanyId;
}
