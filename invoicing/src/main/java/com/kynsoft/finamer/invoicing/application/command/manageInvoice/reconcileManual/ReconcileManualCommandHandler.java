package com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileManual;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsoft.finamer.invoicing.application.command.invoiceReconcileManualPdf.InvoiceReconcileManualPdfRequest;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceReportType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.services.*;
import com.kynsoft.finamer.invoicing.infrastructure.services.report.factory.InvoiceReportProviderFactory;
import com.kynsoft.finamer.invoicing.infrastructure.utils.InvoiceUploadAttachmentUtil;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReconcileManualCommandHandler implements ICommandHandler<ReconcileManualCommand> {

    private final IManageInvoiceService invoiceService;
    private final IManageAttachmentTypeService attachmentTypeService;
    private final IManageResourceTypeService resourceTypeService;
    private final IManageInvoiceStatusService invoiceStatusService;
    private final IAttachmentStatusHistoryService attachmentStatusHistoryService;
    private final IInvoiceStatusHistoryService invoiceStatusHistoryService;

    private final InvoiceReportProviderFactory invoiceReportProviderFactory;

    private final InvoiceUploadAttachmentUtil invoiceUploadAttachmentUtil;
    private final IReportPdfService iReportPdfService;

    public ReconcileManualCommandHandler(IManageInvoiceService invoiceService, IManageAttachmentTypeService attachmentTypeService,
            IManageResourceTypeService resourceTypeService,
            IManageInvoiceStatusService invoiceStatusService,
            IAttachmentStatusHistoryService attachmentStatusHistoryService,
            IInvoiceStatusHistoryService invoiceStatusHistoryService,
            InvoiceReportProviderFactory invoiceReportProviderFactory,
            InvoiceUploadAttachmentUtil invoiceUploadAttachmentUtil,
            IReportPdfService iReportPdfService) {
        this.iReportPdfService = iReportPdfService;
        this.invoiceService = invoiceService;
        this.attachmentTypeService = attachmentTypeService;
        this.resourceTypeService = resourceTypeService;
        this.invoiceStatusService = invoiceStatusService;
        this.attachmentStatusHistoryService = attachmentStatusHistoryService;
        this.invoiceStatusHistoryService = invoiceStatusHistoryService;
        this.invoiceReportProviderFactory = invoiceReportProviderFactory;
        this.invoiceUploadAttachmentUtil = invoiceUploadAttachmentUtil;
    }

    @Override
    public void handle(ReconcileManualCommand command) {
        ManageInvoiceStatusDto reconcileStatus = this.invoiceStatusService.findByEInvoiceStatus(EInvoiceStatus.RECONCILED);
        List<ReconcileManualErrorResponse> errorResponse = new ArrayList<>();
        for (UUID id : command.getInvoices()) {
            ManageInvoiceDto invoiceDto = this.invoiceService.findById(id);
            if (!invoiceDto.getAgency().getAutoReconcile()) {
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The agency does not have auto-reconciliation enabled."
                ));
                continue;
            }
            if (invoiceDto.getStatus().compareTo(EInvoiceStatus.PROCESSED) != 0) {
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The invoice is not in processed status."
                ));
                continue;
            }
            if (invoiceDto.getInvoiceType().compareTo(EInvoiceType.INVOICE) != 0) {
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The invoice type must be INV."
                ));
                continue;
            }
            Optional<byte[]> fileContent = Optional.empty();
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] fileContentResponse = this.iReportPdfService.concatenateManualPDFs(new InvoiceReconcileManualPdfRequest(List.of(id), outputStream.toByteArray()));
                fileContent = Optional.of(fileContentResponse);
                //fileContent = this.createInvoiceReconcileAutomaticSupportAttachmentContent(invoiceDto.getId().toString());
            } catch (Exception e) {
                e.printStackTrace();
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The pdf could not be generated."
                ));
                continue;
            }

            String filename = "invoice_" + invoiceDto.getInvoiceId() + ".pdf";
            String file = "";
            try {
                LinkedHashMap<String, String> response = invoiceUploadAttachmentUtil.uploadAttachmentContent(filename, fileContent.get());
                file = response.get("url");
            } catch (Exception e) {
                System.out.println("Error al subir el adjunto: " + e);
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The attachment could not be uploaded."
                ));
                continue;
            }

            //creando y adjuntando el attachment
//            RulesChecker.checkRule(new ManageAttachmentFileNameNotNullRule(file));
            ManageAttachmentTypeDto attachmentTypeDto = command.getAttachInvDefault() != null
                    ? this.attachmentTypeService.findById(command.getAttachInvDefault())
                    : null;
            ResourceTypeDto resourceTypeDto = command.getResourceType() != null
                    ? this.resourceTypeService.findById(command.getResourceType())
                    : null;
            List<ManageAttachmentDto> attachments = invoiceDto.getAttachments() != null ? invoiceDto.getAttachments() : new ArrayList<>();

            ManageAttachmentDto attachmentDto = new ManageAttachmentDto(
                    UUID.randomUUID(),
                    null,
                    filename,
                    file,
                    "",
                    attachmentTypeDto,
                    null,
                    command.getEmployeeName(),
                    command.getEmployeeId(),
                    null,
                    resourceTypeDto,
                    false
            );
            attachments.add(attachmentDto);
            try {
                invoiceDto = this.invoiceService.changeInvoiceStatus(invoiceDto, reconcileStatus);
            } catch (BusinessNotFoundException e){
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        e.getBrokenRule().getErrorField().getMessage()
                ));
                continue;
            } catch (Exception e) {
                errorResponse.add(new ReconcileManualErrorResponse(
                        invoiceDto,
                        "The attachment could not be added."
                ));
                continue;
            }
            this.invoiceService.update(invoiceDto);
            this.attachmentStatusHistoryService.create(attachmentDto, invoiceDto);
            this.invoiceStatusHistoryService.create(invoiceDto, command.getEmployeeId().toString());
        }
        command.setErrorResponse(errorResponse);
        command.setTotalInvoicesRec(command.getInvoices().size() - errorResponse.size());
        command.setTotalInvoices(command.getInvoices().size());
    }

    private Optional<byte[]> createInvoiceReconcileAutomaticSupportAttachmentContent(String invoiceId) {
        IInvoiceReport service = invoiceReportProviderFactory.getInvoiceReportService(EInvoiceReportType.RECONCILE_AUTO);
        return service.generateReport(invoiceId);
    }

}
