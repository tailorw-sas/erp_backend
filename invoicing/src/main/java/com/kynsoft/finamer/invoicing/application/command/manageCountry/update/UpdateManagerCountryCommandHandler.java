package com.kynsoft.finamer.invoicing.application.command.manageCountry.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.invoicing.domain.dto.ManagerCountryDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.rules.managerCountry.ManagerCountryNameMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.services.IManagerCountryService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UpdateManagerCountryCommandHandler implements ICommandHandler<UpdateManagerCountryCommand> {

    private final IManagerCountryService service;

    public UpdateManagerCountryCommandHandler(IManagerCountryService service) {
        this.service = service;
    }

    @Override
    public void handle(UpdateManagerCountryCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manager Country ID cannot be null."));

        ManagerCountryDto test = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setDescription, command.getDescription(), test.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(test::setIsDefault, command.getIsDefault(), test.getIsDefault(), update::setUpdate);

        if (UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setName, command.getName(), test.getName(), update::setUpdate)) {
            RulesChecker.checkRule(new ManagerCountryNameMustBeUniqueRule(this.service, command.getName(), command.getId()));
        }
        //Si el Dial Code cambio, hay que verificar que este correcto


        this.updateStatus(test::setStatus, command.getStatus(), test.getStatus(), update::setUpdate);

        if (update.getUpdate() > 0) {
            this.service.update(test);
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
