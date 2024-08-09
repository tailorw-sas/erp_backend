package com.kynsoft.finamer.invoicing.application.command.manageHotel.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateManageHotelCommand implements ICommand {

    private UUID id;
    private String name;
    private UUID tradingCompany;
    private String status;
    private Boolean isVirtual;
    private boolean requiresFlatRate;

    public UpdateManageHotelCommand(UUID id, String name, UUID tradingCompany, String status, Boolean isVirtual,boolean requiresFlatRate) {
        this.id = id;
        this.name = name;
        this.tradingCompany = tradingCompany;
        this.status = status;
        this.isVirtual = isVirtual;
        this.requiresFlatRate=requiresFlatRate;
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdateManageHotelMessage(id);
    }
}
