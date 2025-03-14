package com.kynsoft.finamer.invoicing.application.command.manageAttachment.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.exception.BusinessException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.rules.manageAttachment.ManageAttachmentFileNameNotNullRule;
import com.kynsoft.finamer.invoicing.domain.services.*;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CreateAttachmentCommandHandler implements ICommandHandler<CreateAttachmentCommand> {

    private final IManageAttachmentService attachmentService;
    private final IManageAttachmentTypeService attachmentTypeService;
    private final IManageResourceTypeService resourceTypeService;
    private final IManageInvoiceService manageInvoiceService;
    private final IAttachmentStatusHistoryService attachmentStatusHistoryService;

    private final IManageInvoiceStatusService invoiceStatusService;
    private final IInvoiceStatusHistoryService invoiceStatusHistoryService;
    private final IManageEmployeeService employeeService;

    public CreateAttachmentCommandHandler(IManageAttachmentService attachmentService,
                                            IManageAttachmentTypeService attachmentTypeService, 
                                            IManageInvoiceService manageInvoiceService,
                                            IManageResourceTypeService resourceTypeService,
                                            IAttachmentStatusHistoryService attachmentStatusHistoryService, 
                                            IManageInvoiceStatusService invoiceStatusService,
                                            IInvoiceStatusHistoryService invoiceStatusHistoryService,
                                            IManageEmployeeService employeeService) {
        this.attachmentService = attachmentService;
        this.attachmentTypeService = attachmentTypeService;
        this.manageInvoiceService = manageInvoiceService;
        this.resourceTypeService = resourceTypeService;
        this.attachmentStatusHistoryService = attachmentStatusHistoryService;
        this.invoiceStatusService = invoiceStatusService;
        this.invoiceStatusHistoryService = invoiceStatusHistoryService;
        this.employeeService = employeeService;
    }

    @Override
    public void handle(CreateAttachmentCommand command) {
        RulesChecker.checkRule(new ManageAttachmentFileNameNotNullRule(command.getFile()));

        ManageAttachmentTypeDto attachmentType = command.getType() != null
                ? this.attachmentTypeService.findById(command.getType())
                : null;
        ManageInvoiceDto invoiceDto = this.manageInvoiceService.findById(command.getInvoice());

        ResourceTypeDto resourceTypeDto = command.getPaymentResourceType() != null
                ? this.resourceTypeService.findById(command.getPaymentResourceType())
                : null;

        ManageEmployeeDto employee = null;
        String employeeFullName = "";
        try {
            employee = this.employeeService.findById(command.getEmployeeId());
            employeeFullName = employee.getFirstName() + " " + employee.getLastName();
        } catch (Exception e) {
            employeeFullName = command.getEmployee();
        }

        if (invoiceDto.getInvoiceType().compareTo(EInvoiceType.INCOME) == 0 && attachmentType.getDefaults() != null
                && attachmentType.getDefaults()) {
            List<ManageAttachmentDto> attachmentDtoList = invoiceDto.getAttachments();
            for (ManageAttachmentDto attachmentDto : attachmentDtoList) {
                if (attachmentDto.getType().getDefaults() != null && attachmentDto.getType().getDefaults()) {
                    throw new BusinessException(DomainErrorMessage.INVOICE_ATTACHMENT_TYPE_CHECK_DEFAULT,
                            DomainErrorMessage.INVOICE_ATTACHMENT_TYPE_CHECK_DEFAULT.getReasonPhrase());
                }
            }
        }

        List<ManageAttachmentDto> attachmentDtoList = invoiceDto.getAttachments();
        boolean defaultAttachment = false;
        if (attachmentDtoList != null && !attachmentDtoList.isEmpty()) {
            for (ManageAttachmentDto attachmentDto : attachmentDtoList) {
                if (attachmentDto.getType().isAttachInvDefault()) {
                    defaultAttachment = true;
                    break;
                }
            }
        } else if (attachmentType != null && attachmentType.isAttachInvDefault()) {
            defaultAttachment = true;
        }
        if (defaultAttachment) {
            boolean reconciled = false;
            if (invoiceDto.getStatus().equals(EInvoiceStatus.PROCESSED)) {
                ManageInvoiceStatusDto reconcileStatus = invoiceStatusService.findByEInvoiceStatus(EInvoiceStatus.RECONCILED);
                invoiceDto = this.manageInvoiceService.changeInvoiceStatus(invoiceDto, reconcileStatus);
                reconciled = true;
            }

            Long attachmentId = attachmentService.create(new ManageAttachmentDto(
                    command.getId(),
                    null,
                    command.getFilename(),
                    command.getFile(),
                    command.getRemark(),
                    attachmentType,
                    invoiceDto, 
                    command.getEmployee(), 
                    command.getEmployeeId(), 
                    null, 
                    resourceTypeDto,
                    false
            ));
            this.updateAttachmentStatusHistory(invoiceDto, command.getFilename(), attachmentId, employeeFullName, command.getEmployeeId());
            if (reconciled){
                this.manageInvoiceService.update(invoiceDto);
                this.updateInvoiceStatusHistory(invoiceDto, employeeFullName, command.getFilename());
            }
        } else {
            throw new BusinessException(
                    DomainErrorMessage.INVOICE_MUST_HAVE_ATTACHMENT_TYPE,
                    DomainErrorMessage.INVOICE_MUST_HAVE_ATTACHMENT_TYPE.getReasonPhrase()
            );
        }
    }

    private void updateAttachmentStatusHistory(ManageInvoiceDto invoice, String fileName, Long attachmentId,
            String employeeFullName, UUID employeeId) {

        AttachmentStatusHistoryDto attachmentStatusHistoryDto = new AttachmentStatusHistoryDto();
        attachmentStatusHistoryDto.setId(UUID.randomUUID());
        attachmentStatusHistoryDto
                .setDescription("An attachment to the invoice was inserted. The file name: " + fileName);
        attachmentStatusHistoryDto.setEmployee(employeeFullName);
        attachmentStatusHistoryDto.setInvoice(invoice);
        attachmentStatusHistoryDto.setEmployeeId(employeeId);
        attachmentStatusHistoryDto.setAttachmentId(attachmentId);

        this.attachmentStatusHistoryService.create(attachmentStatusHistoryDto);
    }
 
    private void updateInvoiceStatusHistory(ManageInvoiceDto invoiceDto, String employeeFullName, String fileName) {

        InvoiceStatusHistoryDto dto = new InvoiceStatusHistoryDto();
        dto.setId(UUID.randomUUID());
        dto.setInvoice(invoiceDto);
        dto.setDescription("An attachment to the invoice was inserted. The file name: " + fileName);
        dto.setEmployee(employeeFullName);
        dto.setInvoiceStatus(invoiceDto.getStatus());

        this.invoiceStatusHistoryService.create(dto);
    }
}
