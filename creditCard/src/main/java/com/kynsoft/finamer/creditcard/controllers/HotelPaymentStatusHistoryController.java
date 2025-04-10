package com.kynsoft.finamer.creditcard.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.query.hotelPaymentStatusHistory.getById.FindHotelPaymentStatusHistoryByIdQuery;
import com.kynsoft.finamer.creditcard.application.query.hotelPaymentStatusHistory.search.GetSearchHotelPaymentStatusHistoryQuery;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.HotelPaymentStatusHistoryResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hotel-payment-status-history")
public class HotelPaymentStatusHistoryController {

    private final IMediator mediator;

    public HotelPaymentStatusHistoryController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        FindHotelPaymentStatusHistoryByIdQuery query = new FindHotelPaymentStatusHistoryByIdQuery(id);
        HotelPaymentStatusHistoryResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchHotelPaymentStatusHistoryQuery query = new GetSearchHotelPaymentStatusHistoryQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }
}
