package com.kynsoft.finamer.invoicing.application.command.resourceType.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateManageResourceTypeCommand implements ICommand {

    private UUID id;
    private String name;
    private String description;
    private Status status;
    private Boolean defaults;
    private boolean invoice;

    public static UpdateManageResourceTypeCommand fromRequest(UpdateManageResourceTypeRequest request, UUID id) {
        return new UpdateManageResourceTypeCommand(
                id,
                request.getName(),
                request.getDescription(),
                request.getStatus(),
                request.getDefaults(),
                request.isInvoice()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdateManageResourceTypeMessage(id);
    }
}
