package com.kynsof.share.core.domain.kafka.entity.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManageMerchantHotelEnrolleKafka {

    private UUID id;
    private UUID managerMerchant;
    private UUID managerHotel;
    private String enrrolle;
    private String status;
}
