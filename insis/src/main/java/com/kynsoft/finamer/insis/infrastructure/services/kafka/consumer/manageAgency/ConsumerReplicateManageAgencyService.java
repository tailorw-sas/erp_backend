package com.kynsoft.finamer.insis.infrastructure.services.kafka.consumer.manageAgency;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageAgencyKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.insis.application.command.manageAgency.create.CreateManageAgencyCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageAgencyService {
    private final IMediator mediator;

    public ConsumerReplicateManageAgencyService(IMediator mediator){
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-agency", groupId = "innsist-entity-replica")
    public void listen(ReplicateManageAgencyKafka objKafka){
        try{
            CreateManageAgencyCommand command = new CreateManageAgencyCommand(
                    objKafka.getId(),
                    objKafka.getCode(),
                    objKafka.getName(),
                    objKafka.getAgencyAlias(),
                    objKafka.getStatus()
            );

            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerReplicateManageAgencyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
