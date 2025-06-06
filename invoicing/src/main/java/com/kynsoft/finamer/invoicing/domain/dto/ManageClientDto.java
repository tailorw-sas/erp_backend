package com.kynsoft.finamer.invoicing.domain.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageClientDto implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private Boolean isNightType;
    private String status;
}
