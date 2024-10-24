package com.kynsoft.finamer.creditcard.domain.rules.managerAccountType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagerAccountTypeCodeSizeRule extends BusinessRule {

    private final String code;

    public ManagerAccountTypeCodeSizeRule(String code) {
        super(
                DomainErrorMessage.MANAGER_ACCOUNT_TYPE_CODE_SIZE, 
                new ErrorField("code", "The Manager Account Type code is not accepted.")
        );
        this.code = code;
    }

    @Override
    public boolean isBroken() {
        return !validateCode(code);
    }

    private boolean validateCode(String code) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]{3,5}$");
        Matcher matcher = pattern.matcher(code);

        return matcher.matches();
    }
}
