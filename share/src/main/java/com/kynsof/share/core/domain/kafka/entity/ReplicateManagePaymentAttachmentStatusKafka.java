package com.kynsof.share.core.domain.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateManagePaymentAttachmentStatusKafka implements Serializable {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("status")
    private String status;
    private Boolean defaults;
    private boolean nonNone;
    private boolean patWithAttachment;
    private boolean pwaWithOutAttachment;
    private boolean supported;
    private boolean otherSupport;
}
