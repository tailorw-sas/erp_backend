package com.kynsoft.finamer.creditcard.domain.rules.manageVCCTransactionType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

public class ManageVCCTransactionTypeNameMustNotBeNullRule extends BusinessRule {

    private final String name;

    public ManageVCCTransactionTypeNameMustNotBeNullRule(String name) {
        super(
                DomainErrorMessage.MANAGER_B2BPARTNER_NAME_CANNOT_BE_EMPTY, 
                new ErrorField("name", "The name of the B2B Partner cannot be empty.")
        );
        this.name = name;
    }

    @Override
    public boolean isBroken() {
        return this.name == null || this.name.isEmpty();
    }

}
