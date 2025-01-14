package com.kynsoft.finamer.payment.infrastructure.excel.validators.bank;

import com.kynsof.share.core.application.excel.validator.ExcelRuleValidator;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.payment.domain.dto.ManageBankAccountDto;
import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.excel.bean.payment.PaymentBankRow;
import com.kynsoft.finamer.payment.domain.services.IManageBankAccountService;
import com.kynsoft.finamer.payment.domain.services.IManageHotelService;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Objects;

public class PaymentImportBankAccountValidator extends ExcelRuleValidator<PaymentBankRow> {

    private final IManageBankAccountService bankAccountService;
    private final IManageHotelService manageHotelService;

    protected PaymentImportBankAccountValidator(ApplicationEventPublisher applicationEventPublisher,
            IManageBankAccountService bankAccountService,
            IManageHotelService manageHotelService) {
        super(applicationEventPublisher);
        this.bankAccountService = bankAccountService;
        this.manageHotelService = manageHotelService;
    }

    @Override
    public boolean validate(PaymentBankRow obj, List<ErrorField> errorFieldList) {
        if (Objects.isNull(obj.getBankAccount())) {
            errorFieldList.add(new ErrorField("Bank Account", "Bank Account can't be empty."));
            return false;
        }
        boolean result = bankAccountService.existByAccountNumber(obj.getBankAccount());
        if (!result) {
            errorFieldList.add(new ErrorField("Bank Account", "The bank account not exist."));
            return false;
        } else {
            try {
                ManageBankAccountDto manageBankAccountDto = bankAccountService.findManageBankAccountByCodeAndHotelCode(obj.getBankAccount(), obj.getManageHotelCode());
                //ManageBankAccountDto manageBankAccountDto = bankAccountService.findByAccountNumber(obj.getBankAccount());
                if (Status.INACTIVE.name().equals(manageBankAccountDto.getStatus())) {
                    errorFieldList.add(new ErrorField("Bank Account", "The bank account is not active."));
                    return false;
                }
            } catch (Exception e) {
                errorFieldList.add(new ErrorField("Bank Account", "The bank account doesn't belong to the hotel."));
                return false;
            }
        }
        return true;
    }

}
