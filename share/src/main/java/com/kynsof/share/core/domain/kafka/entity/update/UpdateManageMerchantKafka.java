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
public class UpdateManageMerchantKafka {
    private UUID id;
    private String code;
    private String description;
    private UUID b2bPartner;
    private Boolean defaultm;
    private String status;
}
