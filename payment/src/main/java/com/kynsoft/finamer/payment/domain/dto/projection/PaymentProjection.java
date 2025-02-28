package com.kynsoft.finamer.payment.domain.dto.projection;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentProjection implements Serializable {

    private UUID id;
    private long paymentId;

    private double paymentAmount;
    private double paymentBalance;
    private double depositAmount;
    private double depositBalance;
    private double otherDeductions;
    private double identified;
    private double notIdentified;
    private Double notApplied;
    private Double applied;

    private UUID agencyId;
    private UUID hotelId;
}
