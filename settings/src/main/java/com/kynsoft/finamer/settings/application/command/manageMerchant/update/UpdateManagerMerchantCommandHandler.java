package com.kynsoft.finamer.settings.application.command.manageMerchant.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.update.UpdateManageMerchantKafka;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.settings.domain.dto.ManagerB2BPartnerDto;
import com.kynsoft.finamer.settings.domain.dto.ManagerMerchantDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.services.IManagerB2BPartnerService;
import com.kynsoft.finamer.settings.domain.services.IManagerMerchantService;
import java.util.UUID;
import java.util.function.Consumer;

import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageMerchant.ProducerUpdateManageMerchantService;
import org.springframework.stereotype.Component;

@Component
public class UpdateManagerMerchantCommandHandler implements ICommandHandler<UpdateManagerMerchantCommand> {

    private final IManagerMerchantService service;
    private final IManagerB2BPartnerService serviceB2BPartner;

    private final ProducerUpdateManageMerchantService producerService;

    public UpdateManagerMerchantCommandHandler(IManagerMerchantService service,
                                               IManagerB2BPartnerService serviceB2BPartner, ProducerUpdateManageMerchantService producerService) {
        this.service = service;
        this.serviceB2BPartner = serviceB2BPartner;
        this.producerService = producerService;
    }

    @Override
    public void handle(UpdateManagerMerchantCommand command) {

        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "Manager Merchant ID cannot be null."));

        ManagerMerchantDto test = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setDescription, command.getDescription(), test.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(test::setCode, command.getCode(), test.getCode(), update::setUpdate);
        UpdateIfNotNull.updateBoolean(test::setDefaultm, command.getDefaultm(), test.getDefaultm(), update::setUpdate);

        this.updateStatus(test::setStatus, command.getStatus(), test.getStatus(), update::setUpdate);
        this.updateB2BPartner(test::setB2bPartner, command.getB2bPartner(), test.getB2bPartner().getId(), update::setUpdate);


            this.service.update(test);
            this.producerService.update(new UpdateManageMerchantKafka(test.getId(), test.getCode(), test.getDescription(),test.getB2bPartner().getId(),test.getDefaultm(),test.getStatus().name()));

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
