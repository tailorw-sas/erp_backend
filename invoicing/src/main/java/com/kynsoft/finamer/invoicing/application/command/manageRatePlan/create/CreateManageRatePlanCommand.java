package com.kynsoft.finamer.invoicing.application.command.manageRatePlan.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateManageRatePlanCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private UUID hotelId;

    @Override
    public ICommandMessage getMessage() {
        return new CreateManageRatePlanMessage(id);
    }
}
