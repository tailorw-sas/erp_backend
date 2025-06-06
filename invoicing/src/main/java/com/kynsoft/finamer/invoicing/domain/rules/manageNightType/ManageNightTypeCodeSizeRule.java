package com.kynsoft.finamer.invoicing.domain.rules.manageNightType;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageNightTypeCodeSizeRule extends BusinessRule {

    private final String code;

    public ManageNightTypeCodeSizeRule(String code) {
        super(
                DomainErrorMessage.CODE_SIZE, 
                new ErrorField("code", "The manage night type code is not accepted.")
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
