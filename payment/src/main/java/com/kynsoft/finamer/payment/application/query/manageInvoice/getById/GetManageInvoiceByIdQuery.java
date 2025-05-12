package com.kynsoft.finamer.payment.application.query.manageInvoice.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GetManageInvoiceByIdQuery implements IQuery {

    private final UUID id;

    public GetManageInvoiceByIdQuery(UUID id){
        this.id = id;
    }
}
