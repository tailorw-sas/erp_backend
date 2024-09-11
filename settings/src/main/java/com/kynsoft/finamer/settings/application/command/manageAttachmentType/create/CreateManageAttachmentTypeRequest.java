package com.kynsoft.finamer.settings.application.command.manageAttachmentType.create;

import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateManageAttachmentTypeRequest {

    private String code;
    private String description;
    private Status status;
    private String name;
    private Boolean defaults;
    private Boolean attachInvDefault;
}
