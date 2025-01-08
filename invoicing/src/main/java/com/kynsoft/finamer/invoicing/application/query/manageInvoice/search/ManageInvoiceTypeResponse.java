package com.kynsoft.finamer.invoicing.application.query.manageInvoice.search;

import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceTypeDto;
import com.kynsoft.finamer.invoicing.infrastructure.interfacesEntity.ManageInvoiceTypeProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ManageInvoiceTypeResponse {

    private UUID id;
    private String code;
    private String name;

    public ManageInvoiceTypeResponse(ManageInvoiceTypeDto invoiceType) {
        this.id = invoiceType.getId();
        this.code = invoiceType.getCode();
        this.name = invoiceType.getName();
    }

    public ManageInvoiceTypeResponse(ManageInvoiceTypeProjection manageInvoiceType) {
        this.id = manageInvoiceType.getId();
        this.code = manageInvoiceType.getCode();
        this.name = manageInvoiceType.getName();
    }
}
