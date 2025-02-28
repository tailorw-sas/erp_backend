package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.consumer.manageLanguage;

import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageLanguageKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.manageLanguage.update.UpdateManageLanguageCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUpdateManageLanguageService {

    private final IMediator mediator;

    public ConsumerUpdateManageLanguageService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-update-manage-language", groupId = "invoicing-entity-replica")
    public void listen(UpdateManageLanguageKafka objKafka) {
        try {
            UpdateManageLanguageCommand command = new UpdateManageLanguageCommand(objKafka.getId(), objKafka.getName(), objKafka.getDefaults(), objKafka.getStatus());
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerUpdateManageLanguageService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
