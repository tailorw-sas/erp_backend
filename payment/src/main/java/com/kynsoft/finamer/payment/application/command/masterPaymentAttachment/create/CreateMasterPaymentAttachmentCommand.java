package com.kynsoft.finamer.payment.application.command.masterPaymentAttachment.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateMasterPaymentAttachmentCommand implements ICommand {

    private UUID id;
    private Status status;
    private UUID resource;
    private UUID resourceType;
    private UUID attachmentType;
    private String fileName;
    private String path;
    private String remark;

    public CreateMasterPaymentAttachmentCommand(Status status, UUID resource, UUID resourceType, 
                                                UUID attachmentType, String fileNames, String paths, 
                                                String remark) {
        this.id = UUID.randomUUID();
        this.status = status;
        this.resource = resource;
        this.resourceType = resourceType;
        this.attachmentType = attachmentType;
        this.fileName = fileNames;
        this.path = paths;
        this.remark = remark;
    }

    public static CreateMasterPaymentAttachmentCommand fromRequest(CreateMasterPaymentAttachmentRequest request) {
        return new CreateMasterPaymentAttachmentCommand(
                request.getStatus(),
                request.getResource(),
                request.getResourceType(),
                request.getAttachmentType(),
                request.getFileName(),
                request.getPath(),
                request.getRemark()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateMasterPaymentAttachmentMessage(id);
    }
}
