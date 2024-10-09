package com.kynsoft.finamer.invoicing.application.query.manageAgencyContact;

import com.kynsoft.finamer.invoicing.domain.dto.ManageRegionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageAgencyContactRegionResponse {

    private UUID id;
    private String code;
    private String name;

    public ManageAgencyContactRegionResponse(ManageRegionDto dto){
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
    }
}