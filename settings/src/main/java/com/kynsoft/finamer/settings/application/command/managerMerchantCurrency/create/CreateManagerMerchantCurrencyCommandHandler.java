package com.kynsoft.finamer.settings.application.command.managerMerchantCurrency.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsoft.finamer.settings.domain.dto.ManagerCurrencyDto;
import com.kynsoft.finamer.settings.domain.dto.ManagerMerchantCurrencyDto;
import com.kynsoft.finamer.settings.domain.dto.ManagerMerchantDto;
import com.kynsoft.finamer.settings.domain.rules.managerMerchantCurrency.ManagerMerchantCurrencyMustBeUniqueRule;
import com.kynsoft.finamer.settings.domain.services.IManagerCurrencyService;
import com.kynsoft.finamer.settings.domain.services.IManagerMerchantCurrencyService;
import com.kynsoft.finamer.settings.domain.services.IManagerMerchantService;
import org.springframework.stereotype.Component;

@Component
public class CreateManagerMerchantCurrencyCommandHandler implements ICommandHandler<CreateManagerMerchantCurrencyCommand> {

    private final IManagerMerchantService serviceMerchantService;
    private final IManagerCurrencyService serviceCurrencyService;
    private final IManagerMerchantCurrencyService serviceMerchantCurrency;

    public CreateManagerMerchantCurrencyCommandHandler(IManagerMerchantService serviceMerchantService,
                                                       IManagerCurrencyService serviceCurrencyService,
                                                       IManagerMerchantCurrencyService serviceMerchantCurrency) {
        this.serviceMerchantService = serviceMerchantService;
        this.serviceCurrencyService = serviceCurrencyService;
        this.serviceMerchantCurrency = serviceMerchantCurrency;
    }

    @Override
    public void handle(CreateManagerMerchantCurrencyCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getManagerCurrency(), "id", "Manager Currency ID cannot be null."));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getManagerMerchant(), "id", "Manager Merchant ID cannot be null."));

        ManagerCurrencyDto managerCurrencyDto = this.serviceCurrencyService.findById(command.getManagerCurrency());
        ManagerMerchantDto managerMerchantDto = this.serviceMerchantService.findById(command.getManagerMerchant());

        RulesChecker.checkRule(new ManagerMerchantCurrencyMustBeUniqueRule(this.serviceMerchantCurrency, command.getManagerMerchant(), command.getManagerCurrency()));

        serviceMerchantCurrency.create(new ManagerMerchantCurrencyDto(command.getId(), managerMerchantDto, managerCurrencyDto, command.getValue(), command.getDescription()));
    }
}
