package com.kynsoft.finamer.payment.application.command.replicate.objects;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateReplicateCommand implements ICommand {

    private List<ObjectEnum> objects;

    public CreateReplicateCommand(List<ObjectEnum> objects) {
        this.objects = objects;
    }

    public static CreateReplicateCommand fromRequest(CreateReplicateRequest request) {
        return new CreateReplicateCommand(
                request.getObjects()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreateReplicateMessage();
    }
}
