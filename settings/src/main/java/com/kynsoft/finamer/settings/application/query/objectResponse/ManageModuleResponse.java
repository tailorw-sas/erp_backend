package com.kynsoft.finamer.settings.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageModuleDto;
import com.kynsoft.finamer.settings.domain.dto.ModuleDto;
import com.kynsoft.finamer.settings.domain.dto.ModuleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageModuleResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private ModuleStatus status;
    private List<ManagePermissionResponse> permissions;

    public ManageModuleResponse(ModuleDto dto){
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
        this.permissions = dto.getPermissions().stream().map(ManagePermissionResponse::new).toList();
    }
}
