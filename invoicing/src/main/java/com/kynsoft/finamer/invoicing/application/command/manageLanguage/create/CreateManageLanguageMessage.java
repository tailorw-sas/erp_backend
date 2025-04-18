package com.kynsoft.finamer.invoicing.application.command.manageLanguage.create;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateManageLanguageMessage implements ICommandMessage {

    private final UUID id;

    private final String command = "CREATE_LANGUAGE";

    public CreateManageLanguageMessage(UUID id) {
        this.id = id;
    }
}
