package com.kynsoft.finamer.invoicing.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageRoomCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageRoomCategoryResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private String status;

    public ManageRoomCategoryResponse(ManageRoomCategoryDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
    }

}
