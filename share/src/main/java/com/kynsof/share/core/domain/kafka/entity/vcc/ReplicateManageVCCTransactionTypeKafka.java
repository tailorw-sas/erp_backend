package com.kynsof.share.core.domain.kafka.entity.vcc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateManageVCCTransactionTypeKafka {

    private UUID id;
    private String code;
    private String name;
    private Boolean isDefault;
    private Boolean subcategory;
    private boolean manual;
    private String status;
}
