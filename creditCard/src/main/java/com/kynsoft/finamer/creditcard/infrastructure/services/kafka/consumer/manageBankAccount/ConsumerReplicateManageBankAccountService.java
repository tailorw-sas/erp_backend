package com.kynsoft.finamer.creditcard.infrastructure.services.kafka.consumer.manageBankAccount;

import com.kynsof.share.core.domain.kafka.entity.ReplicateManageBankAccountKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.creditcard.application.command.manageBankAccount.create.CreateManageBankAccountCommand;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerReplicateManageBankAccountService {

    private final IMediator mediator;

    public ConsumerReplicateManageBankAccountService(IMediator mediator) {

        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-replicate-manage-bank-account", groupId = "vcc-entity-replica")
    public void listen(ReplicateManageBankAccountKafka objKafka) {
        try {
            CreateManageBankAccountCommand command = new CreateManageBankAccountCommand(
                    objKafka.getId(), Status.valueOf(objKafka.getStatus()), objKafka.getAccountNumber(),
                    objKafka.getManageBank(), objKafka.getManageHotel(), objKafka.getManageAccountType(),
                    objKafka.getDescription()
            );
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerReplicateManageBankAccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
