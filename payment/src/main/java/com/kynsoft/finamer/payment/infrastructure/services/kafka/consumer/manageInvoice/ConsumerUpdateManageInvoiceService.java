package com.kynsoft.finamer.payment.infrastructure.services.kafka.consumer.manageInvoice;

import com.kynsof.share.core.domain.kafka.entity.ManageBookingKafka;
import com.kynsof.share.core.domain.kafka.entity.ManageInvoiceKafka;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.domain.services.IManageBookingService;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUpdateManageInvoiceService {

    private final IManageInvoiceService service;
    private final IManageBookingService serviceBookingService;

    public ConsumerUpdateManageInvoiceService(IManageInvoiceService service,
            IManageBookingService serviceBookingService) {

        this.service = service;
        this.serviceBookingService = serviceBookingService;
    }

    @KafkaListener(topics = "finamer-update-manage-invoice", groupId = "payment-entity-replica")
    public void listen(ManageInvoiceKafka objKafka) {
        try {
            List<ManageBookingDto> bookingDtos = new ArrayList<>();
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
                            booking.getBookingParent() != null ? this.serviceBookingService.findById(booking.getBookingParent()) : null
                    ));
                }
            }

            this.service.update(new ManageInvoiceDto(
                    objKafka.getId(), 
                    objKafka.getInvoiceId(), 
                    objKafka.getInvoiceNo(), 
                    deleteHotelInfo(objKafka.getInvoiceNumber()), 
                    EInvoiceType.valueOf(objKafka.getInvoiceType()),
                    objKafka.getInvoiceAmount(), 
                    bookingDtos,
                    objKafka.getHasAttachment(),
                    objKafka.getInvoiceParent() != null ? this.service.findById(objKafka.getInvoiceParent()) : null,
                    objKafka.getInvoiceDate()
            ));
        } catch (Exception ex) {
            Logger.getLogger(ConsumerUpdateManageInvoiceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }

}
