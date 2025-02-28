package com.kynsoft.finamer.creditcard.application.command.manageReconcileTransactionStatus.delete;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.creditcard.domain.dto.ManageReconcileTransactionStatusDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageReconcileTransactionStatusService;
import org.springframework.stereotype.Component;

@Component
public class DeleteManageReconcileTransactionStatusCommandHandler implements ICommandHandler<DeleteManageReconcileTransactionStatusCommand> {

    private final IManageReconcileTransactionStatusService service;

    public DeleteManageReconcileTransactionStatusCommandHandler(IManageReconcileTransactionStatusService service) {
        this.service = service;
    }

    @Override
    public void handle(DeleteManageReconcileTransactionStatusCommand command) {
        ManageReconcileTransactionStatusDto dto = service.findById(command.getId());

        service.delete(dto);
    }
}
