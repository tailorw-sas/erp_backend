package com.kynsoft.finamer.payment.domain.rules.attachmentType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;
import com.kynsoft.finamer.payment.domain.services.IManageAttachmentTypeService;

import java.util.UUID;

public class AttachmentTypeCodeMustBeUniqueRule extends BusinessRule {

    private final IManageAttachmentTypeService service;

    private final String code;

    private final UUID id;

    public AttachmentTypeCodeMustBeUniqueRule(IManageAttachmentTypeService service, String code, UUID id) {
        super(
                DomainErrorMessage.ITEM_ALREADY_EXITS,
                new ErrorField("defaults", DomainErrorMessage.ITEM_ALREADY_EXITS.getReasonPhrase())
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
