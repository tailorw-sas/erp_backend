package com.kynsoft.finamer.invoicing.application.command.manageAttachment.delete;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeleteAttachmentCommand implements ICommand {

    private UUID id;
    private UUID employee;

    @Override
    public ICommandMessage getMessage() {
        return new DeleteAttachmentMessage(id);
    }

}
