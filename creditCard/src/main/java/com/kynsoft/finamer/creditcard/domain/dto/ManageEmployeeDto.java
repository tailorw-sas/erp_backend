package com.kynsoft.finamer.creditcard.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageEmployeeDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private List<ManageAgencyDto> manageAgencyList;
    private List<ManageHotelDto> manageHotelList;
}