package com.kynsoft.finamer.audit.application.query.configuration.getbyid;


import com.kynsoft.finamer.audit.domain.bus.query.IQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@Getter
@AllArgsConstructor
public class GetConfigurationByIdQuery implements IQuery {
    private final UUID id;
}
