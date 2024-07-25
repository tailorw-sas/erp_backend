package com.kynsoft.finamer.invoicing.application.command.manageInvoice.update;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.invoicing.domain.dto.ManageAgencyDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceTypeDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.InvoiceType;
import com.kynsoft.finamer.invoicing.domain.services.IManageAgencyService;
import com.kynsoft.finamer.invoicing.domain.services.IManageHotelService;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceService;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceTypeService;
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

    public UpdateInvoiceCommandHandler(IManageInvoiceService service, IManageAgencyService agencyService,
            IManageHotelService hotelService, IManageInvoiceTypeService iManageInvoiceTypeService) {
        this.service = service;
        this.agencyService = agencyService;
        this.hotelService = hotelService;
        this.iManageInvoiceTypeService = iManageInvoiceTypeService;
    }

    @Override
    public void handle(UpdateInvoiceCommand command) {

        ManageInvoiceDto dto = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateBoolean(dto::setIsManual, command.getIsManual(), dto.getIsManual(), update::setUpdate);
        UpdateIfNotNull.updateDouble(dto::setInvoiceAmount, command.getInvoiceAmount(), dto.getInvoiceAmount(),
                update::setUpdate);
        this.updateDate(dto::setInvoiceDate, command.getInvoiceDate(), dto.getInvoiceDate(),
                update::setUpdate);
        this.updateAgency(dto::setAgency, command.getAgency(), dto.getAgency().getId(), update::setUpdate);
        this.updateHotel(dto::setHotel, command.getHotel(), dto.getHotel().getId(), update::setUpdate);

        // dto.setInvoiceNumber(InvoiceType.getInvoiceTypeCode(dto.getInvoiceType() != null ? dto.getInvoiceType() : EInvoiceType.INVOICE) + "-" + dto.getInvoiceNo().toString());
        // update.setUpdate(1);

        this.service.calculateInvoiceAmount(dto);

        if (update.getUpdate() > 0) {
            this.service.update(dto);
        }

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

}
