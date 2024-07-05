package com.kynsoft.finamer.payment.infrastructure.services.kafka.consumer.manageHotel;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageHotelKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.manageHotel.create.CreateManageHotelCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageHotelService {

    private final IMediator mediator;

    public ConsumerReplicateManageHotelService(IMediator mediator) {

        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-hotel", groupId = "payment-entity-replica")
    public void listen(ReplicateManageHotelKafka objKafka) {
        try {
            CreateManageHotelCommand command = new CreateManageHotelCommand(objKafka.getId(), objKafka.getCode(), objKafka.getName());
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerReplicateManageHotelService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
