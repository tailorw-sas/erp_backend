package com.kynsoft.finamer.invoicing.application.command.manageInvoice.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.rules.manageBooking.ManageBookingCheckBookingAmountAndBookingBalanceRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoice.ManageInvoiceInvoiceDateInCloseOperationRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoice.ManageInvoiceValidateChangeAgencyRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoice.ManageInvoiceValidateChangeStatusRule;
import com.kynsoft.finamer.invoicing.domain.services.*;
import com.kynsoft.finamer.invoicing.infrastructure.services.kafka.producer.manageInvoice.ProducerUpdateManageInvoiceService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UpdateInvoiceCommandHandler implements ICommandHandler<UpdateInvoiceCommand> {

    private final IManageInvoiceService service;
    private final IManageAgencyService agencyService;
    private final IManageHotelService hotelService;
    private final IManageInvoiceTypeService iManageInvoiceTypeService;
    private final IManageInvoiceStatusService invoiceStatusService;

    private final IInvoiceStatusHistoryService invoiceStatusHistoryService;

    private final ProducerUpdateManageInvoiceService producerUpdateManageInvoiceService;
    private final IInvoiceCloseOperationService closeOperationService;

    public UpdateInvoiceCommandHandler(IManageInvoiceService service,
            IManageAgencyService agencyService,
            IManageHotelService hotelService,
            IManageInvoiceTypeService iManageInvoiceTypeService,
            IInvoiceStatusHistoryService invoiceStatusHistoryService,
            ProducerUpdateManageInvoiceService producerUpdateManageInvoiceService,
            IManageInvoiceStatusService invoiceStatusService,
            IInvoiceCloseOperationService closeOperationService) {
        this.service = service;
        this.agencyService = agencyService;
        this.hotelService = hotelService;
        this.iManageInvoiceTypeService = iManageInvoiceTypeService;
        this.invoiceStatusHistoryService = invoiceStatusHistoryService;
        this.producerUpdateManageInvoiceService = producerUpdateManageInvoiceService;
        this.invoiceStatusService = invoiceStatusService;
        this.closeOperationService = closeOperationService;
    }

    @Override
    public void handle(UpdateInvoiceCommand command) {

        ManageInvoiceDto dto = this.service.findById(command.getId());
        RulesChecker.checkRule(new ManageBookingCheckBookingAmountAndBookingBalanceRule(dto.getInvoiceAmount(), dto.getDueAmount()));
        ConsumerUpdate update = new ConsumerUpdate();
        if (command.getInvoiceDate() != null && !command.getInvoiceDate().equals(dto.getInvoiceDate())) {
            RulesChecker.checkRule(new ManageInvoiceInvoiceDateInCloseOperationRule(this.closeOperationService, dto.getInvoiceDate().toLocalDate(), dto.getHotel().getId()));
            this.updateLocalDateTime(dto::setInvoiceDate, command.getInvoiceDate(), dto.getInvoiceDate(), update::setUpdate);
        }

        if (!command.getAgency().equals(dto.getAgency().getId())) {
            RulesChecker.checkRule(new ManageInvoiceValidateChangeAgencyRule(dto.getStatus()));
            this.updateAgency(dto::setAgency, command.getAgency(), dto.getAgency().getId(), update::setUpdate);
        }
        if (command.getInvoiceStatus() != null) {

            ManageInvoiceStatusDto invoiceStatusDto = this.invoiceStatusService.findById(command.getInvoiceStatus());
            //if (!dto.getStatus().equals(EInvoiceStatus.CANCELED) && command.getStatus().equals(EInvoiceStatus.CANCELED)) {
            if (!dto.getStatus().equals(EInvoiceStatus.CANCELED) && invoiceStatusDto.isCanceledStatus()) {
                RulesChecker.checkRule(new ManageInvoiceValidateChangeStatusRule(dto.getStatus()));
                dto.setStatus(EInvoiceStatus.CANCELED);
                dto.setManageInvoiceStatus(this.invoiceStatusService.findByCanceledStatus());
            }
        }

        this.service.calculateInvoiceAmount(dto);
        try {
            //TODO: aqui se envia para actualizar el invoice en payment
            this.producerUpdateManageInvoiceService.update(dto);
        } catch (Exception e) {
        }
        this.service.update(dto);
    }

    public void updateLocalDateTime(Consumer<LocalDateTime> setter, LocalDateTime newValue, LocalDateTime oldValue,
            Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);
        }
    }

    public void updateAgency(Consumer<ManageAgencyDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManageAgencyDto agencyDto = this.agencyService.findById(newValue);
            setter.accept(agencyDto);
            update.accept(1);

        }
    }

    public void updateManageInvoiceStatus(Consumer<ManageInvoiceStatusDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManageInvoiceStatusDto invoiceStatusDto = this.invoiceStatusService.findById(newValue);
            setter.accept(invoiceStatusDto);
            update.accept(1);
        }
    }

    private void updateDate(Consumer<LocalDate> setter, LocalDate newValue, LocalDate oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);

        }
    }

    public void updateHotel(Consumer<ManageHotelDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManageHotelDto hotelDto = this.hotelService.findById(newValue);
            setter.accept(hotelDto);
            update.accept(1);

        }
    }

    public void updateInvoiceType(Consumer<ManageInvoiceTypeDto> setter, UUID newValue, UUID oldValue,
            Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManageInvoiceTypeDto invoiceTypeDto = this.iManageInvoiceTypeService.findById(newValue);
            setter.accept(invoiceTypeDto);
            update.accept(1);

        }
    }

    private void updateInvoiceStatusHistory(ManageInvoiceDto invoiceDto, String employee) {

        InvoiceStatusHistoryDto dto = new InvoiceStatusHistoryDto();
        dto.setId(UUID.randomUUID());
        dto.setInvoice(invoiceDto);
        dto.setDescription("The income data was updated.");
        dto.setEmployee(employee);

        this.invoiceStatusHistoryService.create(dto);

    }

}
