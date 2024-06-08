package com.kynsoft.finamer.settings.domain.rules.managerMerchant;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagerMerchantCodeRule extends BusinessRule {

    private final String code;

    public ManagerMerchantCodeRule(String code) {
        super(
                DomainErrorMessage.MODULE_NAME_CANNOT_BE_EMPTY, 
                new ErrorField("code", "The code is not accepted.")
        );
        this.code = code;
    }

    @Override
    public boolean isBroken() {
        return !validateCode(code);
    }

    private boolean validateCode(String code) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]{3,10}$");
        Matcher matcher = pattern.matcher(code);

        return matcher.matches();
    }
}
