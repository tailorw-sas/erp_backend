package com.kynsoft.finamer.invoicing.application.command.manageInvoice.newCredit;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateNewCreditMessage implements ICommandMessage {

    private final String command = "CREATE_NEW_CREDIT";
    private UUID id;
    private UUID credit;
    private Long invoiceId;
    private String invoiceNumber;

    public CreateNewCreditMessage(UUID id, UUID credit, Long invoiceId, String invoiceNumber) {
        this.id = id;
        this.credit = credit;
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
    }
}
