package com.kynsoft.finamer.payment.infrastructure.services.kafka.consumer.manageInvoice;

import com.kynsof.share.core.domain.kafka.entity.AttachmentKafka;
import com.kynsof.share.core.domain.kafka.entity.ManageBookingKafka;
import com.kynsof.share.core.domain.kafka.entity.ManageInvoiceKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.payment.create.CreateAttachmentRequest;
import com.kynsoft.finamer.payment.application.command.payment.createPaymentToCredit.CreatePaymentToCreditCommand;
import com.kynsoft.finamer.payment.domain.dto.*;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ConsumerReplicateManageInvoiceService {

    private final IManageInvoiceService service;
    private final IManageHotelService hotelService;
    private final IMediator mediator;
    private final IManageBookingService serviceBookingService;
    private final IManageHotelService manageHotelService;
    private final IManageAgencyService manageAgencyService;
    private final IManageInvoiceStatusService manageInvoiceStatusService;

    public ConsumerReplicateManageInvoiceService(IManageInvoiceService service,
                                                 IMediator mediator,
                                                 IManageHotelService hotelService,
                                                 IManageBookingService serviceBookingService,
                                                 IManageHotelService manageHotelService,
                                                 IManageAgencyService manageAgencyService,
                                                 IManageInvoiceStatusService manageInvoiceStatusService) {
        this.mediator = mediator;
        this.service = service;
        this.hotelService = hotelService;
        this.serviceBookingService = serviceBookingService;
        this.manageHotelService = manageHotelService;
        this.manageAgencyService = manageAgencyService;
        this.manageInvoiceStatusService = manageInvoiceStatusService;
    }

    @KafkaListener(topics = "finamer-replicate-manage-invoice", groupId = "payment-entity-replica")
    public void listen(ManageInvoiceKafka objKafka) {
        List<ManageBookingDto> bookingDtos = new ArrayList<>();
        this.createBookingList(objKafka, bookingDtos);

        ManageHotelDto manageHotelDto = manageHotelService.findById(objKafka.getHotel());
        ManageAgencyDto manageAgencyDto = manageAgencyService.findById(objKafka.getAgency());
        ManageInvoiceStatusDto manageInvoiceStatusDto = this.manageInvoiceStatusService.findById(objKafka.getInvoiceStatus());
        ManageInvoiceDto invoiceParent = this.getInvoiceParent(objKafka.getInvoiceParent());


        ManageInvoiceDto invoiceDto = new ManageInvoiceDto(
                objKafka.getId(),
                objKafka.getInvoiceId(),
                objKafka.getInvoiceNo(),
                deleteHotelInfo(objKafka.getInvoiceNumber()),
                EInvoiceType.valueOf(objKafka.getInvoiceType()),
                objKafka.getInvoiceAmount(),
                objKafka.getInvoiceBalance(),
                bookingDtos,
                objKafka.getHasAttachment(), //!= null ? objKafka.getHasAttachment() : false
                objKafka.getInvoiceParent() != null ? invoiceParent : null,
                objKafka.getInvoiceDate(),
                manageHotelDto,
                manageAgencyDto,
                objKafka.getAutoRec(),
                objKafka.getInvoiceStatus() != null ? manageInvoiceStatusDto : null
        );

        this.service.create(invoiceDto);

        if (invoiceDto.getInvoiceType().equals(EInvoiceType.CREDIT)) {
            ManageHotelDto hotelDto = this.hotelService.findById(objKafka.getHotel());
            if (!hotelDto.getAutoApplyCredit()) {//check no marcado
                this.automaticProcessApplyPayment(objKafka, invoiceDto, true);//Aplica al padre
            } else {//check marcado
                 this.automaticProcessApplyPayment(objKafka, invoiceDto, false);
//                List<CreateAttachmentRequest> attachmentKafkas = new ArrayList<>();
//                this.addAttachment(objKafka, attachmentKafkas);
//                this.mediator.send(new CreatePaymentToCreditCommand(objKafka.getClient(), objKafka.getAgency(), objKafka.getHotel(), invoiceDto, attachmentKafkas, false, mediator));
            }
        }
        if (invoiceDto.getInvoiceType().equals(EInvoiceType.OLD_CREDIT)) {//check marcado
            this.automaticProcessApplyPayment(objKafka, invoiceDto, false);
        }
    }

    private void createBookingList(ManageInvoiceKafka objKafka, List<ManageBookingDto> bookingDtos) {
        if (objKafka.getBookings() != null) {
            for (ManageBookingKafka booking : objKafka.getBookings()) {
                bookingDtos.add(new ManageBookingDto(
                        booking.getId(),
                        booking.getBookingId(),
                        booking.getReservationNumber(),
                        booking.getCheckIn(),
                        booking.getCheckOut(),
                        booking.getFullName(),
                        booking.getFirstName(),
                        booking.getLastName(),
                        booking.getInvoiceAmount(),
                        booking.getAmountBalance(),
                        booking.getCouponNumber(),
                        booking.getAdults(),
                        booking.getChildren(),
                        null,
                        booking.getBookingParent() != null ? this.serviceBookingService.findById(booking.getBookingParent()) : null,
                        booking.getBookingDate()
                ));
            }
        }
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }

    private void addAttachment(ManageInvoiceKafka objKafka, List<CreateAttachmentRequest> attachmentKafkas) {
        if (objKafka.getAttachments() != null) {
            for (AttachmentKafka attDto : objKafka.getAttachments()) {
                attachmentKafkas.add(new CreateAttachmentRequest(
                        Status.ACTIVE,
                        attDto.getEmployee(),
                        null,
                        null,
                        attDto.getFileName(),
                        "",
                        attDto.getPath(),
                        attDto.getRemark(),
                        attDto.isSupport()
                ));
            }
        }
    }

    private void automaticProcessApplyPayment(ManageInvoiceKafka objKafka, ManageInvoiceDto invoiceDto, boolean autoApplyCredit) {

        List<CreateAttachmentRequest> attachmentKafkas = new ArrayList<>();
        this.addAttachment(objKafka, attachmentKafkas);

        //this.mediator.send(new CreatePaymentToCreditCommand(objKafka.getClient(), objKafka.getAgency(), objKafka.getHotel(), invoiceDto, attachmentKafkas, true, mediator));
        this.mediator.send(new CreatePaymentToCreditCommand(objKafka.getClient(), objKafka.getAgency(), objKafka.getHotel(), invoiceDto, attachmentKafkas, autoApplyCredit, mediator, objKafka.getEmployee()));
    }

    private ManageInvoiceDto getInvoiceParent(UUID parentId){
        if(Objects.nonNull(parentId)){
            return this.service.findById(parentId);
        }else {
            return null;
        }
    }
}
