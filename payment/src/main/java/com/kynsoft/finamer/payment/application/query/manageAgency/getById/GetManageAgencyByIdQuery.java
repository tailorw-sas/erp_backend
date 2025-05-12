package com.kynsoft.finamer.payment.application.query.manageAgency.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class GetManageAgencyByIdQuery implements IQuery {

    private UUID id;
}
