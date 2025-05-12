package com.kynsoft.finamer.payment.controllers;

import com.kynsof.share.core.domain.http.entity.BookingHttp;
import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.query.http.invoice.booking.genId.FindBookingByGenIdQuery;
import com.kynsoft.finamer.payment.application.query.http.invoice.booking.uuid.FindBookingByUUIDQuery;
import com.kynsoft.finamer.payment.application.query.manageBooking.getByGenId.GetBookingByGenIdQuery;
import com.kynsoft.finamer.payment.application.query.manageBooking.getById.GetManageBookingByIdQuery;
import com.kynsoft.finamer.payment.application.query.manageBooking.getByListIds.GetBookingByListIdsQuery;
import com.kynsoft.finamer.payment.application.query.manageBooking.search.GetSearchManageBookingQuery;
import com.kynsoft.finamer.payment.application.query.objectResponse.BookingProjectionResponse;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageBookingResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manage-booking")
public class ManageBookingController {

    private final IMediator mediator;

    public ManageBookingController(IMediator mediator) {

        this.mediator = mediator;
    }
    
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchManageBookingQuery query = new GetSearchManageBookingQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }

    @GetMapping(path = "/gen-id/{id}")
    public ResponseEntity<?> getByGenId(@PathVariable Long id) {

        FindBookingByGenIdQuery query = new FindBookingByGenIdQuery(id, mediator);
        BookingHttp response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/uuid/{id}")
    public ResponseEntity<?> getByUUID(@PathVariable UUID id) {

        FindBookingByUUIDQuery query = new FindBookingByUUIDQuery(id, mediator);
        BookingHttp response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") UUID id){
        GetManageBookingByIdQuery query = new GetManageBookingByIdQuery(id);
        ManageBookingResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projection/{id}")
    public ResponseEntity<?> getByGenProjection(@PathVariable long id) {

        GetBookingByGenIdQuery query = new GetBookingByGenIdQuery(id);
        BookingProjectionResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projection-all/{id}")
    public ResponseEntity<?> getByGenProjectionAll(@PathVariable long id) {

        GetBookingByListIdsQuery query = new GetBookingByListIdsQuery(id);
        ManageBookingResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

}
