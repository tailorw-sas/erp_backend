package com.kynsoft.finamer.creditcard.application.command.manageAttachmentType.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UpdateManageAttachmentTypeCommand implements ICommand {

    private UUID id;
    private String description;
    private Status status;
    private String name;
    private Boolean defaults;

    public static UpdateManageAttachmentTypeCommand fromRequest(UpdateManageAttachmentTypeRequest request, UUID id){
        return new UpdateManageAttachmentTypeCommand(
                id,
                request.getDescription(),
                request.getStatus(),
                request.getName(),
                request.getDefaults()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdateManageAttachmentTypeMessage(id);
    }
}
