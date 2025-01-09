package com.kynsoft.finamer.invoicing.application.command.manageInvoice.undoImportInvoice;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UndoImportInvoiceCommand implements ICommand {

    private List<UUID> ids;
    private IMediator mediator;
    private List<UndoImportErrors> errors;
    private int satisfactoryQuantity;
    private String employee;

    public UndoImportInvoiceCommand(List<UUID> ids, IMediator mediator, String employee) {
        this.ids = ids;
        this.mediator = mediator;
        this.employee = employee;
    }

    public static UndoImportInvoiceCommand fromRequest(UndoImportInvoiceRequest request, IMediator mediator) {
        return new UndoImportInvoiceCommand(request.getIds(), mediator, request.getEmployee());
    }

    @Override
    public ICommandMessage getMessage() {
        return new UndoImportInvoiceMessage(errors, satisfactoryQuantity);
    }

}
