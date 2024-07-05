package com.kynsoft.finamer.payment.application.command.managePaymentStatus.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateManagePaymentStatusCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;

    public CreateManagePaymentStatusCommand(UUID id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManagePaymentStatusMessage(id);
    }
}
