package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.consumer.manageAgency;

import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageAgencyKafka;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.manageAgency.update.UpdateManageAgencyCommand;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EGenerationType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUpdateManageAgencyService {

    private final IMediator mediator;

    public ConsumerUpdateManageAgencyService(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-update-manage-agency", groupId = "invoicing-entity-replica")
    public void listen(UpdateManageAgencyKafka objKafka) {
        try {
            UpdateManageAgencyCommand command = new UpdateManageAgencyCommand(objKafka.getId(),
                    objKafka.getName(),
                    objKafka.getClient(),objKafka.getCif(),
                    objKafka.getAddress(),objKafka.getSentB2BPartner(),
                    objKafka.getCityState(),objKafka.getCountry(),
                    objKafka.getMailingAddress(),
                    objKafka.getZipCode(),
                    objKafka.getCity(), objKafka.getCreditDay(),
                    objKafka.getAutoReconcile(),
                    objKafka.getValidateCheckout(),
                    EGenerationType.valueOf(objKafka.getGenerationType()),
                    objKafka.getBookingCouponFormat()
            );
            mediator.send(command);
        } catch (Exception ex) {
            Logger.getLogger(ConsumerUpdateManageAgencyService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
