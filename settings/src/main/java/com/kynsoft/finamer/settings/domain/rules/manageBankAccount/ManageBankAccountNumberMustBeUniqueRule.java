package com.kynsoft.finamer.settings.domain.rules.manageBankAccount;

import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.rules.BusinessRule;
import com.kynsoft.finamer.settings.domain.services.IManageBankAccountService;

import java.util.UUID;

public class ManageBankAccountNumberMustBeUniqueRule extends BusinessRule {

    private final IManageBankAccountService service;

    private final String accountNumber;

    private final UUID id;
    private final UUID bankId;

    public ManageBankAccountNumberMustBeUniqueRule(IManageBankAccountService service, String accountNumber, UUID id, UUID bankId) {
        super(
                DomainErrorMessage.ITEM_ALREADY_EXITS,
                new ErrorField("accountNumber", DomainErrorMessage.ITEM_ALREADY_EXITS.getReasonPhrase())
        );
        this.service = service;
        this.accountNumber = accountNumber;
        this.id = id;
        this.bankId = bankId;
    }

    @Override
    public boolean isBroken() {
        return this.service.countByAccountNumberAndNotId(accountNumber, id, bankId) > 0;
    }
}
