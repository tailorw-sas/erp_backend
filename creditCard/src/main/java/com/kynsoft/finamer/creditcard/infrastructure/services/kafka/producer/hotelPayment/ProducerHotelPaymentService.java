package com.kynsoft.finamer.creditcard.infrastructure.services.kafka.producer.hotelPayment;

import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerHotelPaymentService {

    private final KafkaTemplate<String, Object> producer;

    public ProducerHotelPaymentService(KafkaTemplate<String, Object> producer) {
        this.producer = producer;
    }

    @Async
    public void producer(ReplicateHotelPaymentKafka entity){
        try {
            this.producer.send("finamer-replicate-hotel-payment", entity);
        } catch (Exception ex) {
            Logger.getLogger(ProducerHotelPaymentService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
