package com.kynsoft.finamer.invoicing.domain.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManageAttachmentDto {
    private UUID id;
    private Long attachment_id;
    private String filename;
    private String file;
    private String remark;
    private ManageAttachmentTypeDto type;
    private ManageInvoiceDto invoice;

}
