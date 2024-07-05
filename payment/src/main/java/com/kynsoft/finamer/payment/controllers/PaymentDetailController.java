package com.kynsoft.finamer.payment.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.paymentDetail.create.CreatePaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.create.CreatePaymentDetailMessage;
import com.kynsoft.finamer.payment.application.command.paymentDetail.create.CreatePaymentDetailRequest;
import com.kynsoft.finamer.payment.application.command.paymentDetail.delete.DeletePaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.delete.DeletePaymentDetailMessage;
import com.kynsoft.finamer.payment.application.command.paymentDetail.update.UpdatePaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.update.UpdatePaymentDetailMessage;
import com.kynsoft.finamer.payment.application.command.paymentDetail.update.UpdatePaymentDetailRequest;
import com.kynsoft.finamer.payment.application.command.paymentDetailApplyDeposit.create.CreatePaymentDetailApplyDepositCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetailApplyDeposit.create.CreatePaymentDetailApplyDepositMessage;
import com.kynsoft.finamer.payment.application.command.paymentDetailApplyDeposit.create.CreatePaymentDetailApplyDepositRequest;
import com.kynsoft.finamer.payment.application.query.objectResponse.PaymentDetailResponse;
import com.kynsoft.finamer.payment.application.query.paymentDetail.getById.FindPaymentDetailByIdQuery;
import com.kynsoft.finamer.payment.application.query.paymentDetail.search.GetSearchPaymentDetailQuery;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-detail")
public class PaymentDetailController {

    private final IMediator mediator;

    public PaymentDetailController(IMediator mediator) {

        this.mediator = mediator;
    }

    @PostMapping()
    public ResponseEntity<CreatePaymentDetailMessage> create(@RequestBody CreatePaymentDetailRequest request) {
        CreatePaymentDetailCommand createCommand = CreatePaymentDetailCommand.fromRequest(request);
        CreatePaymentDetailMessage response = mediator.send(createCommand);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply-deposit")
    public ResponseEntity<CreatePaymentDetailApplyDepositMessage> create(@RequestBody CreatePaymentDetailApplyDepositRequest request) {
        CreatePaymentDetailApplyDepositCommand createCommand = CreatePaymentDetailApplyDepositCommand.fromRequest(request);
        CreatePaymentDetailApplyDepositMessage response = mediator.send(createCommand);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {

        DeletePaymentDetailCommand command = new DeletePaymentDetailCommand(id);
        DeletePaymentDetailMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdatePaymentDetailRequest request) {

        UpdatePaymentDetailCommand command = UpdatePaymentDetailCommand.fromRequest(request, id);
        UpdatePaymentDetailMessage response = mediator.send(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {

        FindPaymentDetailByIdQuery query = new FindPaymentDetailByIdQuery(id);
        PaymentDetailResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchPaymentDetailQuery query = new GetSearchPaymentDetailQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }
}
