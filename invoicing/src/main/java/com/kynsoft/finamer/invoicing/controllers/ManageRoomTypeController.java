package com.kynsoft.finamer.invoicing.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.query.manageRoomType.getByCodeAndHotelCode.FindManageRoomTypeByCodeAndHotelCodeQuery;
import com.kynsoft.finamer.invoicing.application.query.manageRoomType.search.GetSearchManageRoomTypeQuery;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.ManageRoomTypeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manage-room-type")
public class ManageRoomTypeController {

    private final IMediator mediator;

    public ManageRoomTypeController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchManageRoomTypeQuery query = new GetSearchManageRoomTypeQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/code/{code}/hotel-code/{hotelCode}")
    public ResponseEntity<?> getById(@PathVariable String code, @PathVariable String hotelCode) {

        FindManageRoomTypeByCodeAndHotelCodeQuery query = new FindManageRoomTypeByCodeAndHotelCodeQuery(code, hotelCode);
        ManageRoomTypeResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

}
