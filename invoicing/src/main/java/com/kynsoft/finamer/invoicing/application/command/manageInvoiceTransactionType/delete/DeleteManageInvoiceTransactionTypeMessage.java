package com.kynsoft.finamer.invoicing.application.command.manageInvoiceTransactionType.delete;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteManageInvoiceTransactionTypeMessage implements ICommandMessage {

    private final UUID id;

    private final String command = "DELETE_MANAGE_INVOICE_TRANSACTION_TYPE";

    public DeleteManageInvoiceTransactionTypeMessage(UUID id) {
        this.id = id;
    }
}
