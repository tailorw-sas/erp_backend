package com.kynsoft.finamer.creditcard.application.query.transaction.search;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageTransactionStatusDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageSearchTransactionStatusResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private Boolean visible;
    private Boolean isDeclined;
    private Boolean isSent;
    private Boolean isCancelled;
    private Boolean isPaid;

    public ManageSearchTransactionStatusResponse(ManageTransactionStatusDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.visible = dto.getVisible();
        this.isDeclined = dto.isDeclinedStatus();
        this.isSent = dto.isSentStatus();
        this.isCancelled = dto.isCancelledStatus();
        this.isPaid = dto.isPaidStatus();
    }

}