package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageMerchantCommissionDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.CalculationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageMerchantCommissionResponse implements IResponse {

    private UUID id;
    private ManageMerchantResponse manageMerchant;
    private ManageCreditCardTypeResponse manageCreditCartType;
    private Double commission;
    private CalculationType calculationType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;

    public ManageMerchantCommissionResponse(ManageMerchantCommissionDto dto) {
        this.id = dto.getId();
        this.manageMerchant = dto.getManagerMerchant() != null ? new ManageMerchantResponse(dto.getManagerMerchant()) : null;
        this.manageCreditCartType = dto.getManageCreditCartType() != null ? new ManageCreditCardTypeResponse(dto.getManageCreditCartType()) : null;
        this.commission = dto.getCommission();
        this.calculationType = dto.getCalculationType();
        this.fromDate = dto.getFromDate();
        this.toDate = dto.getToDate();
        this.status = dto.getStatus();
    }

}
