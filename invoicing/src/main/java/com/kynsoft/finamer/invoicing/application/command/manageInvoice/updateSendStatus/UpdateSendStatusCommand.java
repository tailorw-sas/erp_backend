package com.kynsoft.finamer.invoicing.application.command.manageInvoice.updateSendStatus;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.domain.response.UploadFileResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class UpdateSendStatusCommand implements ICommand {
    Map<UUID, UploadFileResponse> invoiceResponses;

    private UpdateSendStatusCommand(Map<UUID, UploadFileResponse> invoiceResponses){
        this.invoiceResponses = invoiceResponses;
    }

    public static UpdateSendStatusCommand fromRequest(UpdateSendStatusRequest request) {
        return new UpdateSendStatusCommand(request.getInvoiceResponses());
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdateSendStatusMessage();//TODO APF find out what to do with this object
    }
}
