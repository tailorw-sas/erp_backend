package com.kynsoft.finamer.creditcard.infrastructure.services.kafka.consumer.manageHotel;

import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageHotelKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.manageHotel.update.UpdateManageHotelCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUpdateManageHotelService {

    private final IMediator mediator;

    public ConsumerUpdateManageHotelService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-update-manage-hotel", groupId = "vcc-entity-replica")
    public void listen(UpdateManageHotelKafka objKafka) {
        try {
            UpdateManageHotelCommand command = new UpdateManageHotelCommand(objKafka.getId(), objKafka.getName(), objKafka.getIsApplyByVCC(), objKafka.getStatus());
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerUpdateManageHotelService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
