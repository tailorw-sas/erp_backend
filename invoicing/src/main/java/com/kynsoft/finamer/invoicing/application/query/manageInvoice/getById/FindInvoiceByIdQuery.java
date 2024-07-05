package com.kynsoft.finamer.invoicing.application.query.manageInvoice.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FindInvoiceByIdQuery  implements IQuery {

    private final UUID id;

    public FindInvoiceByIdQuery(UUID id) {
        this.id = id;
    }

}
