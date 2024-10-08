package com.kynsoft.finamer.settings.domain.rules.manageClient;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageClientCodeSizeRule extends BusinessRule {

    private final String code;

    public ManageClientCodeSizeRule(String code) {
        super(
                DomainErrorMessage.MANAGER_CLIENT_CODE_SIZE, 
                new ErrorField("code", "The code is not accepted.")
        );
        this.code = code;
    }

    @Override
    public boolean isBroken() {
        return !validateCode(code);
    }

    private boolean validateCode(String code) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{3,15}$");
        Matcher matcher = pattern.matcher(code);

        return matcher.matches();
    }
}
