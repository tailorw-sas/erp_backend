package com.kynsoft.finamer.payment.infrastructure.services.kafka.consumer.income;

import com.kynsof.share.core.domain.kafka.entity.CreateIncomeTransactionSuccessKafka;
import com.kynsof.share.core.domain.kafka.entity.ManageBookingKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsof.share.utils.ServiceLocator;
import com.kynsoft.finamer.payment.application.command.paymentDetail.applyPayment.ApplyPaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.applyPayment.ApplyPaymentDetailMessage;
import com.kynsoft.finamer.payment.domain.dto.*;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.domain.services.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ConsumerCreateIncomeTransactionSuccess {
    private final IManageInvoiceService manageInvoiceService;
    private final IPaymentDetailService paymentDetailService;
    private final IManageBookingService manageBookingService;

    private final IManageHotelService manageHotelService;
    private final IManageAgencyService manageAgencyService;
    private final ServiceLocator<IMediator> serviceLocator;

    public ConsumerCreateIncomeTransactionSuccess(IManageInvoiceService manageInvoiceService,
                                                  IPaymentDetailService paymentDetailService,
                                                  IManageBookingService manageBookingService,
                                                  IManageHotelService manageHotelService,
                                                  IManageAgencyService manageAgencyService,
                                                  ServiceLocator<IMediator> serviceLocator) {
        this.manageInvoiceService = manageInvoiceService;
        this.paymentDetailService = paymentDetailService;
        this.manageBookingService = manageBookingService;
        this.manageHotelService = manageHotelService;
        this.manageAgencyService = manageAgencyService;
        this.serviceLocator = serviceLocator;
    }

    @KafkaListener(topics = "finamer-create-income-transaction-success", groupId = "payment-entity-replica")
    public void listen(CreateIncomeTransactionSuccessKafka objKafka) {
            this.stablishRelationPaymentDetailsIncome(objKafka);
    }

    private void stablishRelationPaymentDetailsIncome(CreateIncomeTransactionSuccessKafka objKafka){
        ManageInvoiceDto manageInvoiceDto =  createManageInvoice(objKafka,createManageBooking(objKafka.getBookings()));
        manageInvoiceService.create(manageInvoiceDto);
        PaymentDetailDto paymentDetailDto =paymentDetailService.findById(objKafka.getRelatedPaymentDetailIds());
        paymentDetailDto.setManageBooking(manageInvoiceDto.getBookings().get(0));
        paymentDetailService.create(paymentDetailDto);
        //TODO
        //Aplicar pago
        ApplyPaymentDetailCommand applyPaymentDetailCommand = new ApplyPaymentDetailCommand(paymentDetailDto.getId(),
                objKafka.getBookings().get(0).getId(),objKafka.getEmployeeId());
        serviceLocator.getBean(IMediator.class).send(applyPaymentDetailCommand);
    }

    private List<ManageBookingDto> createManageBooking(List<ManageBookingKafka> manageBookingKafkas){
      return   manageBookingKafkas.stream().map(booking-> new ManageBookingDto(booking.getId(),booking.getBookingId(),
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
               Objects.nonNull(booking.getBookingParent())?manageBookingService.findById(booking.getBookingParent()):null,
              booking.getBookingDate()
       )).toList();
    }

    private ManageInvoiceDto createManageInvoice(CreateIncomeTransactionSuccessKafka objKafka,List<ManageBookingDto> bookingDtos){
        ManageHotelDto manageHotelDto = manageHotelService.findById(objKafka.getHotel());
        ManageAgencyDto manageAgencyDto = manageAgencyService.findById(objKafka.getAgency());
        return new ManageInvoiceDto(
                objKafka.getId(),
                objKafka.getInvoiceId(),
                objKafka.getInvoiceNo(),
                deleteHotelInfo(objKafka.getInvoiceNumber()),
                EInvoiceType.valueOf(objKafka.getInvoiceType()),
                objKafka.getInvoiceAmount(),
                bookingDtos,
                false,
                objKafka.getInvoiceParent() != null ? this.manageInvoiceService.findById(objKafka.getInvoiceParent()) : null,
                objKafka.getInvoiceDate(),
                manageHotelDto,
                manageAgencyDto,
                false
        );
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }
}
