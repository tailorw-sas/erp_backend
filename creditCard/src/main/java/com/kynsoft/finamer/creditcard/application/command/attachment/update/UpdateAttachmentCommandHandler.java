package com.kynsoft.finamer.creditcard.application.command.attachment.update;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.creditcard.domain.dto.AttachmentDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManageAttachmentTypeDto;
import com.kynsoft.finamer.creditcard.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.creditcard.domain.services.IAttachmentService;
import com.kynsoft.finamer.creditcard.domain.services.IManageAttachmentTypeService;
import com.kynsoft.finamer.creditcard.domain.services.IManageResourceTypeService;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Consumer;

@Component
public class UpdateAttachmentCommandHandler implements ICommandHandler<UpdateAttachmentCommand> {

    private final IAttachmentService attachmentService;
    private final IManageAttachmentTypeService attachmentTypeService;
    private final IManageResourceTypeService resourceTypeService;

    public UpdateAttachmentCommandHandler(IAttachmentService attachmentService,
                                          IManageAttachmentTypeService attachmentTypeService, IManageResourceTypeService resourceTypeService) {
        this.attachmentService = attachmentService;
        this.attachmentTypeService = attachmentTypeService;
        this.resourceTypeService = resourceTypeService;
    }

    @Override
    public void handle(UpdateAttachmentCommand command) {
        AttachmentDto dto = this.attachmentService.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();

        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setFile, command.getFile(), dto.getFile(),
                update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setRemark, command.getRemark(), dto.getRemark(),
                update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(dto::setFilename, command.getFilename(),
                dto.getFilename(), update::setUpdate);

        if (command.getType() != null) {
            this.updateType(dto::setType, command.getType(), dto.getType().getId(), update::setUpdate);
        }

        if(command.getPaymentResourceType() != null){
            this.updatePaymentResourceType(dto::setPaymentResourceType, command.getPaymentResourceType(), dto.getPaymentResourceType() != null ? dto.getPaymentResourceType().getId(): null, update::setUpdate);
        }

        if (update.getUpdate() > 0) {
            this.attachmentService.update(dto);
//            this.updateAttachmentStatusHistory(dto.getInvoice() , dto.getFilename(), dto.getAttachmentId(), dto.getEmployee(), dto.getEmployeeId());
        }
    }

    public void updateType(Consumer<ManageAttachmentTypeDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            ManageAttachmentTypeDto attachmentTypeDto = this.attachmentTypeService.findById(newValue);
            setter.accept(attachmentTypeDto);
            update.accept(1);
        }
    }

    public void updatePaymentResourceType(Consumer<ResourceTypeDto> setter, UUID newValue, UUID oldValue,
                                          Consumer<Integer> update){
        if (newValue != null && !newValue.equals(oldValue)) {
            ResourceTypeDto resourceTypeDto = this.resourceTypeService.findById(newValue);
            setter.accept(resourceTypeDto);
            update.accept(1);
        }
    }

//     private void updateAttachmentStatusHistory( ManageInvoiceDto invoice, String fileName, Long attachmentId, String employee, UUID employeeId) {
//
//        AttachmentStatusHistoryDto attachmentStatusHistoryDto = new AttachmentStatusHistoryDto();
//        attachmentStatusHistoryDto.setId(UUID.randomUUID());
//        attachmentStatusHistoryDto.setDescription("An attachment to the invoice was updated. The file name: " + fileName);
//        attachmentStatusHistoryDto.setEmployee(employee);
//        attachmentStatusHistoryDto.setInvoice(invoice);
//        attachmentStatusHistoryDto.setEmployeeId(employeeId);
//        attachmentStatusHistoryDto.setAttachmentId(attachmentId);
//
//        this.attachmentStatusHistoryService.create(attachmentStatusHistoryDto);
//    }
}
