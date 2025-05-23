package com.kynsoft.finamer.payment.application.command.resourceType.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.ReplicatePaymentResourceTypeKafka;
import com.kynsof.share.core.domain.kafka.entity.update.UpdatePaymentResourceTypeKafka;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.payment.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.rules.resourceType.ResourceDefaultMustBeUniqueRule;
import com.kynsoft.finamer.payment.domain.rules.resourceType.ResourceInvoiceMustBeUniqueRule;
import com.kynsoft.finamer.payment.domain.rules.resourceType.ResourceVccMustBeUniqueRule;
import com.kynsoft.finamer.payment.domain.services.IManageResourceTypeService;
import com.kynsoft.finamer.payment.infrastructure.services.kafka.producer.resourceType.ProducerReplicateResourceTypeService;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UpdateManageResourceTypeCommandHandler implements ICommandHandler<UpdateManageResourceTypeCommand> {

    private final IManageResourceTypeService service;

    private final ProducerReplicateResourceTypeService producer;

    public UpdateManageResourceTypeCommandHandler(IManageResourceTypeService service, ProducerReplicateResourceTypeService producer) {
        this.service = service;
        this.producer = producer;
    }

    @Override
    public void handle(UpdateManageResourceTypeCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manage Resource Type ID cannot be null."));
        if (command.getDefaults()) {
            RulesChecker.checkRule(new ResourceDefaultMustBeUniqueRule(this.service, command.getId()));
        }

        if (command.isInvoice()) {
            RulesChecker.checkRule(new ResourceInvoiceMustBeUniqueRule(this.service, command.getId()));
        }
        if (command.isVcc()) {
            RulesChecker.checkRule(new ResourceVccMustBeUniqueRule(this.service, command.getId()));
        }
        ResourceTypeDto resourceTypeDto = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(resourceTypeDto::setDescription, command.getDescription(), resourceTypeDto.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(resourceTypeDto::setName, command.getName(), resourceTypeDto.getName(), update::setUpdate);
        this.updateStatus(resourceTypeDto::setStatus, command.getStatus(), resourceTypeDto.getStatus(), update::setUpdate);
        this.updateBooleam(resourceTypeDto::setDefaults, command.getDefaults(), resourceTypeDto.getDefaults(), update::setUpdate);
        this.updateBooleam(resourceTypeDto::setInvoice, command.isInvoice(), resourceTypeDto.isInvoice(), update::setUpdate);
        this.updateBooleam(resourceTypeDto::setVcc, command.isVcc(), resourceTypeDto.isVcc(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.service.update(resourceTypeDto);
            this.producer.create(new ReplicatePaymentResourceTypeKafka(
                    resourceTypeDto.getId(), 
                    resourceTypeDto.getCode(), 
                    resourceTypeDto.getName(), 
                    resourceTypeDto.getDescription(),
                    resourceTypeDto.isInvoice(), 
                    resourceTypeDto.getDefaults(),
                    resourceTypeDto.isVcc(), 
                    resourceTypeDto.getStatus().name()
            ));
        }

    }

    private void updateStatus(Consumer<Status> setter, Status newValue, Status oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);

        }
    }

    private void updateBooleam(Consumer<Boolean> setter, Boolean newValue, Boolean oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);

        }
    }

}
