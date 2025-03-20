package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.consumer.managePaymentTransactionType;

import com.kynsof.share.core.domain.kafka.entity.update.UpdateManagePaymentTransactionTypeKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.managePaymentTransactionType.update.UpdateManagePaymentTransactionTypeCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUpdateManageTransactionTypeService {

    private final IMediator mediator;

    public ConsumerUpdateManageTransactionTypeService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-update-manage-payment-transaction-type", groupId = "invoicing-entity-replica")
    public void listen(UpdateManagePaymentTransactionTypeKafka objKafka) {
        try {
            UpdateManagePaymentTransactionTypeCommand command = new UpdateManagePaymentTransactionTypeCommand(
                    objKafka.getId(), 
                    objKafka.getName(),
                    objKafka.getStatus(),
                    objKafka.getCash(),
                    objKafka.getDeposit(),
                    objKafka.getApplyDeposit(),
                    objKafka.getRemarkRequired(),
                    objKafka.getMinNumberOfCharacter(),
                    objKafka.getDefaultRemark(),
                    objKafka.getDefaults(),
                    objKafka.getNegative()
            );
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerUpdateManageTransactionTypeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
