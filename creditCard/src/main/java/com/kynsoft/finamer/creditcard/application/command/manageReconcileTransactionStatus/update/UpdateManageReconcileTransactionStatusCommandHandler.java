package com.kynsoft.finamer.creditcard.application.command.manageReconcileTransactionStatus.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateFields;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.creditcard.domain.dto.ManageReconcileTransactionStatusDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import com.kynsoft.finamer.creditcard.domain.rules.manageReconcileTransactionStatus.ManageReconcileTransactionStatusCancelledMustBeUniqueRule;
import com.kynsoft.finamer.creditcard.domain.rules.manageReconcileTransactionStatus.ManageReconcileTransactionStatusCompletedMustBeUniqueRule;
import com.kynsoft.finamer.creditcard.domain.rules.manageReconcileTransactionStatus.ManageReconcileTransactionStatusCreatedMustBeUniqueRule;
import com.kynsoft.finamer.creditcard.domain.services.IManageReconcileTransactionStatusService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Component
public class UpdateManageReconcileTransactionStatusCommandHandler implements ICommandHandler<UpdateManageReconcileTransactionStatusCommand> {

    private final IManageReconcileTransactionStatusService service;

    public UpdateManageReconcileTransactionStatusCommandHandler(IManageReconcileTransactionStatusService service) {
        this.service = service;
    }

    @Override
    public void handle(UpdateManageReconcileTransactionStatusCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Module ID cannot be null."));

        if (command.isCreated()){
            RulesChecker.checkRule(new ManageReconcileTransactionStatusCreatedMustBeUniqueRule(this.service, command.getId()));
        }
        if (command.isCancelled()){
            RulesChecker.checkRule(new ManageReconcileTransactionStatusCancelledMustBeUniqueRule(this.service, command.getId()));
        }
        if (command.isCompleted()){
            RulesChecker.checkRule(new ManageReconcileTransactionStatusCompletedMustBeUniqueRule(this.service, command.getId()));
        }

        ManageReconcileTransactionStatusDto dto = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();


        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setName, command.getName(), dto.getName(), update::setUpdate);
        UpdateFields.updateString(dto::setDescription, command.getDescription(), dto.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setRequireValidation, command.getRequireValidation(), dto.getRequireValidation(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setCreated, command.isCreated(), dto.isCreated(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setCancelled, command.isCancelled(), dto.isCancelled(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(dto::setCompleted, command.isCompleted(), dto.isCompleted(), update::setUpdate);

        List<ManageReconcileTransactionStatusDto> manageReconcileTransactionStatusDtoList = service.findByIds(command.getNavigate());


        this.updateRelatedStatus(dto::setRelatedStatuses, manageReconcileTransactionStatusDtoList, update::setUpdate);
        this.updateStatus(dto::setStatus, command.getStatus(), dto.getStatus(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.service.update(dto);
        }
    }

    private boolean updateRelatedStatus(Consumer<List<ManageReconcileTransactionStatusDto>> setter, List<ManageReconcileTransactionStatusDto> newValue, Consumer<Integer> update) {

        setter.accept(newValue);
        update.accept(1);
        return true;
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
