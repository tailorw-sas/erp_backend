package com.kynsoft.finamer.settings.application.command.manageCurrency.update;

import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateManagerCurrencyRequest {
    private String name;
    private String description;
    private Status status;
}
