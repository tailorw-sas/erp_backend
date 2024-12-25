package com.kynsof.share.core.domain.kafka.entity.hotelPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReplicateHotelPaymentErrors implements Serializable {
    private String field;
    private String msg;
}
