package com.kynsoft.finamer.insis.infrastructure.services.kafka.producer.manageHotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsoft.finamer.insis.infrastructure.model.kafka.ReplicateManageHotelKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerUpdateManageHotelService {
    private final KafkaTemplate<String, Object> producer;

    public ProducerUpdateManageHotelService(KafkaTemplate<String, Object> producer){
        this.producer = producer;
    }

    @Async
    public void update(ReplicateManageHotelKafka entity){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String serializedString  = mapper.writeValueAsString(entity);
            producer.send("finamer-innsist-update-manage-hotel", serializedString);
        }catch (Exception ex){
            Logger.getLogger(ProducerUpdateManageHotelService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
