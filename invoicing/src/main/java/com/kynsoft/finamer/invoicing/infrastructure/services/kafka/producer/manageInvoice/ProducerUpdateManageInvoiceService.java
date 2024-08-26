package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.producer.manageInvoice;

import com.kynsof.share.core.domain.kafka.entity.ManageBookingKafka;
import com.kynsof.share.core.domain.kafka.entity.ManageInvoiceKafka;
import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerUpdateManageInvoiceService {

    private final KafkaTemplate<String, Object> producer;

    public ProducerUpdateManageInvoiceService(KafkaTemplate<String, Object> producer) {
        this.producer = producer;
    }

    @Async
    public void update(ManageInvoiceDto entity) {
        try {
            List<ManageBookingKafka> bookingKafkas = new ArrayList<>();
            if (entity.getBookings() != null) {
                for (ManageBookingDto booking : entity.getBookings()) {
                    bookingKafkas.add(new ManageBookingKafka(
                            booking.getId(),
                            booking.getBookingId(),
                            booking.getReservationNumber(),
                            booking.getCheckIn(),
                            booking.getCheckOut(),
                            booking.getFullName(),
                            booking.getFirstName(),
                            booking.getLastName(),
                            booking.getInvoiceAmount(),
                            booking.getDueAmount(),
                            booking.getCouponNumber(),
                            booking.getAdults(),
                            booking.getChildren(),
                            entity.getId()
                    ));
                }
            }

            this.producer.send("finamer-update-manage-invoice", new ManageInvoiceKafka(
                    entity.getId(),
                    entity.getInvoiceId(),
                    entity.getInvoiceNo(),
                    entity.getInvoiceNumber(),
                    entity.getInvoiceAmount(),
                    bookingKafkas
            ));
        } catch (Exception ex) {
            Logger.getLogger(ProducerUpdateManageInvoiceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}