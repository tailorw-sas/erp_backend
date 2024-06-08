package com.kynsoft.finamer.settings.application.command.managerLanguage.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateManagerLanguageCommand implements ICommand {

    private UUID id;
    private String code;
    private String description;
    private Status status;
    private String name;
    private Boolean isEnabled;

    public CreateManagerLanguageCommand(String code, String description, Status status, String name, Boolean isEnabled) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.description = description;
        this.status = status;
        this.name = name;
        this.isEnabled = isEnabled;
    }

    public static CreateManagerLanguageCommand fromRequest(CreateManagerLanguageRequest request){
        return new CreateManagerLanguageCommand(
                request.getCode(),
                request.getDescription(),
                request.getStatus(),
                request.getName(),
                request.getIsEnabled()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManagerLanguageMessage(id);
    }
}
