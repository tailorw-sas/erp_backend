package com.kynsoft.finamer.payment.application.command.payment.changeAttachmentStatus;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EAttachment;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChangeAttachmentStatusCommand implements ICommand {

    private EAttachment attachmentStatus;
    private UUID payment;

    private PaymentDto paymentResponse;

    public ChangeAttachmentStatusCommand(EAttachment attachmentStatus, UUID payment) {
        this.attachmentStatus = attachmentStatus;
        this.payment = payment;
    }

    public static ChangeAttachmentStatusCommand fromRequest(ChangeAttachmentStatusRequest request) {
        return new ChangeAttachmentStatusCommand(
                request.getStatus(),
                request.getPayment()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new ChangeAttachmentStatusMessage(paymentResponse);
    }
}
