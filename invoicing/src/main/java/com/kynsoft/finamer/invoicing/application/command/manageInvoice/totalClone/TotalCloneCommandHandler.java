package com.kynsoft.finamer.invoicing.application.command.manageInvoice.totalClone;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.InvoiceType;
import com.kynsoft.finamer.invoicing.domain.services.*;
import com.kynsoft.finamer.invoicing.infrastructure.services.kafka.producer.manageInvoice.ProducerReplicateManageInvoiceService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Component
public class TotalCloneCommandHandler implements ICommandHandler<TotalCloneCommand> {

    private final IManageInvoiceService invoiceService;
    private final IManageAgencyService agencyService;
    private final IManageHotelService hotelService;
    private final IManageAttachmentTypeService attachmentTypeService;
    private final IManageBookingService bookingService;
    private final IParameterizationService parameterizationService;
    private final IManageInvoiceStatusService invoiceStatusService;
    private final IInvoiceCloseOperationService closeOperationService;
    private final ProducerReplicateManageInvoiceService producerReplicateManageInvoiceService;
    private final IManageRatePlanService ratePlanService;
    private final IManageNightTypeService nightTypeService;
    private final IManageRoomTypeService roomTypeService;
    private final IManageRoomCategoryService roomCategoryService;
    private final IInvoiceStatusHistoryService invoiceStatusHistoryService;
    private final IAttachmentStatusHistoryService attachmentStatusHistoryService;

    public TotalCloneCommandHandler(IManageInvoiceService invoiceService, IManageAgencyService agencyService, IManageHotelService hotelService, IManageAttachmentTypeService attachmentTypeService, IManageBookingService bookingService, IParameterizationService parameterizationService, IManageInvoiceStatusService invoiceStatusService, IInvoiceCloseOperationService closeOperationService, ProducerReplicateManageInvoiceService producerReplicateManageInvoiceService, IManageRatePlanService ratePlanService, IManageNightTypeService nightTypeService, IManageRoomTypeService roomTypeService, IManageRoomCategoryService roomCategoryService, IInvoiceStatusHistoryService invoiceStatusHistoryService, IAttachmentStatusHistoryService attachmentStatusHistoryService) {
        this.invoiceService = invoiceService;
        this.agencyService = agencyService;
        this.hotelService = hotelService;
        this.attachmentTypeService = attachmentTypeService;
        this.bookingService = bookingService;
        this.parameterizationService = parameterizationService;
        this.invoiceStatusService = invoiceStatusService;
        this.closeOperationService = closeOperationService;
        this.producerReplicateManageInvoiceService = producerReplicateManageInvoiceService;
        this.ratePlanService = ratePlanService;
        this.nightTypeService = nightTypeService;
        this.roomTypeService = roomTypeService;
        this.roomCategoryService = roomCategoryService;
        this.invoiceStatusHistoryService = invoiceStatusHistoryService;
        this.attachmentStatusHistoryService = attachmentStatusHistoryService;
    }


    @Override
    public void handle(TotalCloneCommand command) {

        List<ManageBookingDto> bookings = new LinkedList<>();
        List<ManageAttachmentDto> attachmentDtos = new LinkedList<>();

        ManageInvoiceDto invoiceToClone = this.invoiceService.findById(command.getInvoiceToClone());
        ManageHotelDto hotelDto = this.hotelService.findById(command.getHotel());

        //vienen todos los attachments juntos, lo del padre y los nuevos
        //TODO: incluir en el request de los attachments el employeeName y employeeId
        for (TotalCloneAttachmentRequest attachmentRequest: command.getAttachments()) {
            ManageAttachmentTypeDto attachmentType = this.attachmentTypeService.findById(
                    attachmentRequest.getType());

            ManageAttachmentDto attachmentDto = new ManageAttachmentDto(
                    UUID.randomUUID(),
                    null,
                    attachmentRequest.getFilename(),
                    attachmentRequest.getFile(),
                    attachmentRequest.getRemark(),
                    attachmentType,
                    null,
                    attachmentRequest.getEmployeeName(),
                    attachmentRequest.getEmployeeId(),
                    null,
                    null);

            attachmentDtos.add(attachmentDto);
        }

        for(TotalCloneBookingRequest bookingRequest : command.getBookings()){
            //no se agregan nuevos booking solo se pueden editar, el id siempre debe venir
            ManageBookingDto bookingToClone = this.bookingService.findById(bookingRequest.getId());
            List<ManageRoomRateDto> roomRateDtoList = bookingToClone.getRoomRates();

            //cambiando el id de los room rates para poder guardarlos
            for (ManageRoomRateDto roomRateDto : roomRateDtoList){
                roomRateDto.setId(UUID.randomUUID());
            }

            //obtener los nomencladores si vienen en el request
            ManageNightTypeDto nightTypeDto = bookingRequest.getNightType() != null
                    ? this.nightTypeService.findById(bookingRequest.getNightType())
                    : null;
            ManageRoomTypeDto roomTypeDto = bookingRequest.getRoomType() != null
                    ? this.roomTypeService.findById(bookingRequest.getRoomType())
                    : null;
            ManageRoomCategoryDto roomCategoryDto = bookingRequest.getRoomCategory() != null
                    ? this.roomCategoryService.findById(bookingRequest.getRoomCategory())
                    : null;
            ManageRatePlanDto ratePlanDto = bookingRequest.getRatePlan() != null
                    ? this.ratePlanService.findById(bookingRequest.getRatePlan())
                    : null;

            //creando el nuevo booking con los valores que vienen, el amount se agarra directo del padre
            ManageBookingDto newBooking = new ManageBookingDto(
                    UUID.randomUUID(),
                    null,
                    null,
                    bookingRequest.getHotelCreationDate(),
                    bookingRequest.getBookingDate(),
                    bookingRequest.getCheckIn(),
                    bookingRequest.getCheckOut(),
                    bookingRequest.getHotelBookingNumber(),
                    bookingRequest.getFullName(),
                    bookingRequest.getFirstName(),
                    bookingRequest.getLastName(),
                    bookingToClone.getInvoiceAmount(),
                    bookingToClone.getDueAmount(),
                    bookingRequest.getRoomNumber(),
                    bookingRequest.getCouponNumber(),
                    bookingRequest.getAdults(),
                    bookingRequest.getChildren(),
                    bookingRequest.getRateAdult(),
                    bookingRequest.getRateChild(),
                    bookingRequest.getHotelInvoiceNumber(),
                    bookingRequest.getFolioNumber(),
                    bookingRequest.getHotelAmount(),
                    bookingRequest.getDescription(),
                    null,
                    ratePlanDto,
                    nightTypeDto,
                    roomTypeDto,
                    roomCategoryDto,
                    roomRateDtoList,
                    null,
                    bookingToClone
            );
            bookings.add(newBooking);
        }

        for (ManageBookingDto booking : bookings) {
            this.calculateBookingHotelAmount(booking);
        }

        ManageAgencyDto agencyDto = this.agencyService.findById(command.getAgency());
        String invoiceNumber = InvoiceType.getInvoiceTypeCode(invoiceToClone.getInvoiceType());
        if (hotelDto.getManageTradingCompanies() != null
                && hotelDto.getManageTradingCompanies().getIsApplyInvoice()) {
            invoiceNumber += "-" + hotelDto.getManageTradingCompanies().getCode();
        } else {
            invoiceNumber += "-" + hotelDto.getCode();
        }

        //TODO: replicar el campo creditDay de Agency para calcular dueDate
        //LocalDate dueDate = command.getInvoiceDate().toLocalDate().plusDays(5L);
        LocalDate dueDate = command.getInvoiceDate().toLocalDate();

        EInvoiceStatus status = EInvoiceStatus.RECONCILED;
        ParameterizationDto parameterization = this.parameterizationService.findActiveParameterization();
        ManageInvoiceStatusDto invoiceStatus = parameterization != null ? this.invoiceStatusService.findByCode(parameterization.getProcessed()) : null;

        ManageInvoiceDto clonedInvoice = new ManageInvoiceDto(
                command.getClonedInvoice(),
                0L,
                0L,
                invoiceNumber,
                command.getInvoiceDate(),
                dueDate,
                true,
                invoiceToClone.getInvoiceAmount(), //TODO: revisar si es mejor asi o calcularlo nuevamente
                invoiceToClone.getDueAmount(),
                hotelDto,
                agencyDto,
                invoiceToClone.getInvoiceType(), //TODO: se queda con el invoiceType del padre?
                status,
                false, //TODO: de donde sale esto?
                bookings,
                attachmentDtos,
                false, //TODO: de donde sale esto?
                null,
                null, //TODO: parametrizacion para cargar esto
                invoiceStatus,
                null,
                true,
                invoiceToClone,
                invoiceToClone.getCredits()
        );

        ManageInvoiceDto created = this.invoiceService.create(clonedInvoice);
        command.setClonedInvoiceId(created.getInvoiceId());
        command.setClonedInvoiceNo(created.getInvoiceNumber());

        this.setInvoiceToCloneAmounts(invoiceToClone, command.getEmployeeName());

        try {
            this.producerReplicateManageInvoiceService.create(created);
        } catch (Exception e) {
        }

        //invoice status history
        this.invoiceStatusHistoryService.create(
                new InvoiceStatusHistoryDto(
                        UUID.randomUUID(),
                        created,
                        "The invoice data was inserted.",
                        null,
                        command.getEmployeeName(),
                        status
                )
        );

        //attachment status history
        for(ManageAttachmentDto attachment : created.getAttachments()) {
            this.attachmentStatusHistoryService.create(
                    new AttachmentStatusHistoryDto(
                            UUID.randomUUID(),
                            "An attachment to the invoice was inserted. The file name: " + attachment.getFilename(),
                            attachment.getAttachmentId(),
                            created,
                            command.getEmployeeName(),
                            attachment.getEmployeeId(),
                            null,
                            null
                    )
            );
        }
    }

    private void calculateBookingHotelAmount(ManageBookingDto dto) {
        Double hotelAmount = 0.00;
        if (dto.getRoomRates() != null) {
            for (ManageRoomRateDto roomRateDto : dto.getRoomRates()) {
                hotelAmount += roomRateDto.getHotelAmount();
            }
            dto.setHotelAmount(hotelAmount);
        }
    }

    private void setInvoiceToCloneAmounts(ManageInvoiceDto invoiceDto, String employee){

        for (ManageBookingDto bookingDto : invoiceDto.getBookings()){
            for (ManageRoomRateDto roomRateDto : bookingDto.getRoomRates()){
                List<ManageAdjustmentDto> adjustmentDtoList = new LinkedList<>();
                ManageAdjustmentDto adjustmentDto = new ManageAdjustmentDto(
                        UUID.randomUUID(),
                        null,
                        -roomRateDto.getInvoiceAmount(),
                        LocalDateTime.now(),
                        "Automatic adjustment generated to closed the invoice, because it was cloned",
                        null, //TODO: aclarar todos los campos que dejo en null
                        null,
                        null,
                        employee
                );
                adjustmentDtoList.add(adjustmentDto);
                roomRateDto.setAdjustments(adjustmentDtoList);
                roomRateDto.setInvoiceAmount(0.00);
            }
            this.bookingService.calculateInvoiceAmount(bookingDto);
        }
        this.invoiceService.calculateInvoiceAmount(invoiceDto);
    }
}