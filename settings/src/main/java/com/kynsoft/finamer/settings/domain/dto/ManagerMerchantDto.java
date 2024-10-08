package com.kynsoft.finamer.settings.domain.dto;

import com.kynsoft.finamer.settings.application.query.objectResponse.ManagerMerchantConfigResponse;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerMerchantDto {

    private UUID id;
    private String code;
    private String description;
    private ManagerB2BPartnerDto b2bPartner;
    private Boolean defaultm;
    private Status status;

}
