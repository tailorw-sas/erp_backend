package com.kynsoft.finamer.payment.domain.dto;

import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MasterPaymentAttachmentDto {

    private UUID id;
    private Status status;
    private PaymentDto resource;
    private ResourceTypeDto resourceType;
    private AttachmentTypeDto attachmentType;
    private String fileName;
    private String fileWeight;
    private String path;
    private String remark;
    private Long attachmentId;
    private String statusHistory;

    public MasterPaymentAttachmentDto(UUID id, Status status, PaymentDto resource, ResourceTypeDto resourceType, AttachmentTypeDto attachmentType, String fileName, String fileWeight, String path, String remark, Long attachmentId) {
        this.id = id;
        this.status = status;
        this.resource = resource;
        this.resourceType = resourceType;
        this.attachmentType = attachmentType;
        this.fileName = fileName;
        this.fileWeight = fileWeight;
        this.path = path;
        this.remark = remark;
        this.attachmentId = attachmentId;
    }
}
