package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ParameterizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParameterizationResponse implements IResponse {

    private UUID id;
    private Boolean isActive;
    private String transactionStatusCode;

    public ParameterizationResponse(ParameterizationDto dto){
        this.id = dto.getId();
        this.isActive = dto.getIsActive();
        this.transactionStatusCode = dto.getTransactionStatusCode();
    }
}
