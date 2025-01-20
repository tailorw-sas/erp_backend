package com.kynsoft.finamer.creditcard.application.command.manageHotel.create;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.creditcard.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageHotelService;
import org.springframework.stereotype.Component;

@Component
public class CreateManageHotelCommandHandler implements ICommandHandler<CreateManageHotelCommand> {

    private final IManageHotelService service;

    public CreateManageHotelCommandHandler(IManageHotelService service) {
        this.service = service;
    }

    @Override
    public void handle(CreateManageHotelCommand command) {
        service.create(new ManageHotelDto(
                command.getId(), command.getCode(), command.getName(), command.getIsApplyByVCC(), command.getStatus(), command.getAddress()
        ));
    }
}
