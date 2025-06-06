package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageHotelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageHotelResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private Boolean isApplyByVCC;
    private String status;
    private String address;

    public ManageHotelResponse(ManageHotelDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.isApplyByVCC = dto.getIsApplyByVCC();
        this.status = dto.getStatus();
        this.address = dto.getAddress();
    }
}
