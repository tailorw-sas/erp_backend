package com.kynsoft.finamer.creditcard.application.command.attachment.delete;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteAttachmentMessage implements ICommandMessage {

    private final UUID id;

    private final String command = "DELETE_ATTACHMENT";

    public DeleteAttachmentMessage(UUID id) {
        this.id = id;
    }

}
