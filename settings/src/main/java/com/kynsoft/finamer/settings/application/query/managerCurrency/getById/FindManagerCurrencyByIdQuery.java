package com.kynsoft.finamer.settings.application.query.managerCurrency.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FindManagerCurrencyByIdQuery  implements IQuery {

    private final UUID id;

    public FindManagerCurrencyByIdQuery(UUID id) {
        this.id = id;
    }

}
