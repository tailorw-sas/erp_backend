package com.kynsoft.finamer.settings.domain.dto;

import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagerB2BPartnerDto implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private Status status;
    private String url;
    private String ip;
    private String userName;
    private String password;
    private String token;
    private ManageB2BPartnerTypeDto b2BPartnerTypeDto;

}
