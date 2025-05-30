package com.kynsoft.finamer.invoicing.controllers;

import com.kynsof.share.core.domain.http.entity.InvoiceHttp;
import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.create.CreateInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.create.CreateInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.create.CreateInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.createBulk.CreateBulkInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.createBulk.CreateBulkInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.createBulk.CreateBulkInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.delete.DeleteInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.delete.DeleteInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.newCredit.CreateNewCreditCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.newCredit.CreateNewCreditMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.newCredit.CreateNewCreditRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.partialClone.PartialCloneInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.partialClone.PartialCloneInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.partialClone.PartialCloneInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileManual.ReconcileManualCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileManual.ReconcileManualMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileManual.ReconcileManualRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.send.SendInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.send.SendInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.send.SendInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.totalClone.TotalCloneCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.totalClone.TotalCloneMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.totalClone.TotalCloneRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.undoImportInvoice.UndoImportInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.undoImportInvoice.UndoImportInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.undoImportInvoice.UndoImportInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.update.UpdateInvoiceCommand;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.update.UpdateInvoiceMessage;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.update.UpdateInvoiceRequest;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.update.originalAmount.UpdateInvoiceOriginalAmountCommand;
import com.kynsoft.finamer.invoicing.application.query.invoice.http.getById.FindInvoiceHttpByIdQuery;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.export.ExportInvoiceQuery;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.export.PaymentExcelExporterResponse;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.getById.FindInvoiceByIdQuery;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.search.GetSearchInvoiceQuery;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.sendList.SendListInvoiceQuery;
import com.kynsoft.finamer.invoicing.application.query.manageInvoice.toPayment.search.GetSearchInvoiceToPaymentQuery;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.ExportInvoiceResponse;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.ManageInvoiceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/manage-invoice")
public class InvoiceController {

    private final IMediator mediator;
    public InvoiceController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping(path = "/uuid-id/{id}")
    public ResponseEntity<?> getByUuidId(@PathVariable UUID id) {

        FindInvoiceHttpByIdQuery query = new FindInvoiceHttpByIdQuery(id);
        InvoiceHttp response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<CreateInvoiceMessage> create(@RequestBody CreateInvoiceRequest request) {
        CreateInvoiceCommand createCommand = CreateInvoiceCommand.fromRequest(request);
        CreateInvoiceMessage response = mediator.send(createCommand);

        FindInvoiceByIdQuery query = new FindInvoiceByIdQuery(response.getId());
        ManageInvoiceResponse resp = mediator.send(query);

        this.mediator.send(
                new UpdateInvoiceCommand(response.getId(), null, resp.getAgency().getId(), null, null));
        return ResponseEntity.ok(response);
    }

    @PostMapping("bulk")
    public ResponseEntity<CreateBulkInvoiceMessage> createBulk(@RequestBody CreateBulkInvoiceRequest request) {
        CreateBulkInvoiceCommand command = CreateBulkInvoiceCommand.fromRequest(request);
        CreateBulkInvoiceMessage message = this.mediator.send(command);

        FindInvoiceByIdQuery query = new FindInvoiceByIdQuery(message.getId());
        ManageInvoiceResponse resp = mediator.send(query);

        this.mediator.send(new UpdateInvoiceOriginalAmountCommand(resp.getId(), resp.getInvoiceAmount()));
        return ResponseEntity.ok(message);

    }

    @PostMapping("total-clone-invoice")
    public ResponseEntity<?> totalClone(@RequestBody TotalCloneRequest request) {

        TotalCloneCommand command = TotalCloneCommand.fromRequest(request, mediator);

        TotalCloneMessage message = this.mediator.send(command);

        return ResponseEntity.ok(message);

    }

    @PostMapping("partial-clone")
    public ResponseEntity<PartialCloneInvoiceMessage> createBulk(@RequestBody PartialCloneInvoiceRequest request) {

        PartialCloneInvoiceCommand command = PartialCloneInvoiceCommand.fromRequest(request);

        PartialCloneInvoiceMessage message = this.mediator.send(command);

        return ResponseEntity.ok(message);

    }

    @PostMapping("/undo")
    public ResponseEntity<UndoImportInvoiceMessage> createBulk(@RequestBody UndoImportInvoiceRequest request) {

        UndoImportInvoiceCommand command = UndoImportInvoiceCommand.fromRequest(request, mediator);

        UndoImportInvoiceMessage message = this.mediator.send(command);

        return ResponseEntity.ok(message);

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {

        FindInvoiceByIdQuery query = new FindInvoiceByIdQuery(id);
        ManageInvoiceResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {

        DeleteInvoiceCommand command = new DeleteInvoiceCommand(id);
        DeleteInvoiceMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@AuthenticationPrincipal Jwt jwt, @RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        String userId = jwt.getClaim("sub");
        UUID employeeId = UUID.fromString(userId);
        GetSearchInvoiceQuery query = new GetSearchInvoiceQuery(pageable, request.getFilter(), request.getQuery(), employeeId);
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/send-list")
    public ResponseEntity<?> send(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        SendListInvoiceQuery query = new SendListInvoiceQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/search-payment/search")
    public ResponseEntity<?> searchToPayment(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        GetSearchInvoiceToPaymentQuery query = new GetSearchInvoiceToPaymentQuery(pageable, request.getFilter(), request.getQuery());
        PaginatedResponse data = mediator.send(query);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        ExportInvoiceQuery query = new ExportInvoiceQuery(pageable, request.getFilter(), request.getQuery());
        ExportInvoiceResponse data = mediator.send(query);

        final byte[] bytes = data.getStream().toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PostMapping("/export-base64")
    public ResponseEntity<?> exportBase64(@RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        ExportInvoiceQuery query = new ExportInvoiceQuery(pageable, request.getFilter(), request.getQuery());
        ExportInvoiceResponse data = mediator.send(query);

        final byte[] bytes = data.getStream().toByteArray();
        PaymentExcelExporterResponse response = new PaymentExcelExporterResponse(bytes, "file");

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody UpdateInvoiceRequest request) {

        UpdateInvoiceCommand command = UpdateInvoiceCommand.fromRequest(request, id);
        UpdateInvoiceMessage response = mediator.send(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new-credit")
    public ResponseEntity<?> newCredit(@RequestBody CreateNewCreditRequest request) {
        CreateNewCreditCommand command = CreateNewCreditCommand.fromRequest(request);
        CreateNewCreditMessage response = this.mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody SendInvoiceRequest request) {
        SendInvoiceCommand command = SendInvoiceCommand.fromRequest(request);
        SendInvoiceMessage response = this.mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reconcile-manual")
    public ResponseEntity<?> generateInvoicePdf(@RequestBody ReconcileManualRequest request) {

        //Realizar el reconcile manual
        ReconcileManualCommand command = ReconcileManualCommand.fromRequest(request);
        ReconcileManualMessage response = this.mediator.send(command);

        return ResponseEntity.ok(response);
    }
}
