package com.kynsoft.finamer.settings.application.query.manageTradingCompanies.search;

import com.kynsof.share.core.domain.bus.query.IQuery;
import com.kynsof.share.core.domain.request.FilterCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchManageTradingCompaniesQuery implements IQuery {

    private Pageable pageable;
    private List<FilterCriteria> filter;
    private String query;
    private UUID employeeId;
}
