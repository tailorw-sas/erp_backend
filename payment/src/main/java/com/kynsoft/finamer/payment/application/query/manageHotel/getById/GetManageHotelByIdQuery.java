package com.kynsoft.finamer.payment.application.query.manageHotel.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetManageHotelByIdQuery implements IQuery {

    private UUID id;
}
