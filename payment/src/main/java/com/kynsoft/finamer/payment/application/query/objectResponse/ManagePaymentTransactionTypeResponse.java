package com.kynsoft.finamer.payment.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagePaymentTransactionTypeResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private Boolean cash;
    private Boolean deposit;
    private Boolean applyDeposit;

    public ManagePaymentTransactionTypeResponse(ManagePaymentTransactionTypeDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
        this.cash = dto.getCash();
        this.deposit = dto.getDeposit();
        this.applyDeposit = dto.getApplyDeposit();
    }

}
