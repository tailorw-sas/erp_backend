package com.kynsoft.finamer.payment.application.command.templateEntity.delete;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsoft.finamer.payment.domain.dto.TemplateDto;
import com.kynsoft.finamer.payment.domain.services.ITemplateEntityService;
import org.springframework.stereotype.Component;

@Component
public class DeleteTemplateEntityCommandHandler implements ICommandHandler<DeleteTemplateEntityCommand> {

    private final ITemplateEntityService serviceImpl;

    public DeleteTemplateEntityCommandHandler(ITemplateEntityService serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    @Override
    public void handle(DeleteTemplateEntityCommand command) {
        TemplateDto object = this.serviceImpl.findById(command.getId());

        serviceImpl.delete(object);
    }

}
