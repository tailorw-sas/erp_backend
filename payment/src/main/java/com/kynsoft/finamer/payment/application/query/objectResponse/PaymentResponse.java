package com.kynsoft.finamer.payment.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsof.share.utils.ScaleAmount;
import com.kynsoft.finamer.payment.domain.dto.MasterPaymentAttachmentDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentResponse implements IResponse {

    private UUID id;
    private Long paymentId;
    private ManagePaymentSourceResponse paymentSource;
    private String reference;
    private LocalDate transactionDate;
    private ManagePaymentStatusResponse paymentStatus;
    private ManageClientResponse client;
    private ManageAgencyResponse agency;
    private ManageHotelResponse hotel;
    private ManageBankAccountResponse bankAccount;
    private ManagePaymentAttachmentStatusResponse attachmentStatus;

    private Double paymentAmount;
    private Double paymentBalance;
    private double depositAmount;
    private double depositBalance;
    private double otherDeductions;
    private double identified;
    private double notIdentified;
    private String remark;
    private Boolean hasAttachment;
    private List<MasterPaymentAttachmentResponse> attachments = new ArrayList<>();

    public PaymentResponse(PaymentDto dto) {
        this.id = dto.getId();
        this.paymentId = dto.getPaymentId();
        this.paymentSource = dto.getPaymentSource() != null ? new ManagePaymentSourceResponse(dto.getPaymentSource()) : null;
        this.reference = dto.getReference();
        this.transactionDate = dto.getTransactionDate();
        this.paymentStatus = dto.getPaymentStatus() != null ? new ManagePaymentStatusResponse(dto.getPaymentStatus()) : null;
        this.client = dto.getClient() != null ? new ManageClientResponse(dto.getClient()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgencyResponse(dto.getAgency()) : null;
        this.hotel = dto.getHotel() != null ? new ManageHotelResponse(dto.getHotel()) : null;
        this.bankAccount = dto.getBankAccount() != null ? new ManageBankAccountResponse(dto.getBankAccount()) : null;
        this.attachmentStatus = dto.getAttachmentStatus() != null ? new ManagePaymentAttachmentStatusResponse(dto.getAttachmentStatus()) : null;
        this.paymentAmount = ScaleAmount.scaleAmount(dto.getPaymentAmount());
        this.paymentBalance = ScaleAmount.scaleAmount(dto.getPaymentBalance());
        this.depositAmount = ScaleAmount.scaleAmount(dto.getDepositAmount());
        this.depositBalance = ScaleAmount.scaleAmount(dto.getDepositBalance());
        this.otherDeductions = ScaleAmount.scaleAmount(dto.getOtherDeductions());
        this.identified = ScaleAmount.scaleAmount(dto.getIdentified());
        this.notIdentified = ScaleAmount.scaleAmount(dto.getNotIdentified());
        this.remark = dto.getRemark();
        if (dto.getAttachments() != null) {
            for (MasterPaymentAttachmentDto attachment : dto.getAttachments()) {
                attachments.add(new MasterPaymentAttachmentResponse(attachment));
            }
        }
        this.hasAttachment = !this.attachments.isEmpty();
    }

}
