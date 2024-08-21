package com.kynsoft.finamer.payment.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.payment.domain.dto.*;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
    private List<ManageBookingResponse> bookings = new ArrayList<>();

    public ManageInvoiceResponse(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceId = dto.getInvoiceId();
        this.invoiceNo = dto.getInvoiceNo();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceAmount = dto.getInvoiceAmount();
        if (dto.getBookings() != null) {
            for (ManageBookingDto booking : dto.getBookings()) {
                this.bookings.add(new ManageBookingResponse(booking));
            }
        }
    }

}
