package com.kynsoft.finamer.payment.infrastructure.excel.validators.expenseBooking;

import com.kynsof.share.core.application.excel.validator.ExcelRuleValidator;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.excel.bean.payment.PaymentExpenseBookingRow;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Objects;

public class PaymentExpenseBookingTransactionTypeValidation extends ExcelRuleValidator<PaymentExpenseBookingRow> {

    private final IManagePaymentTransactionTypeService managePaymentTransactionTypeService;
    protected PaymentExpenseBookingTransactionTypeValidation(ApplicationEventPublisher applicationEventPublisher, IManagePaymentTransactionTypeService managePaymentTransactionTypeService) {
        super(applicationEventPublisher);
        this.managePaymentTransactionTypeService = managePaymentTransactionTypeService;
    }

    @Override
    public boolean validate(PaymentExpenseBookingRow obj, List<ErrorField> errorFieldList) {

        if (Objects.isNull(obj.getTransactionType())){
            errorFieldList.add(new ErrorField("transactionType","Transaction Type can't be empty"));
            return false;
        }

        try {
             ManagePaymentTransactionTypeDto transactionTypeDto = managePaymentTransactionTypeService.findByCode(obj.getTransactionType());
             if (!transactionTypeDto.isExpenseToBooking()) {
                errorFieldList.add(new ErrorField("transactionType","Transaction Type is not expense to booking."));
                return false;
            }
        } catch (Exception e) {
            errorFieldList.add(new ErrorField("transactionType","Transaction Type not exist"));
            return false;
        }

        return true;
    }
}
