package com.kynsoft.finamer.payment.application.command.payment.update;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdatePaymentCommand implements ICommand {
    private UUID id;
    
    private UUID paymentSource;
    private String reference;
    private LocalDate transactionDate;
    private UUID paymentStatus;
    private UUID client;
    private UUID agency;
    private UUID hotel;
    private UUID bankAccount;
    private UUID attachmentStatus;

    private String remark;
    private UUID employee;

    public UpdatePaymentCommand(UUID id, UUID paymentSource, String reference, LocalDate transactionDate, 
                                UUID paymentStatus, UUID client, UUID agency, UUID hotel, UUID bankAccount, 
                                UUID attachmentStatus, String remark, UUID employee) {
        this.id = id;
        this.paymentSource = paymentSource;
        this.reference = reference;
        this.transactionDate = transactionDate;
        this.paymentStatus = paymentStatus;
        this.client = client;
        this.agency = agency;
        this.hotel = hotel;
        this.bankAccount = bankAccount;
        this.attachmentStatus = attachmentStatus;
        this.remark = remark;
        this.employee = employee;
    }

    public static UpdatePaymentCommand fromRequest(UpdatePaymentRequest request, UUID id) {
        return new UpdatePaymentCommand(
                id,
                request.getPaymentSource(),
                request.getReference(),
                request.getTransactionDate(),
                request.getPaymentStatus(),
                request.getClient(),
                request.getAgency(),
                request.getHotel(),
                request.getBankAccount(),
                request.getAttachmentStatus(),
                request.getRemark(),
                request.getEmployee()
        );
    }

    @Override
    public ICommandMessage getMessage() {
        return new UpdatePaymentMessage(id);
    }
}
