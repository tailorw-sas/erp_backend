package com.kynsoft.finamer.creditcard.application.command.managePaymentTransactionStatus.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateManagePaymentTransactionStatusCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;
    private Status status;
    private String description;
    private boolean requireValidation;
    private List<UUID> navigate;

    private boolean inProgress;
    private boolean completed;
    private boolean cancelled;
    private boolean applied;

    public CreateManagePaymentTransactionStatusCommand(
            String code, String name, Status status, String description,
            boolean inProgress, boolean completed, boolean cancelled,
            boolean applied, boolean requireValidation, List<UUID> navigate) {

        this.id = UUID.randomUUID();
        this.code = code;
        this.name = name;
        this.status = status;
        this.description = description;
        this.inProgress = inProgress;
        this.completed = completed;
        this.cancelled = cancelled;
        this.applied = applied;
        this.requireValidation = requireValidation;
        this.navigate = navigate;
    }

    public static CreateManagePaymentTransactionStatusCommand fromRequest(CreateManagePaymentTransactionStatusRequest request) {
        return new CreateManagePaymentTransactionStatusCommand(
                request.getCode(), request.getName(), request.getStatus(), request.getDescription(),
                request.isInProgress(), request.isCompleted(), request.isCancelled(), request.isApplied(),
                request.isRequireValidation(), request.getNavigate()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateManagePaymentTransactionStatusMessage(id);
    }
}
