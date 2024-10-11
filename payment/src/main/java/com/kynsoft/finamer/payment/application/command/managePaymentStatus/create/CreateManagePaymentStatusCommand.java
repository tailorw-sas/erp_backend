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
    private String status;
    private Boolean applied;
    private boolean confirmed;

    public CreateManagePaymentStatusCommand(UUID id, String code, String name, String status, Boolean applied, boolean confirmed) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.applied = applied;
        this.confirmed = confirmed;
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManagePaymentStatusMessage(id);
    }
}
