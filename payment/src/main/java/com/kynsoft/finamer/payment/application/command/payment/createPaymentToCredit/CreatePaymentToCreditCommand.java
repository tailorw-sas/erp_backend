package com.kynsoft.finamer.payment.application.command.payment.createPaymentToCredit;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.payment.application.command.payment.create.CreateAttachmentRequest;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentToCreditCommand implements ICommand {

    private UUID id;
    private ManageInvoiceDto invoiceDto;
    private UUID client;
    private UUID agency;
    private UUID hotel;
    private UUID employee;
    private List<CreateAttachmentRequest> attachments;
    private boolean autoApplyCredit;
    private final IMediator mediator;

    public CreatePaymentToCreditCommand(UUID client, UUID agency, UUID hotel, ManageInvoiceDto invoiceDto, List<CreateAttachmentRequest> attachments, boolean autoApplyCredit, final IMediator mediator, UUID employee) {
        this.id = UUID.randomUUID();
        this.client = client;
        this.agency = agency;
        this.hotel = hotel;
        this.invoiceDto = invoiceDto;
        this.attachments = attachments;
        this.autoApplyCredit = autoApplyCredit;
        this.mediator = mediator;
        this.employee = employee;
    }

    @Override
    public ICommandMessage getMessage() {
        return new CreatePaymentToCreditMessage();
    }
}
