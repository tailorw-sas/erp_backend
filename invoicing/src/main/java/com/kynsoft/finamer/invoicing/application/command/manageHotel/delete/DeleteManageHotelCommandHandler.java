package com.kynsoft.finamer.invoicing.application.command.manageHotel.delete;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.invoicing.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.invoicing.domain.services.IManageHotelService;
import org.springframework.stereotype.Component;

@Component
public class DeleteManageHotelCommandHandler implements ICommandHandler<DeleteManageHotelCommand> {

    private final IManageHotelService service;

    public DeleteManageHotelCommandHandler(IManageHotelService service) {
        this.service = service;
    }

    @Override
    public void handle(DeleteManageHotelCommand command) {
        ManageHotelDto dto = service.findById(command.getId());

        service.delete(dto);
    }
}
