package com.kynsoft.finamer.creditcard.controllers;

import com.kynsof.share.core.domain.request.PageableUtil;
import com.kynsof.share.core.domain.request.SearchRequest;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransaction.update.UpdateManageStatusTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransaction.update.UpdateManageStatusTransactionCommandMessage;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransaction.update.UpdateManageStatusTransactionCommandRequest;
import com.kynsoft.finamer.creditcard.application.command.resendPost.ResendPostCommand;
import com.kynsoft.finamer.creditcard.application.command.resendPost.ResendPostMessage;
import com.kynsoft.finamer.creditcard.application.command.resendPost.ResendPostRequest;
import com.kynsoft.finamer.creditcard.application.command.transaction.adjustment.CreateAdjustmentTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.transaction.adjustment.CreateAdjustmentTransactionMessage;
import com.kynsoft.finamer.creditcard.application.command.transaction.adjustment.CreateAdjustmentTransactionRequest;
import com.kynsoft.finamer.creditcard.application.command.manageRedirect.CreateRedirectCommand;
import com.kynsoft.finamer.creditcard.application.command.manageRedirect.CreateRedirectCommandMessage;
import com.kynsoft.finamer.creditcard.application.command.manageRedirectTransactionPayment.CreateRedirectTransactionPaymentCommand;
import com.kynsoft.finamer.creditcard.application.command.manageRedirectTransactionPayment.CreateRedirectTransactionPaymentCommandMessage;
import com.kynsoft.finamer.creditcard.application.command.manageRedirectTransactionPayment.CreateRedirectTransactionPaymentRequest;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransactionBlue.update.UpdateManageStatusTransactionBlueCommand;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransactionBlue.update.UpdateManageStatusTransactionBlueCommandMessage;
import com.kynsoft.finamer.creditcard.application.command.manageStatusTransactionBlue.update.UpdateManageStatusTransactionBlueCommandRequest;
import com.kynsoft.finamer.creditcard.application.command.transaction.manual.CreateManualTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.transaction.manual.CreateManualTransactionMessage;
import com.kynsoft.finamer.creditcard.application.command.transaction.manual.CreateManualTransactionRequest;
import com.kynsoft.finamer.creditcard.application.command.transaction.refund.CreateRefundTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.transaction.refund.CreateRefundTransactionMessage;
import com.kynsoft.finamer.creditcard.application.command.transaction.refund.CreateRefundTransactionRequest;
import com.kynsoft.finamer.creditcard.application.command.sendMail.SendMailCommand;
import com.kynsoft.finamer.creditcard.application.command.sendMail.SendMailMessage;
import com.kynsoft.finamer.creditcard.application.command.sendMail.SendMailRequest;
import com.kynsoft.finamer.creditcard.application.command.transaction.update.UpdateTransactionCommand;
import com.kynsoft.finamer.creditcard.application.command.transaction.update.UpdateTransactionMessage;
import com.kynsoft.finamer.creditcard.application.command.transaction.update.UpdateTransactionRequest;
import com.kynsoft.finamer.creditcard.application.query.manageMerchant.getById.FindManageMerchantByIdQuery;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.ManageMerchantResponse;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.TransactionResponse;
import com.kynsoft.finamer.creditcard.application.query.transaction.getById.FindTransactionByIdQuery;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.GetSearchTransactionQuery;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.TransactionResumeResponse;
import com.kynsoft.finamer.creditcard.domain.dto.PaymentRequestDto;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final IMediator mediator;

    public TransactionController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping("/manual")
    public ResponseEntity<?> createManual(@RequestBody CreateManualTransactionRequest request) {
        CreateManualTransactionCommand command = CreateManualTransactionCommand.fromRequest(request);
        CreateManualTransactionMessage response = mediator.send(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/adjustment")
    public ResponseEntity<?> createAdjustment(@RequestBody CreateAdjustmentTransactionRequest request) {
        CreateAdjustmentTransactionCommand command = CreateAdjustmentTransactionCommand.fromRequest(request);
        CreateAdjustmentTransactionMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<?> createRefund(@RequestBody CreateRefundTransactionRequest request) {
        CreateRefundTransactionCommand command = CreateRefundTransactionCommand.fromRequest(request);
        CreateRefundTransactionMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        FindTransactionByIdQuery query = new FindTransactionByIdQuery(id);
        TransactionResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@AuthenticationPrincipal Jwt jwt, @RequestBody SearchRequest request) {
        Pageable pageable = PageableUtil.createPageable(request);

        String userId = jwt.getClaim("sub");
        UUID employeeId = UUID.fromString(userId);
        //UUID employeeId = UUID.fromString("637ee5cb-1e36-4917-a0a9-5874bc8bea04");

        GetSearchTransactionQuery query = new GetSearchTransactionQuery(pageable, request.getFilter(), request.getQuery(), employeeId);
        TransactionResumeResponse response = mediator.send(query);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend/email/payment-link")
    public ResponseEntity<?> send(@RequestBody SendMailRequest request) {
        SendMailCommand command = SendMailCommand.fromRequest(request);
        SendMailMessage response = this.mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-post")
    public ResponseEntity<?> resendPost(@RequestBody ResendPostRequest request) {
        ResendPostCommand command = ResendPostCommand.fromRequest(request);
        ResendPostMessage response = this.mediator.send(command);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/redirectTypeLink")
    public ResponseEntity<?> getPaymentForm(@RequestBody CreateRedirectTransactionPaymentRequest request) {

        CreateRedirectTransactionPaymentCommand redirectTransactionPaymentCommand = CreateRedirectTransactionPaymentCommand.builder()
                .Token(request.getToken())
                .build();
        CreateRedirectTransactionPaymentCommandMessage createRedirectCommandMessage = mediator.send(redirectTransactionPaymentCommand);
        return ResponseEntity.ok(createRedirectCommandMessage);
    }

    @PostMapping("/redirectTypePost")
    public ResponseEntity<?> getPaymentPostForm(@RequestBody PaymentRequestDto requestDto) {
        FindManageMerchantByIdQuery query = new FindManageMerchantByIdQuery(requestDto.getMerchantId());
        ManageMerchantResponse response = mediator.send(query);
        CreateRedirectCommand redirectCommand = CreateRedirectCommand.builder()
                .manageMerchantResponse(response)
                .requestDto(requestDto)
                .build();
        CreateRedirectCommandMessage createRedirectCommandMessage = mediator.send(redirectCommand);
        return ResponseEntity.ok(createRedirectCommandMessage);
    }

    @PostMapping("/processMerchantCardNetResponse")
    public ResponseEntity<?> processMerchantCardNetResponse(@RequestBody UpdateManageStatusTransactionCommandRequest request) {
        UpdateManageStatusTransactionCommand updateManageStatusTransactionCommandRequest = UpdateManageStatusTransactionCommand.builder()
                .session(request.getSession())
                .employee(request.getEmployee())
                .employeeId(request.getEmployeeId())
                .build();

        UpdateManageStatusTransactionCommandMessage response = mediator.send(updateManageStatusTransactionCommandRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/processMerchantBlueResponse")
    public ResponseEntity<?> processMerchantBlueResponse(@RequestBody UpdateManageStatusTransactionBlueCommandRequest request) {
        UpdateManageStatusTransactionBlueCommand updateManageStatusTransactionBlueCommand = UpdateManageStatusTransactionBlueCommand.builder()
                .request(request)
                .build();

        UpdateManageStatusTransactionBlueCommandMessage response = mediator.send(updateManageStatusTransactionBlueCommand);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateTransactionRequest request) {

        UpdateTransactionCommand command = UpdateTransactionCommand.fromRequest(request, id);
        UpdateTransactionMessage response = mediator.send(command);

        return ResponseEntity.ok(response);
    }

}
