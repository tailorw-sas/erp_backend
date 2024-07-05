package com.kynsoft.finamer.invoicing.application.command.manageInvoice.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceRequest {
    private UUID id;
    private LocalDateTime invoiceDate;
    private Boolean isManual;
    private Double invoiceAmount;
    private UUID hotel;
    private UUID agency;
    private UUID invoiceType;

}
