package com.kynsoft.finamer.insis.application.query.manageTradingCompany.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetManageTradingCompanyByIdQuery implements IQuery {
    private UUID id;
}
