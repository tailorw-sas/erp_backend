package com.kynsoft.finamer.creditcard.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.manualTransaction.create.CreateManualTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.manualTransaction.create.CreateManualTransactionMessage;
import com.kynsoft.finamer.creditcard.application.command.manualTransaction.create.CreateManualTransactionRequest;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.TransactionResponse;
import com.kynsoft.finamer.creditcard.application.query.transaction.getById.FindTransactionByIdQuery;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.GetSearchTransactionQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final IMediator mediator;

    public TransactionController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/manual")
    public ResponseEntity<?> create(@RequestBody CreateManualTransactionRequest request){
        CreateManualTransactionCommand command = CreateManualTransactionCommand.fromRequest(request);
        CreateManualTransactionMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        FindTransactionByIdQuery query = new FindTransactionByIdQuery(id);
        TransactionResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchTransactionQuery query = new GetSearchTransactionQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }
}
