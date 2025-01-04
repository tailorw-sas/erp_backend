package com.kynsoft.finamer.creditcard.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.resourceType.create.CreateManageResourceTypeCommand;
import com.kynsoft.finamer.creditcard.application.command.resourceType.create.CreateManageResourceTypeMessage;
import com.kynsoft.finamer.creditcard.application.command.resourceType.create.CreateManageResourceTypeRequest;
import com.kynsoft.finamer.creditcard.application.command.resourceType.delete.DeleteManageResourceTypeCommand;
import com.kynsoft.finamer.creditcard.application.command.resourceType.delete.DeleteManageResourceTypeMessage;
import com.kynsoft.finamer.creditcard.application.command.resourceType.update.UpdateManageResourceTypeCommand;
import com.kynsoft.finamer.creditcard.application.command.resourceType.update.UpdateManageResourceTypeMessage;
import com.kynsoft.finamer.creditcard.application.command.resourceType.update.UpdateManageResourceTypeRequest;
import com.kynsoft.finamer.creditcard.application.query.resourceType.getById.FindResourceTypeByIdQuery;
import com.kynsoft.finamer.creditcard.application.query.resourceType.search.GetSearchResourceTypeQuery;
import com.kynsoft.finamer.creditcard.application.query.resourceType.search.GetSearchResourceTypeResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/resource-type")
public class ResourceTypeController {

    private final IMediator mediator;

    public ResourceTypeController(IMediator mediator) {

        this.mediator = mediator;
    }

    @PostMapping()
    public ResponseEntity<CreateManageResourceTypeMessage> create(@RequestBody CreateManageResourceTypeRequest request) {
        CreateManageResourceTypeCommand createCommand = CreateManageResourceTypeCommand.fromRequest(request, UUID.randomUUID());
        CreateManageResourceTypeMessage response = mediator.send(createCommand);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {

        DeleteManageResourceTypeCommand command = new DeleteManageResourceTypeCommand(id);
        DeleteManageResourceTypeMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateManageResourceTypeRequest request) {

        UpdateManageResourceTypeCommand command = UpdateManageResourceTypeCommand.fromRequest(request, id);
        UpdateManageResourceTypeMessage response = mediator.send(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        FindResourceTypeByIdQuery query = new FindResourceTypeByIdQuery(id);
        GetSearchResourceTypeResponse response = mediator.send(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);
        GetSearchResourceTypeQuery query = new GetSearchResourceTypeQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }
}
