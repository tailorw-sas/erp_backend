package com.kynsoft.finamer.invoicing.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.query.manageTradingCompanies.getByCode.FindTradingCompaniesByIdQuery;
import com.kynsoft.finamer.invoicing.application.query.manageTradingCompanies.search.GetSearchManageTradingCompaniesQuery;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.ManageTraidingCompaniesResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-trading-companies")
public class ManageTradingCompaniesController {

    private final IMediator mediator;

    public ManageTradingCompaniesController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchManageTradingCompaniesQuery query = new GetSearchManageTradingCompaniesQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<?> getById(@PathVariable String code) {

        FindTradingCompaniesByIdQuery query = new FindTradingCompaniesByIdQuery(code);
        ManageTraidingCompaniesResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

}
