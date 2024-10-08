package com.kynsoft.finamer.invoicing.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageAttachmentDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageAttachmentTypeDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dto.ResourceTypeDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageAttachmentResponse implements IResponse {
    private UUID id;
    private Long attachmentId;
    private Long resource;
    private String filename;
    private String file;
    private String remark;
    private ManageAttachmentTypeDto type;
    private ManageInvoiceDto invoice;
    private String employee;
    private UUID employeeId;
    private LocalDateTime createdAt;
    private ResourceTypeDto paymenResourceType;

    public ManageAttachmentResponse(ManageAttachmentDto dto) {
        this.id = dto.getId();
        this.remark = dto.getRemark();
        this.file = dto.getFile();
        this.filename = dto.getFilename();
        this.type = dto.getType();
        this.invoice = dto.getInvoice();
        this.attachmentId = dto.getAttachmentId();
        this.employee = dto.getEmployee();
        this.employeeId = dto.getEmployeeId();
        this.createdAt = dto.getCreatedAt();
        this.paymenResourceType = dto.getPaymentResourceType();
    }
}
