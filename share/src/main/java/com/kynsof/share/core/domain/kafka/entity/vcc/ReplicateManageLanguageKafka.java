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
public class ReplicateManageLanguageKafka {

    private UUID id;
    private String code;
    private String name;
    private Boolean defaults;
    private String status;
}
