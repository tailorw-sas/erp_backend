package com.kynsoft.finamer.invoicing.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageRatePlanDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManageRatePlanResponse implements IResponse {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private ManageHotelResponse hotel;

    public ManageRatePlanResponse(ManageRatePlanDto dto){
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
        this.hotel = dto.getHotel() != null ? new ManageHotelResponse(dto.getHotel()) : null;
    }
}
