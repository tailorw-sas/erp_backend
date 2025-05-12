package com.kynsoft.finamer.payment.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageInvoiceResponse implements IResponse {

    private UUID id;
    private Long invoiceId;
    private Long invoiceNo;
    private String invoiceNumber;
    private Double invoiceAmount;
    private Double invoiceBalance;
    private LocalDateTime invoiceDate;
    private Boolean hasAttachment;
    private EInvoiceType invoiceType;
    private ManageHotelResponse hotel;
    private ManageAgencyResponse agency;
    private Boolean autoRec;
    private ManageInvoiceResponse parent;
    private ManageInvoiceStatusResponse status;

    public ManageInvoiceResponse(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceId = dto.getInvoiceId();
        this.invoiceNo = dto.getInvoiceNo();
        this.invoiceNumber = deleteHotelInfo(dto.getInvoiceNumber());
        this.invoiceAmount = dto.getInvoiceAmount();
        this.invoiceBalance = dto.getInvoiceBalance();
        this.invoiceDate = dto.getInvoiceDate();
        this.hasAttachment = dto.getHasAttachment();
        this.invoiceType = dto.getInvoiceType();
        this.hotel = Objects.nonNull(dto.getHotel()) ? new ManageHotelResponse(dto.getHotel()) : null;
        this.agency = Objects.nonNull(dto.getAgency()) ? new ManageAgencyResponse(dto.getAgency()) : null;
        this.autoRec = dto.getAutoRec();
        this.parent = Objects.nonNull(dto.getParent()) ? new ManageInvoiceResponse(dto.getParent()) : null;
        this.status = Objects.nonNull(dto.getStatus()) ? new ManageInvoiceStatusResponse(dto.getStatus()) : null;
    }

    private String deleteHotelInfo(String input) {
        if(input != null){
            return input.replaceAll("-(.*?)-", "-");
        }

        return input;
    }

}
