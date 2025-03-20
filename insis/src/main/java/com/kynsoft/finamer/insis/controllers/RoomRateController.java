package com.kynsoft.finamer.insis.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.insis.application.query.objectResponse.roomRate.GetCountRoomRateByHotelInvoiceDateRequest;
import com.kynsoft.finamer.insis.application.query.roomRate.countByHotelInvoiceDate.GetCountRoomRateByHotelInvoiceDateQuery;
import com.kynsoft.finamer.insis.application.query.roomRate.search.GetSearchRoomRateQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/room-rate")
public class RoomRateController {

    private final IMediator mediator;

    public RoomRateController(IMediator mediator){
        this.mediator = mediator;
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request){
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchRoomRateQuery query = new GetSearchRoomRateQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("count")
    public ResponseEntity<?> getCountByHotelAndInvoiceDate(@AuthenticationPrincipal Jwt jwt, @RequestBody GetCountRoomRateByHotelInvoiceDateRequest request){
        String userId = jwt.getClaim("sub");
        UUID employeeId = UUID.fromString(userId);

        GetCountRoomRateByHotelInvoiceDateQuery query = new GetCountRoomRateByHotelInvoiceDateQuery(employeeId, request.getFromInvoiceDate(), request.getToInvoiceDate());
        PaginatedResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }
}
