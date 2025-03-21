package com.kynsoft.finamer.insis.application.command.manageRoomType.create;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class CreateRoomTypeCommand implements ICommand {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private boolean deleted;
    private UUID hotelId;

    public CreateRoomTypeCommand(UUID id, String code, String name, UUID hotelId){
        this.id = Objects.nonNull(id) ? id : UUID.randomUUID();
        this.code = code;
        this.name = name;
        this.status = "ACTIVE";
        this.hotelId = hotelId;
    }
    @Override
    public ICommandMessage getMessage() {
        return new CreateRoomTypeMessage(id);
    }
}
