package com.kynsoft.finamer.settings.application.query.objectResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageReconcileTransactionStatusDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.NavigateReconcileTransactionStatus;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageReconcileTransactionStatusResponse  implements IResponse {
    private UUID id;
    private String code;
    private Status status;
    private String description;

    private String name;
    private Boolean requireValidation;
    private List<ManageReconcileTransactionStatusResponse> navigate;

    public ManageReconcileTransactionStatusResponse(ManageReconcileTransactionStatusDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.status = dto.getStatus();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.requireValidation = dto.getRequireValidation();
        this.navigate = dto.getRelatedStatuses() != null ? dto.getRelatedStatuses().stream().map(ManageReconcileTransactionStatusResponse::new).toList() : null;
    }

}
