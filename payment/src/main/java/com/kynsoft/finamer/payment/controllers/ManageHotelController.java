package com.kynsoft.finamer.payment.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.query.manageHotel.getById.GetManageHotelByIdQuery;
import com.kynsoft.finamer.payment.application.query.manageHotel.search.GetSearchManageHotelQuery;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageHotelResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/manage-hotel")
public class ManageHotelController {

    private final IMediator mediator;

    public ManageHotelController(IMediator mediator) {

        this.mediator = mediator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID id){
        GetManageHotelByIdQuery query = new GetManageHotelByIdQuery(id);
        ManageHotelResponse response = this.mediator.send(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchManageHotelQuery query = new GetSearchManageHotelQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }
}
