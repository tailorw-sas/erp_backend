package com.kynsoft.finamer.creditcard.application.command.manageMerchant.delete;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DeleteManageMerchantMessage implements ICommandMessage {

    private final UUID id;

    private final String command = "DELETE_MANAGE_MERCHANT";

    public DeleteManageMerchantMessage(UUID id) {
        this.id = id;
    }

}
