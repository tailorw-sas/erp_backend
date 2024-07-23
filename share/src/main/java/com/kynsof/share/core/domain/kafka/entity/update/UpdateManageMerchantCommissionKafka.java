package com.kynsof.share.core.domain.kafka.entity.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManageMerchantCommissionKafka {

    private UUID id;
    private UUID managerMerchant;
    private UUID manageCreditCartType;
    private Double commission;
    private String calculationType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
}
