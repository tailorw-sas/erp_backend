package com.kynsoft.finamer.creditcard.application.command.manageMerchant.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateFields;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.creditcard.domain.dto.ManagerB2BPartnerDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManageMerchantDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import com.kynsoft.finamer.creditcard.domain.rules.manageMerchant.ManageMerchantDefaultMustBeUniqueRule;
import com.kynsoft.finamer.creditcard.domain.services.IManagerB2BPartnerService;
import com.kynsoft.finamer.creditcard.domain.services.IManageMerchantService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UpdateManageMerchantCommandHandler implements ICommandHandler<UpdateManageMerchantCommand> {

    private final IManageMerchantService service;
    private final IManagerB2BPartnerService serviceB2BPartner;

    public UpdateManageMerchantCommandHandler(IManageMerchantService service,
                                              IManagerB2BPartnerService serviceB2BPartner) {
        this.service = service;
        this.serviceB2BPartner = serviceB2BPartner;
    }

    @Override
    public void handle(UpdateManageMerchantCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manage Merchant ID cannot be null."));

        if (command.getDefaultm()){
            RulesChecker.checkRule(new ManageMerchantDefaultMustBeUniqueRule(this.service, command.getId()));
        }

        ManageMerchantDto test = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateFields.updateString(test::setDescription, command.getDescription(), test.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setCode, command.getCode(), test.getCode(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(test::setDefaultm, command.getDefaultm(), test.getDefaultm(), update::setUpdate);

        this.updateStatus(test::setStatus, command.getStatus(), test.getStatus(), update::setUpdate);
        this.updateB2BPartner(test::setB2bPartner, command.getB2bPartner(), test.getB2bPartner().getId(), update::setUpdate);

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

    private boolean updateB2BPartner(Consumer<ManagerB2BPartnerDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManagerB2BPartnerDto b2BPartnerDto = this.serviceB2BPartner.findById(newValue);
            setter.accept(b2BPartnerDto);
            update.accept(1);

            return true;
        }
        return false;
    }

}
