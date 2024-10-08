package com.kynsoft.finamer.settings.application.command.manageReport.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateManageReportCommand implements ICommand {

    private UUID id;
    private String code;
    private String description;
    private Status status;
    private String name;
    private String moduleId;
    private String moduleName;

    public CreateManageReportCommand(String code, String description, Status status, String name, String moduleId, String moduleName) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.description = description;
        this.status = status;
        this.name = name;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public static CreateManageReportCommand fromRequest(CreateManageReportRequest request){
        return new CreateManageReportCommand(
                request.getCode(),
                request.getDescription(),
                request.getStatus(),
                request.getName(),
                request.getModuleId(),
                request.getModuleName()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManageReportMessage(id);
    }

}
