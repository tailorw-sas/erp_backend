package com.kynsoft.finamer.settings.infrastructure.services.kafka.consumer.manageModule;

import com.kynsof.share.core.domain.kafka.entity.ReplicateModuleKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.settings.application.command.manageModule.create.CreateManageModuleCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CReplicateManageModuleService {

    private final IMediator mediator;

    public CReplicateManageModuleService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-module", groupId = "settings-entity-replica")
    public void listen(ReplicateModuleKafka objKafka) {
        try {

            CreateManageModuleCommand command = new CreateManageModuleCommand(
                    objKafka.getId(), 
                    objKafka.getCode(), 
                    objKafka.getName(), 
                    objKafka.getStatus(), 
                    objKafka.getImage(), 
                    objKafka.getDescription()
            );

            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(CReplicateManageModuleService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
