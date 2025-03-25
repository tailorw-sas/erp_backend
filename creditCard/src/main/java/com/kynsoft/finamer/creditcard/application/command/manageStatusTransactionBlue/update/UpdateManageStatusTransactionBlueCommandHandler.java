package com.kynsoft.finamer.creditcard.application.command.manageStatusTransactionBlue.update;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.exception.BusinessException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.utils.BankerRounding;
import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.services.*;
import com.kynsoft.finamer.creditcard.infrastructure.services.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateManageStatusTransactionBlueCommandHandler implements ICommandHandler<UpdateManageStatusTransactionBlueCommand> {

    private final ITransactionService transactionService;
    private final ManageCreditCardTypeServiceImpl creditCardTypeService;
    private final ManageTransactionStatusServiceImpl transactionStatusService;
    private final TransactionPaymentLogsService transactionPaymentLogsService;
    private final ManageMerchantCommissionServiceImpl merchantCommissionService;
    private final IParameterizationService parameterizationService;
    private final IProcessErrorLogService processErrorLogService;
    private final ITransactionStatusHistoryService transactionStatusHistoryService;
    private final IManageMerchantConfigService merchantConfigService;
    private final IVoucherService voucherService;

    public UpdateManageStatusTransactionBlueCommandHandler(ITransactionService transactionService, ManageCreditCardTypeServiceImpl creditCardTypeService,
                                                           ManageTransactionStatusServiceImpl transactionStatusService,
                                                           TransactionPaymentLogsService transactionPaymentLogsService,
                                                           ManageMerchantCommissionServiceImpl merchantCommissionService, IParameterizationService parameterizationService, IProcessErrorLogService processErrorLogService, ITransactionStatusHistoryService transactionStatusHistoryService, IManageMerchantConfigService merchantConfigService, IVoucherService voucherService) {
        this.transactionService = transactionService;
        this.creditCardTypeService = creditCardTypeService;
        this.transactionStatusService = transactionStatusService;
        this.transactionPaymentLogsService = transactionPaymentLogsService;
        this.merchantCommissionService = merchantCommissionService;
        this.parameterizationService = parameterizationService;
        this.processErrorLogService = processErrorLogService;
        this.transactionStatusHistoryService = transactionStatusHistoryService;
        this.merchantConfigService = merchantConfigService;
        this.voucherService = voucherService;
    }

    @Override
    public void handle(UpdateManageStatusTransactionBlueCommand command) {
        TransactionDto transactionDto = transactionService.findById(command.getRequest().getOrderNumber());
        if (!transactionDto.getStatus().isSentStatus() && !transactionDto.getStatus().isDeclinedStatus()) {
            throw new BusinessException(DomainErrorMessage.MANAGE_TRANSACTION_ALREADY_PROCESSED, DomainErrorMessage.MANAGE_TRANSACTION_ALREADY_PROCESSED.getReasonPhrase());
        }
        ManageCreditCardTypeDto creditCardTypeDto = creditCardTypeService.findByFirstDigit(Character.getNumericValue(command.getRequest().getCardNumber().charAt(0)));
        ManageTransactionStatusDto transactionStatusDto = transactionStatusService.findByMerchantResponseStatus(command.getRequest().getStatus());
        TransactionPaymentLogsDto transactionPaymentLogsDto = transactionPaymentLogsService.findByTransactionId(transactionDto.getTransactionUuid());
        ParameterizationDto parameterizationDto = this.parameterizationService.findActiveParameterization();

        // Solo calcular la comission si es Received
        if (transactionStatusDto.isReceivedStatus()) {
            int decimals = parameterizationDto != null ? parameterizationDto.getDecimals() : 2;

            double commission= 0.0;
            try {
                commission = merchantCommissionService.calculateCommission(transactionDto.getAmount(), transactionDto.getMerchant().getId(), creditCardTypeDto.getId(), transactionDto.getCheckIn().toLocalDate(), decimals);
            } catch (Exception e) {
                ProcessErrorLogDto processErrorLogDto = new ProcessErrorLogDto();
                processErrorLogDto.setTransactionId(transactionDto.getTransactionUuid());
                processErrorLogDto.setError(e.getMessage());
                this.processErrorLogService.create(processErrorLogDto);
            }
            //independientemente del valor de la commission el netAmount tiene dos decimales
            double netAmount = BankerRounding.round(transactionDto.getAmount() - commission, 2);
            transactionDto.setCommission(commission);
            transactionDto.setNetAmount(netAmount);
        }

        //si no encuentra la parametrization que agarre 2 decimales por defecto


        //Comenzar a actualizar lo referente a la transaccion en las diferntes tablas
        //1- Actualizar data in vcc_transaction
        transactionDto.setCardNumber(command.getRequest().getCardNumber());
        transactionDto.setCreditCardType(creditCardTypeDto);
        transactionDto.setPaymentDate(command.getRequest().getPaymentDate());
        if (transactionStatusDto.isReceivedStatus()){
            transactionDto.setTransactionDate(LocalDateTime.now());
        }
        if (!transactionStatusDto.equals(transactionDto.getStatus())){
            transactionDto.setStatus(transactionStatusDto);
            this.transactionStatusHistoryService.create(transactionDto, command.getRequest().getEmployeeId());
        }

        transactionDto.setAuthorizationCode(extractFieldFromResponse(command.getRequest().getMerchantResponse(), "AzulOrderId"));

        this.transactionService.update(transactionDto);

        //3- Actualizar vcc_transaction_payment_logs columna merchant_respose en vcc_transaction
        transactionPaymentLogsDto.setMerchantResponse(command.getRequest().getMerchantResponse());
        transactionPaymentLogsDto.setIsProcessed(true);
        this.transactionPaymentLogsService.update(transactionPaymentLogsDto);

        ManagerMerchantConfigDto merchantConfigDto = merchantConfigService.findByMerchantID(transactionDto.getMerchant().getId());
        //Enviar correo (voucher) de confirmacion a las personas implicadas
        byte[] attachment = this.voucherService.createAndUploadAndAttachTransactionVoucher(transactionDto, merchantConfigDto, command.getRequest().getEmployee());
        transactionService.sendTransactionConfirmationVoucherEmail(transactionDto, merchantConfigDto, command.getRequest().getResponseCodeMessage(), attachment);
    }

    private String extractFieldFromResponse(String request, String fieldName){
        String[] data = request.split("&");
        for (String param : data) {
            if (param.startsWith(fieldName + "=")) {
                String[] keyValue = param.split("=", 2);
                return keyValue.length > 1 ? keyValue[1] : "";
            }
        }
        return null;
    }
}
