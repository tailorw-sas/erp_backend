package com.kynsoft.finamer.settings.application.command.managerLanguage.delete;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.settings.application.command.managerLanguage.create.CreateManagerLanguageMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class DeleteManagerLanguageCommand implements ICommand {

    private UUID id;
    @Override
    public ICommandMessage getMessage() {
        return new DeleteManagerLanguageMessage(id);
    }
}
