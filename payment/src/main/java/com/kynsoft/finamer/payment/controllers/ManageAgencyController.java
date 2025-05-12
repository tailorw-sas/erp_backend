package com.kynsoft.finamer.payment.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.query.manageAgency.getById.GetManageAgencyByIdQuery;
import com.kynsoft.finamer.payment.application.query.manageAgency.search.GetSearchAgencyQuery;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageAgencyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/agency")
public class ManageAgencyController {

    private final IMediator mediator;

    public ManageAgencyController(IMediator mediator) {

        this.mediator = mediator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID id){
        GetManageAgencyByIdQuery query = new GetManageAgencyByIdQuery(id);
        ManageAgencyResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchAgencyQuery query = new GetSearchAgencyQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }
}
