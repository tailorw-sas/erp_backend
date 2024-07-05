package com.kynsoft.finamer.settings.application.command.manageAttachmentType.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageAttachmentTypeKafka;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.settings.domain.dto.ManageAttachmentTypeDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.services.IManageAttachmentTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAttachmentType.ProducerUpdateManageAttachmentTypeService;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UpdateManageAttachmentTypeCommandHandler implements ICommandHandler<UpdateManageAttachmentTypeCommand> {

    private final IManageAttachmentTypeService service;
    private final ProducerUpdateManageAttachmentTypeService producerUpdateManageAttachmentTypeService;

    public UpdateManageAttachmentTypeCommandHandler(IManageAttachmentTypeService service,
                                                    ProducerUpdateManageAttachmentTypeService producerUpdateManageAttachmentTypeService) {
        this.service = service;
        this.producerUpdateManageAttachmentTypeService = producerUpdateManageAttachmentTypeService;
    }

    @Override
    public void handle(UpdateManageAttachmentTypeCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manage Attachment Type ID cannot be null."));

        ManageAttachmentTypeDto dto = service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setDescription, command.getDescription(), dto.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setName, command.getName(), dto.getName(), update::setUpdate);
        updateStatus(dto::setStatus, command.getStatus(), dto.getStatus(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.service.update(dto);
            this.producerUpdateManageAttachmentTypeService.update(new UpdateManageAttachmentTypeKafka(dto.getId(), dto.getName()));
        }
    }

    private boolean updateStatus(Consumer<Status> setter, Status newValue, Status oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);

            return true;
        }
        return false;
    }
}
