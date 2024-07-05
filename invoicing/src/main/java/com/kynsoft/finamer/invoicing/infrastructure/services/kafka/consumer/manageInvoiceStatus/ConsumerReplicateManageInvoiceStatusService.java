package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.consumer.manageInvoiceStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsof.share.core.domain.kafka.entity.ReplicateManageInvoiceStatusKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.manageInvoiceStatus.create.CreateManageInvoiceStatusCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageInvoiceStatusService {

    private final IMediator mediator;

    public ConsumerReplicateManageInvoiceStatusService(IMediator mediator) {

        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-invoice-status", groupId = "invoicing-entity-replica")
    public void listen(ReplicateManageInvoiceStatusKafka objKafka) {
        try {

            CreateManageInvoiceStatusCommand command = new CreateManageInvoiceStatusCommand(objKafka.getId(),
                    objKafka.getCode(), objKafka.getName());
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerReplicateManageInvoiceStatusService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
