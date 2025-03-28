package com.kynsoft.finamer.settings.application.command.manageCollectionStatus.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageCollectionStatusKafka;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateFields;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.settings.domain.dto.ManageCollectionStatusDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.rules.manageCollectionStatus.ManageCollectionStatusNameMustBeUniqueRule;
import com.kynsoft.finamer.settings.domain.services.IManageCollectionStatusService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageCollectionStatus.ProducerUpdateManageCollectionStatusService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UpdateManageCollectionStatusCommandHandler implements ICommandHandler<UpdateManageCollectionStatusCommand> {

    private final IManageCollectionStatusService service;

    private final ProducerUpdateManageCollectionStatusService producerService;

    public UpdateManageCollectionStatusCommandHandler(IManageCollectionStatusService service, ProducerUpdateManageCollectionStatusService producerService) {
        this.service = service;
        this.producerService = producerService;
    }

    @Override
    public void handle(UpdateManageCollectionStatusCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manage Collection Status ID cannot be null."));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getName(), "id", "Manage Collection Status Name cannot be null."));
        RulesChecker.checkRule(new ManageCollectionStatusNameMustBeUniqueRule(service, command.getName(), command.getId()));

        ManageCollectionStatusDto dto = service.findById(command.getId());
        ConsumerUpdate update = new ConsumerUpdate();

        UpdateFields.updateString(dto::setDescription, command.getDescription(), dto.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setName, command.getName(), dto.getName(), update::setUpdate);
        updateStatus(dto::setStatus, command.getStatus(), dto.getStatus(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setEnabledPayment, command.getEnabledPayment(), dto.getEnabledPayment(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setIsVisible, command.getIsVisible(), dto.getIsVisible(), update::setUpdate);

        updateNavigates(dto::setNavigate, command.getNavigate(), dto.getNavigate().stream().map(ManageCollectionStatusDto::getId).toList(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.service.update(dto);
            this.producerService.update(new UpdateManageCollectionStatusKafka(dto.getId(), dto.getName()));
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

    private boolean updateNavigates(Consumer<List<ManageCollectionStatusDto>> setter, List<UUID> newValue, List<UUID> oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            List<ManageCollectionStatusDto> manageCollectionStatusDtos = service.findByIds(newValue);
            setter.accept(manageCollectionStatusDtos);
            update.accept(1);

            return true;
        }
        return false;
    }
}
