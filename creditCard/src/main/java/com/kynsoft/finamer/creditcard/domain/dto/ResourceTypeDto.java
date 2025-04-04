package com.kynsoft.finamer.creditcard.domain.dto;


import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResourceTypeDto {

    private UUID id;
    private String code;
    private String name;
    private boolean vcc;
    private Status status;
    private boolean defaults;
    private String description;
}
