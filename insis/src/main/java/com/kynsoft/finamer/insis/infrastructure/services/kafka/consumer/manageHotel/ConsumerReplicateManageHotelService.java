package com.kynsoft.finamer.insis.infrastructure.services.kafka.consumer.manageHotel;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageHotelKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.insis.application.command.manageHotel.create.CreateHotelCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageHotelService {
    private final IMediator mediator;

    public ConsumerReplicateManageHotelService(IMediator mediator){
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-hotel", groupId = "innsist-entity-replica")
    public void listen(ReplicateManageHotelKafka entity){
        try{
            CreateHotelCommand command = new CreateHotelCommand(
                entity.getId(),
                    entity.getCode(),
                    entity.getName(),
                    entity.getStatus(),
                    entity.getManageTradingCompany()
            );
            mediator.send(command);
        }catch (Exception ex){
            Logger.getLogger(ConsumerReplicateManageHotelService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
