package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageVCCTransactionTypeDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageVCCTransactionTypeResponse implements IResponse {
    private UUID id;
    private String code;
    private String name;
    private Boolean subcategory;
    private boolean manual;
    private Status status;

    public ManageVCCTransactionTypeResponse(ManageVCCTransactionTypeDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.subcategory = dto.getSubcategory();
        this.manual = dto.isManual();
        this.status = dto.getStatus();
    }

}
