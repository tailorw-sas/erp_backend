package com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAccountType;

import com.kynsof.share.core.domain.kafka.entity.ManageAccountTypeKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerReplicateAccountTypeService {
    private final KafkaTemplate<String, Object> producer;

    public ProducerReplicateAccountTypeService(KafkaTemplate<String, Object> producer) {
        this.producer = producer;
    }

    @Async
    public void replicate(ManageAccountTypeKafka entity) {

        try {
            this.producer.send("finamer-replicate-manage-account-type", entity);
        } catch (Exception ex) {
            Logger.getLogger(ProducerReplicateAccountTypeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}