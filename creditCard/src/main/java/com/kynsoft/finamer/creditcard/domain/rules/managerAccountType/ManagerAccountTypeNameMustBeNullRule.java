package com.kynsoft.finamer.creditcard.domain.rules.managerAccountType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

public class ManagerAccountTypeNameMustBeNullRule extends BusinessRule {

    private final String name;

    public ManagerAccountTypeNameMustBeNullRule(String name) {
        super(
                DomainErrorMessage.MANAGER_ACCOUNT_TYPE_NAME_CANNOT_BE_EMPTY, 
                new ErrorField("name", "The name of the Manager Account Type cannot be empty.")
        );
        this.name = name;
    }

    @Override
    public boolean isBroken() {
        return this.name == null || this.name.isEmpty();
    }

}
