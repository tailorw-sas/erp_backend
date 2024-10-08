package com.kynsof.share.core.domain.kafka.entity.update;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManageAttachmentTypeKafka implements Serializable {

    private UUID id;
    private String name;
    private String status;
    private Boolean defaults;
    private boolean attachInvDefault;
}
