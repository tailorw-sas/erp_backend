package com.kynsoft.finamer.payment.domain.dto;

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
public class ManageAgencyTypeDto implements Serializable {

    private UUID id;
    private String code;
    private String status;
    private String name;
}
