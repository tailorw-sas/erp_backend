package com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageTradigCompany;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageTradingCompanyKafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerReplicateManageTradingCompanyService {
    private final KafkaTemplate<String, Object> producer;

    public ProducerReplicateManageTradingCompanyService(KafkaTemplate<String, Object> producer) {
        this.producer = producer;
    }

    @Async
    public void create(ReplicateManageTradingCompanyKafka entity) {

        try {
            this.producer.send("finamer-replicate-manage-trading-company", entity);
        } catch (Exception ex) {
            Logger.getLogger(ProducerReplicateManageTradingCompanyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}