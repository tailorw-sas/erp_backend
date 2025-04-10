package com.kynsoft.finamer.invoicing.application.command.resourceType.create;


import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.invoicing.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.invoicing.domain.rules.resourceType.ResourceTypeCodeMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.rules.resourceType.ResourceTypeDefaultsMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.services.IManageResourceTypeService;
import org.springframework.stereotype.Component;

@Component
public class CreateManageResourceTypeCommandHandler implements ICommandHandler<CreateManageResourceTypeCommand> {

    private final IManageResourceTypeService service;

    public CreateManageResourceTypeCommandHandler(IManageResourceTypeService service) {
        this.service = service;
    }

    @Override
    public void handle(CreateManageResourceTypeCommand command) {
        RulesChecker.checkRule(new ResourceTypeCodeMustBeUniqueRule(this.service, command.getCode(), command.getId()));
        if (command.getDefaults() != null && command.getDefaults()) {
            RulesChecker.checkRule(new ResourceTypeDefaultsMustBeUniqueRule(this.service, command.getId()));
        }
        service.create(new ResourceTypeDto(
                command.getId(),
                command.getCode(),
                command.getName(),
                command.getDescription(),
                command.getStatus(),
                command.getDefaults(),
                command.isInvoice()
        ));
    }
}
