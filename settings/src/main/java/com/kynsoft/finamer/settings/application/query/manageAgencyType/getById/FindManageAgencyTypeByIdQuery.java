package com.kynsoft.finamer.settings.application.query.manageAgencyType.getById;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindManageAgencyTypeByIdQuery implements IQuery {
    private UUID id;
}
