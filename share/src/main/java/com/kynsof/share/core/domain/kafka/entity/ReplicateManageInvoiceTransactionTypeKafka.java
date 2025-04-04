package com.kynsof.share.core.domain.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateManageInvoiceTransactionTypeKafka  implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private boolean defaults;
    private boolean cloneAdjustmentDefault;
    private Boolean negative;
}
