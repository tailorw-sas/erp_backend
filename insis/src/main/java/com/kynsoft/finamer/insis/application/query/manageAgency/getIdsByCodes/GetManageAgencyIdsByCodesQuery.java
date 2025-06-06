package com.kynsoft.finamer.insis.application.query.manageAgency.getIdsByCodes;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetManageAgencyIdsByCodesQuery implements IQuery {
    private List<String> codes;
}
