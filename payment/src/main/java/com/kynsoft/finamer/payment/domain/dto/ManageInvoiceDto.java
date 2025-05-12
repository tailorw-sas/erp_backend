package com.kynsoft.finamer.payment.domain.dto;

import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageInvoiceDto {

    private UUID id;
    private Long invoiceId;
    private Long invoiceNo;
    private String invoiceNumber;
    private EInvoiceType invoiceType;
    private Double invoiceAmount;
    private Double invoiceBalance;
    private List<ManageBookingDto> bookings;
    private Boolean hasAttachment;
    private ManageInvoiceDto parent;
    private LocalDateTime invoiceDate;
    private ManageHotelDto hotel;
    private ManageAgencyDto agency;
    private Boolean autoRec;
    private ManageInvoiceStatusDto status;
}
