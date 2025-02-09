package com.kynsoft.finamer.creditcard.application.command.parameterization.create;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.creditcard.domain.dto.ParameterizationDto;
import com.kynsoft.finamer.creditcard.domain.services.IParameterizationService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CreateParameterizationCommandHandler implements ICommandHandler<CreateParameterizationCommand> {

    private final IParameterizationService service;

    public CreateParameterizationCommandHandler(IParameterizationService service) {
        this.service = service;
    }

    @Override
    public void handle(CreateParameterizationCommand command) {
        this.service.create(new ParameterizationDto(
                command.getId(), true, command.getDecimals()
        ));
    }
}
