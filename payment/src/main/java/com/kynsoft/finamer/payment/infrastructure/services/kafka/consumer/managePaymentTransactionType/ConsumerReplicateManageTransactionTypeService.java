package com.kynsoft.finamer.payment.infrastructure.services.kafka.consumer.managePaymentTransactionType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsof.share.core.domain.kafka.entity.ReplicateManagePaymentTransactionTypeKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.managePaymentTransactionType.create.CreateManagePaymentTransactionTypeCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageTransactionTypeService {

    private final IMediator mediator;

    public ConsumerReplicateManageTransactionTypeService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-payment-transaction-type", groupId = "payment-entity-replica")
    public void listen(String event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(event);

            ReplicateManagePaymentTransactionTypeKafka objKafka = objectMapper.treeToValue(rootNode, ReplicateManagePaymentTransactionTypeKafka.class);
            CreateManagePaymentTransactionTypeCommand command = new CreateManagePaymentTransactionTypeCommand(
                    objKafka.getId(), 
                    objKafka.getCode(), 
                    objKafka.getName(),
                    objKafka.getCash(),
                    objKafka.getDeposit(),
                    objKafka.getApplyDeposit(),
                    objKafka.getRemarkRequired(),
                    objKafka.getMinNumberOfCharacter()
            );
            mediator.send(command);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsumerReplicateManageTransactionTypeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
