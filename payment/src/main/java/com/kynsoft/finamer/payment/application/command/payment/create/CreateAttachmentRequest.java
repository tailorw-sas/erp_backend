package com.kynsoft.finamer.payment.application.command.payment.create;

import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAttachmentRequest {

    private Status status;
    private UUID employee;
    private UUID resourceType;
    private UUID attachmentType;
    private String fileName;
    private String fileWeight;
    private String path;
    private String remark;
}