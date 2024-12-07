package com.kynsoft.finamer.invoicing.application.query.manageAgency.search;

import com.kynsof.share.core.domain.bus.query.IQuery;
import com.kynsof.share.core.domain.request.FilterCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GetSearchManageAgencyQuery implements IQuery {

    private Pageable pageable;
    private List<FilterCriteria> filter;
    private String query;
}
