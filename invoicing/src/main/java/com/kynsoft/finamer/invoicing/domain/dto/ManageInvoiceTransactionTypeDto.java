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
public class ManageInvoiceTransactionTypeDto implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private boolean defaults;
    private boolean cloneAdjustmentDefault;
    private Boolean negative;
}
