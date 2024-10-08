package com.kynsoft.finamer.settings.application.query.userMe;

import com.kynsof.share.core.domain.bus.query.IQuery;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserMeQuery implements IQuery {

    private final UUID id;

    public UserMeQuery(UUID id) {
        this.id = id;
    }

}
