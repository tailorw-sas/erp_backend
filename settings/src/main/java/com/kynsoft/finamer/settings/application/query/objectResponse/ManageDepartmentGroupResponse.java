package com.kynsoft.finamer.settings.application.query.objectResponse;

import java.util.UUID;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageDepartmentGroupDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageDepartmentGroupResponse  implements IResponse {
    private UUID id;
    private String code;
    private Status status;
    private String description;
    private String name;


    public ManageDepartmentGroupResponse(ManageDepartmentGroupDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.status = dto.getStatus();
        this.name = dto.getName();
        this.description = dto.getDescription();
    }
}
