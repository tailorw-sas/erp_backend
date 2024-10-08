package com.kynsoft.finamer.creditcard.application.command.manageCollectionStatus.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreateManageCollectionStatusCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;


    @Override
    public ICommandMessage getMessage() {
        return new CreateManageCollectionStatusMessage(id);
    }
}
