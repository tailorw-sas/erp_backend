package com.kynsoft.finamer.creditcard.infrastructure.services.kafka.consumer.hotelPayment;

import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentFailedIncome;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentSuccessIncome;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerHotelPaymentIncomeResponse {

    @KafkaListener(topics = "finamer-replicate-hotel-payment-response", groupId = "vcc-entity-replica")
    public void listen(ConsumerRecord<String, Object> consumerRecord) {
        try {
            Object response = consumerRecord.value();
            if (response instanceof ReplicateHotelPaymentSuccessIncome successResponse) {
                System.out.println("Success Response -> " + "\nIncome: "+successResponse.getIncomeId() + "\nHotelPayment: "+successResponse.getHotelPaymentId());
            } else if (response instanceof ReplicateHotelPaymentFailedIncome failedResponse) {
                System.out.println("Failed Response -> " +"\nHotelPayment: "+ failedResponse.getHotelPaymentId());
                System.out.println("Errors -> {");
                failedResponse.getErrors().forEach(error -> System.out.println("  "+error.getField() + ": "+error.getMsg()));
                System.out.println("}");
            }
        } catch (Exception e) {
            Logger.getLogger(ConsumerHotelPaymentIncomeResponse.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
