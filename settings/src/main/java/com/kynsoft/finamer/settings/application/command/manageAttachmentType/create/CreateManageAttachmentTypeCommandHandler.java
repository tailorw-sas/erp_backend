package com.kynsoft.finamer.settings.application.command.manageAttachmentType.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.ReplicateManageAttachmentTypeKafka;
import com.kynsoft.finamer.settings.domain.dto.ManageAttachmentTypeDto;
import com.kynsoft.finamer.settings.domain.rules.manageAttachmentType.*;
import com.kynsoft.finamer.settings.domain.services.IManageAttachmentTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAttachmentType.ProducerReplicateManageAttachmentTypeService;
import org.springframework.stereotype.Component;

@Component
public class CreateManageAttachmentTypeCommandHandler implements ICommandHandler<CreateManageAttachmentTypeCommand> {

    private final IManageAttachmentTypeService service;
    private final ProducerReplicateManageAttachmentTypeService producerReplicateManageAttachmentTypeService;

    public CreateManageAttachmentTypeCommandHandler(IManageAttachmentTypeService service,
                                                    ProducerReplicateManageAttachmentTypeService producerReplicateManageAttachmentTypeService) {
        this.service = service;
        this.producerReplicateManageAttachmentTypeService = producerReplicateManageAttachmentTypeService;
    }

    @Override
    public void handle(CreateManageAttachmentTypeCommand command) {
        RulesChecker.checkRule(new ManageAttachmentTypeCodeSizeRule(command.getCode()));
        RulesChecker.checkRule(new ManageAttachmentTypeNameMustBeNullRule(command.getName()));
        RulesChecker.checkRule(new ManageAttachmentTypeCodeMustBeUniqueRule(this.service, command.getCode(), command.getId()));

        if(command.isDefaults()) {
            RulesChecker.checkRule(new ManageAttachmentTypeDefaultMustBeUniqueRule(this.service, command.getId()));
        }

        if(command.isAttachInvDefault()) {
            RulesChecker.checkRule(new ManageAttachmentTypeInvDefaultMustBeUniqueRule(this.service, command.getId()));
        }

        service.create(new ManageAttachmentTypeDto(
                command.getId(),
                command.getCode(),
                command.getDescription(),
                command.getStatus(),
                command.getName(),
                command.isDefaults(),
                command.isAttachInvDefault()
        ));
        this.producerReplicateManageAttachmentTypeService.create(new ReplicateManageAttachmentTypeKafka(command.getId(), command.getCode(), command.getName(),command.getStatus().toString(), command.isDefaults(), command.isAttachInvDefault()));
    }
}
