package com.kynsof.share.core.domain.kafka.entity.hotelPayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateHotelPaymentKafka implements Serializable {

    private UUID id;
    private Long hotelPaymentId;
    private LocalDateTime transactionDate;
    private UUID manageHotel;
    private UUID manageBankAccount;
    private double amount;
    private double commission;
    private double netAmount;
    private UUID status;
    private String remark;
}
