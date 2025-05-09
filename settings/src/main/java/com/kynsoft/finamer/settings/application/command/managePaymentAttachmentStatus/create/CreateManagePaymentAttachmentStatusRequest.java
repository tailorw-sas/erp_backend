package com.kynsoft.finamer.settings.application.command.managePaymentAttachmentStatus.create;

import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateManagePaymentAttachmentStatusRequest {
    private String code;
    private String name;
    private Status status;
    private List<UUID> navigate;
    private UUID module;
    private Boolean show;
    private Boolean defaults;
    private String permissionCode;
    private String description;
    private boolean nonNone;
    private boolean patWithAttachment;
    private boolean pwaWithOutAttachment;
    private boolean supported;
    private boolean otherSupport;
}
