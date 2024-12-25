package com.kynsof.share.core.domain.kafka.entity.hotelPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateHotelPaymentFailedIncome implements Serializable {

    private UUID hotelPaymentId;
    private List<ReplicateHotelPaymentErrors> errors;
}
