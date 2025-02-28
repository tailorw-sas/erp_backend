package com.kynsoft.finamer.insis.application.command.manageRatePlan.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.insis.application.command.manageRatePlan.create.CreateRatePlanMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateRatePlanCommand implements ICommand {

    private UUID id;
    private String name;
    private String status;
    private LocalDateTime updatedAt;

    public UpdateRatePlanCommand(UUID id, String name, String status){
        this.id = id;
        this.name = name;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateRatePlanMessage(id);
    }
}
