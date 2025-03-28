package com.kynsoft.finamer.invoicing.application.command.manageInvoice.updateSendStatus;

import com.kynsof.share.core.domain.response.UploadFileResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class UpdateSendStatusRequest {
    Map<UUID, UploadFileResponse> invoiceResponses;
}
