package com.kynsoft.finamer.creditcard.application.command.manageCurrency.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.creditcard.domain.dto.ManagerCurrencyDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import com.kynsoft.finamer.creditcard.domain.services.IManagerCurrencyService;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UpdateManagerCurrencyCommandHandler implements ICommandHandler<UpdateManagerCurrencyCommand> {

    private final IManagerCurrencyService service;

    public UpdateManagerCurrencyCommandHandler(IManagerCurrencyService service) {
        this.service = service;
    }

    @Override
    public void handle(UpdateManagerCurrencyCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manager Currency ID cannot be null."));

        ManagerCurrencyDto test = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setName, command.getName(), test.getName(), update::setUpdate);
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
