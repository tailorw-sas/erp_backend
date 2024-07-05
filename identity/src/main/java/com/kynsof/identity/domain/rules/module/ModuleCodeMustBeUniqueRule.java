package com.kynsof.identity.domain.rules.module;

import com.kynsof.identity.domain.interfaces.service.IModuleService;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

import java.util.UUID;

public class ModuleCodeMustBeUniqueRule extends BusinessRule {

    private final IModuleService service;

    private final String code;

    private final UUID id;

    public ModuleCodeMustBeUniqueRule(IModuleService service, String code, UUID id) {
        super(
                DomainErrorMessage.ITEM_ALREADY_EXITS,
                new ErrorField("code", DomainErrorMessage.ITEM_ALREADY_EXITS.getReasonPhrase())
        );
        this.service = service;
        this.code = code;
        this.id = id;
    }

    @Override
    public boolean isBroken() {
        return this.service.countByCodeAndNotId(code, id) > 0;
    }

}
