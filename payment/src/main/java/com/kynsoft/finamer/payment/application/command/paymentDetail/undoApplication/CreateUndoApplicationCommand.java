package com.kynsoft.finamer.payment.application.command.paymentDetail.undoApplication;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateUndoApplicationCommand implements ICommand {

    private UUID paymentDetail;
    private final IMediator mediator;
    private UUID employee;

    public CreateUndoApplicationCommand(UUID paymentDetail, IMediator mediator, UUID employee) {
        this.paymentDetail = paymentDetail;
        this.mediator = mediator;
        this.employee = employee;
    }

    public static CreateUndoApplicationCommand fromRequest(CreateUndoApplicationRequest request, IMediator mediator) {
        return new CreateUndoApplicationCommand(
                request.getPaymentDetail(),
                mediator,
                request.getEmployee()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateUndoApplicationMessage(paymentDetail);
    }
}
