package com.kynsoft.finamer.invoicing.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDetailDto {

    private UUID id;
    private Long paymentDetailId;
    private PaymentDto payment;
    private ManageBookingDto manageBooking;
}
