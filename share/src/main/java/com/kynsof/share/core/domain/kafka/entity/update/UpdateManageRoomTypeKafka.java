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
public class UpdateManageRoomTypeKafka {

    private UUID id;
    private String name;
    private String status;
}
