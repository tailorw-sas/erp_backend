package com.kynsoft.finamer.invoicing.application.command.manageInvoice.newCredit;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreateNewCreditCommand implements ICommand {

    private LocalDateTime invoiceDate;
    private UUID invoice;
    private String employee;
    private UUID credit;
    private List<CreateNewCreditBookingRequest> bookings;
    private List<CreateNewCreditAttachmentRequest> attachmentCommands;
    private Long invoiceId;
    private String invoiceNumber;
    private String employeeName;
    

    public CreateNewCreditCommand(LocalDateTime invoiceDate, UUID invoice, String employee,
                                  List<CreateNewCreditBookingRequest> bookings,
                                  List<CreateNewCreditAttachmentRequest> attachmentCommands,
                                  String employeeName) {
        this.invoiceDate = invoiceDate;
        this.invoice = invoice;
        this.employee = employee;
        this.bookings = bookings;
        this.attachmentCommands = attachmentCommands;
        this.employeeName = employeeName;
    }

    public static CreateNewCreditCommand fromRequest(CreateNewCreditRequest request) {

        return new CreateNewCreditCommand(
                request.getInvoiceDate(), request.getInvoice(), request.getEmployee(),
                request.getBookings(), request.getAttachments(), request.getEmployeeName()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateNewCreditMessage(
                invoice, credit, invoiceId, invoiceNumber
        );
    }
}
