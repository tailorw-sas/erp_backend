package com.kynsof.share.core.domain.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplicateManageCountryKafka implements Serializable {
    private UUID id;
    private UUID language;
    private String code;
    private String name;
    private String description;
    private String status;
    private boolean isDefault;
    private String iso3;
}
