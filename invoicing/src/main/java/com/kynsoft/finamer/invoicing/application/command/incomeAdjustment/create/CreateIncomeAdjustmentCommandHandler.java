package com.kynsoft.finamer.invoicing.application.command.incomeAdjustment.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.core.infrastructure.util.DateUtil;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.rules.income.CheckAmountNotZeroRule;
import com.kynsoft.finamer.invoicing.domain.rules.income.CheckIfIncomeDateIsBeforeCurrentDateRule;
import com.kynsoft.finamer.invoicing.domain.services.*;
import com.kynsoft.finamer.invoicing.infrastructure.services.kafka.producer.manageInvoice.ProducerReplicateManageInvoiceService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CreateIncomeAdjustmentCommandHandler implements ICommandHandler<CreateIncomeAdjustmentCommand> {

    private final IManageAdjustmentService manageAdjustmentService;
    private final IManagePaymentTransactionTypeService transactionTypeService;
    private final IInvoiceCloseOperationService closeOperationService;
    private final IManageBookingService bookingService;

    private final IManageInvoiceService service;
    private final IManageEmployeeService employeeService;
    private final ProducerReplicateManageInvoiceService producerReplicateManageInvoiceService;

    public CreateIncomeAdjustmentCommandHandler(IManagePaymentTransactionTypeService transactionTypeService,
                                                IInvoiceCloseOperationService closeOperationService,
                                                IManageBookingService bookingService,
                                                IManageAdjustmentService manageAdjustmentService,
                                                IManageInvoiceService service, IManageEmployeeService employeeService,
                                                ProducerReplicateManageInvoiceService producerReplicateManageInvoiceService) {
        this.transactionTypeService = transactionTypeService;
        this.closeOperationService = closeOperationService;
        this.bookingService = bookingService;
        this.manageAdjustmentService = manageAdjustmentService;
        this.service = service;
        this.employeeService = employeeService;
        this.producerReplicateManageInvoiceService = producerReplicateManageInvoiceService;
    }

    @Override
    public void handle(CreateIncomeAdjustmentCommand command) {

        // RulesChecker.checkRule(new
        // ValidateObjectNotNullRule<>(command.getTransactionType(), "transactionType",
        // "Manage Invoice Transaction Type ID cannot be null."));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getIncome(), "income",
                "Income ID cannot be null."));

        ManageInvoiceDto incomeDto = this.service.findById(command.getIncome());

        ManageRoomRateDto roomRateDto = new ManageRoomRateDto(
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                0.0,
                null,
                null,
                null,
                null,
                null,
                0.00,
                "",
                null,
                null,
                null,
                false,
                null
        );
        Double invoiceAmount = 0.0;
        List<ManageAdjustmentDto> adjustmentDtos = new ArrayList<>();

        for (NewIncomeAdjustmentRequest adjustment : command.getAdjustments()) {
            // Puede ser + y -, pero no puede ser 0
            RulesChecker.checkRule(new CheckAmountNotZeroRule(adjustment.getAmount()));
            RulesChecker.checkRule(new CheckIfIncomeDateIsBeforeCurrentDateRule(adjustment.getDate()));
//            RulesChecker.checkRule(
//                    new ManageInvoiceInvoiceDateInCloseOperationRule(this.closeOperationService,
//                            adjustment.getDate(), incomeDto.getHotel().getId()));

            ManagePaymentTransactionTypeDto paymentTransactionTypeDto = adjustment
                    .getTransactionType() != null
                            ? this.transactionTypeService
                                    .findById(adjustment.getTransactionType())
                            : null;
            adjustmentDtos.add(new ManageAdjustmentDto(
                    UUID.randomUUID(),
                    0L,
                    adjustment.getAmount(),
                    invoiceDate(incomeDto.getHotel().getId(), adjustment.getDate().atStartOfDay()),
                    adjustment.getRemark(),
                    null,
                    paymentTransactionTypeDto,
                    null,
                    this.employeeService.getEmployeeFullName(command.getEmployee()),
                    false
            ));
            invoiceAmount += adjustment.getAmount();
        }
        ConsumerUpdate updatePayment = new ConsumerUpdate();
        UpdateIfNotNull.updateDouble(incomeDto::setInvoiceAmount, invoiceAmount, updatePayment::setUpdate);
        UpdateIfNotNull.updateDouble(incomeDto::setDueAmount, invoiceAmount, updatePayment::setUpdate);
        incomeDto.setOriginalAmount(invoiceAmount);
        roomRateDto.setInvoiceAmount(invoiceAmount);
        roomRateDto.setAdjustments(adjustmentDtos);

        List<ManageRoomRateDto> roomRates = new ArrayList<>();
        roomRates.add(roomRateDto);

        ManageBookingDto bookingDto = new ManageBookingDto(
                UUID.randomUUID(),
                0L,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null,
                null,
                null,
                invoiceAmount,
                invoiceAmount,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0.00,
                null,
                incomeDto,
                null,
                null,
                null,
                null,
                roomRates,
                null, 
                null, 
                null,
                false,
                null
        );
        this.bookingService.create(bookingDto);

        // this.manageAdjustmentService.create(adjustmentDtos);
        ManageInvoiceDto updated = this.service.update(incomeDto);
        try {
            this.producerReplicateManageInvoiceService.create(updated, null, null);
        } catch (Exception e) {
        }
        // ManageInvoiceDto updatedIncome = this.service.findById(incomeDto.getId());
        // this.service.calculateInvoiceAmount(updatedIncome);
    }

    private LocalDateTime invoiceDate(UUID hotel, LocalDateTime invoiceDate) {
        InvoiceCloseOperationDto closeOperationDto = this.closeOperationService.findActiveByHotelId(hotel);

        if (DateUtil.getDateForCloseOperation(closeOperationDto.getBeginDate(), closeOperationDto.getEndDate(), invoiceDate.toLocalDate())) {
            return invoiceDate;
        }

        if (closeOperationDto.getEndDate().isAfter(LocalDate.now())){
            return LocalDateTime.now(ZoneId.of("UTC"));
        }

        return LocalDateTime.of(closeOperationDto.getEndDate(), LocalTime.now(ZoneId.of("UTC")));
    }

}
