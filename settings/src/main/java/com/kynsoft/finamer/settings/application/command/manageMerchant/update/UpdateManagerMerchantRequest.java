package com.kynsoft.finamer.settings.application.command.manageMerchant.update;

import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateManagerMerchantRequest {
    private String code;
    private String description;
    private UUID b2bPartner;
    private Boolean defaultm;
    private Status status;
}
