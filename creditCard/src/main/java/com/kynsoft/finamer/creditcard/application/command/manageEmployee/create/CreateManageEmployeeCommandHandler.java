package com.kynsoft.finamer.creditcard.application.command.manageEmployee.create;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.creditcard.domain.dto.ManageEmployeeDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class CreateManageEmployeeCommandHandler implements ICommandHandler<CreateManageEmployeeCommand> {

    private final IManageEmployeeService service;

    public CreateManageEmployeeCommandHandler(IManageEmployeeService service) {
        this.service = service;
    }

    @Override
    public void handle(CreateManageEmployeeCommand command) {
    
        service.create(new ManageEmployeeDto(
                command.getId(),
                command.getFirstName(),
                command.getLastName(),
                command.getEmail()
        ));

    }
}
