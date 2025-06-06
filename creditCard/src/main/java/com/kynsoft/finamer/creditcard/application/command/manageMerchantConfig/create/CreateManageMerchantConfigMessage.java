package com.kynsoft.finamer.creditcard.application.command.manageMerchantConfig.create;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateManageMerchantConfigMessage implements ICommandMessage {

    private UUID id;
    private String command = "CREATE_MANAGER_MERCHANT_CONFIG";

    public CreateManageMerchantConfigMessage(UUID id) {
        this.id = id;
    }
}
