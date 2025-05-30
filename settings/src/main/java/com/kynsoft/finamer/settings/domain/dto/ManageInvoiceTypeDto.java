package com.kynsoft.finamer.settings.domain.dto;

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
public class ManageInvoiceTypeDto {

    private UUID id;
    private String code;
    private String description;
    private Status status;
    private String name;
    private Boolean enabledToPolicy;
    private boolean income;
    private boolean credit;
    private boolean invoice;
}
