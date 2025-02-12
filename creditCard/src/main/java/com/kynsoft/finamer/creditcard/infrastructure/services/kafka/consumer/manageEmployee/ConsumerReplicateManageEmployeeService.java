package com.kynsoft.finamer.creditcard.infrastructure.services.kafka.consumer.manageEmployee;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageEmployeeKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.manageEmployee.create.CreateManageEmployeeCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageEmployeeService {

    private final IMediator mediator;

    public ConsumerReplicateManageEmployeeService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-employee", groupId = "vcc-entity-replica")
    public void listen(ReplicateManageEmployeeKafka objKafka) {
        try {
            CreateManageEmployeeCommand command = new CreateManageEmployeeCommand(
                    objKafka.getId(), 
                    objKafka.getFirstName(), 
                    objKafka.getLastName(), 
                    objKafka.getEmail(),
                    objKafka.getManageAgencyList(),
                    objKafka.getManageHotelList()
            );
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerReplicateManageEmployeeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
