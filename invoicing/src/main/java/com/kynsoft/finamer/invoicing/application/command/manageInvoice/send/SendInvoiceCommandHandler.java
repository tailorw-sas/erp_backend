package com.kynsoft.finamer.invoicing.application.command.manageInvoice.send;

import com.kynsof.share.core.application.mailjet.*;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsof.share.utils.ServiceLocator;
import com.kynsoft.finamer.invoicing.application.query.report.InvoiceReportQuery;
import com.kynsoft.finamer.invoicing.application.query.report.InvoiceReportRequest;
import com.kynsoft.finamer.invoicing.application.query.report.InvoiceReportResponse;
import com.kynsoft.finamer.invoicing.domain.dto.*;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceReportType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.services.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Component
@Transactional
public class SendInvoiceCommandHandler implements ICommandHandler<SendInvoiceCommand> {

    private final IManageInvoiceService service;
    private final MailService mailService;
    private final IMediator mediator;


    public SendInvoiceCommandHandler(IManageInvoiceService service, MailService mailService, ServiceLocator<IMediator> serviceLocator) {
        this.service = service;
        this.mailService = mailService;
        mediator=serviceLocator.getBean(IMediator.class);
    }

    @Override
    @Transactional
    public void handle(SendInvoiceCommand command) {
        // Obtener la lista de facturas por sus IDs
        List<ManageInvoiceDto> invoices = this.service.findByIds(command.getInvoice());
        // Agrupar facturas por agencia
        Map<ManageAgencyDto, List<ManageInvoiceDto>> invoicesByAgency = new HashMap<>();
        for (ManageInvoiceDto invoice : invoices) {
            if (!invoice.getStatus().equals(EInvoiceStatus.SENT) && !invoice.getStatus().equals(EInvoiceStatus.RECONCILED)) {
                throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.SERVICE_NOT_FOUND,
                        new ErrorField("id", DomainErrorMessage.SERVICE_NOT_FOUND.getReasonPhrase())));
            }
            invoicesByAgency.computeIfAbsent(invoice.getAgency(), k -> new ArrayList<>()).add(invoice);
        }

        // Enviar correos agrupados por agencia
        for (Map.Entry<ManageAgencyDto, List<ManageInvoiceDto>> entry : invoicesByAgency.entrySet()) {
            ManageAgencyDto agency = entry.getKey();
            List<ManageInvoiceDto> agencyInvoices = entry.getValue();

            // Enviar correo a la agencia con todas sus facturas adjuntas
            sendEmail(command, agency, agencyInvoices);

            // Actualizar el estado de cada factura a SENT
            for (ManageInvoiceDto invoice : agencyInvoices) {
                invoice.setStatus(EInvoiceStatus.SENT);
                this.service.update(invoice);
            }
        }
    }

    private void sendEmail(SendInvoiceCommand command, ManageAgencyDto agency, List<ManageInvoiceDto> invoices) {
        if (agency.getMailingAddress() != null) {
            SendMailJetEMailRequest request = new SendMailJetEMailRequest();
            request.setSubject("INVOICES for " + agency.getName());
            request.setTemplateId(6285030); // Cambiar en configuración

            // Variables para el template de email
//            List<MailJetVar> vars = Arrays.asList(
//                    new MailJetVar("username", "Niurka"),
//                    new MailJetVar("otp_token", "5826384")
//            );
            List<MailJetVar> vars = new ArrayList<>();
            request.setMailJetVars(vars);

            // Recipients
            List<MailJetRecipient> recipients = new ArrayList<>();
            recipients.add(new MailJetRecipient(agency.getMailingAddress(), agency.getName()));
            recipients.add(new MailJetRecipient("enrique.muguercia2016@gmail.com", "Enrique Basto"));
            //TODO send email employee
            request.setRecipientEmail(recipients);
            // Adjuntar todas las facturas de la agencia
            List<MailJetAttachment> attachments = new ArrayList<>();
            for (ManageInvoiceDto invoice : invoices) {
                Optional<ByteArrayOutputStream> invoiceBooking = getInvoicesBooking(invoice.getId().toString());
                if (invoiceBooking.isPresent()) {
                    String nameFile = invoice.getInvoiceNumber() + ".pdf";
                    Optional<byte[]> pdfContent = convertBookingToBase64(invoiceBooking.get());
                    if (pdfContent.isPresent()) {
                        MailJetAttachment attachment = new MailJetAttachment("application/pdf", nameFile, Arrays.toString(pdfContent.get())); // PDF content base64
                        attachments.add(attachment);
                    }
                }
            }
            if (!attachments.isEmpty()) {
                request.setMailJetAttachments(attachments);
                try {
                    mailService.sendMail(request);
                    command.setResult(true);
                } catch (Exception e) {
                    command.setResult(false);
                }
            }
        }
    }


    private Optional<ByteArrayOutputStream> getInvoicesBooking(String invoiceIds) {
        InvoiceReportRequest invoiceReportRequest = new InvoiceReportRequest(new String[]{invoiceIds}, new String[]{EInvoiceReportType.INVOICE_AND_BOOKING.name()});
        InvoiceReportQuery query = new InvoiceReportQuery(invoiceReportRequest);
        InvoiceReportResponse response = mediator.send(query);
        return Optional.of(response.getFile());
    }

    private Optional<byte[]> convertBookingToBase64(ByteArrayOutputStream pdfContent) {
        return Optional.of(Base64.getEncoder().encode(pdfContent.toByteArray()));
    }

}
