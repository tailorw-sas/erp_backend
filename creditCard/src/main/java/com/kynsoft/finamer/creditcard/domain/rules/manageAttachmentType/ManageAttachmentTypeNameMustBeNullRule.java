package com.kynsoft.finamer.creditcard.domain.rules.manageAttachmentType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

public class ManageAttachmentTypeNameMustBeNullRule extends BusinessRule {

    private final String name;

    public ManageAttachmentTypeNameMustBeNullRule(String name) {
        super(
                DomainErrorMessage.MANAGE_ATTACHMENT_TYPE_NAME_CANNOT_BE_EMPTY,
                new ErrorField("name", "The name cannot be empty.")
        );
        this.name = name;
    }

    @Override
    public boolean isBroken() {
        return this.name == null || this.name.isEmpty();
    }
}
