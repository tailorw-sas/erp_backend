package com.kynsoft.finamer.payment.infrastructure.excel.validators.detail;

import com.kynsof.share.core.application.excel.validator.ExcelRuleValidator;
import com.kynsof.share.core.domain.response.ErrorField;

import com.kynsoft.finamer.payment.domain.excel.bean.detail.PaymentDetailRow;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Objects;

public class PaymentDetailExistPaymentValidator extends ExcelRuleValidator<PaymentDetailRow> {

    private final IPaymentService paymentService;
    protected PaymentDetailExistPaymentValidator(ApplicationEventPublisher applicationEventPublisher, IPaymentService paymentService) {
        super(applicationEventPublisher);
        this.paymentService = paymentService;
    }

    @Override
    public boolean validate(PaymentDetailRow obj, List<ErrorField> errorFieldList) {
         if (Objects.isNull(obj.getPaymentId()) || !paymentService.existPayment(Long.parseLong(obj.getPaymentId()))){
             errorFieldList.add(new ErrorField("Payment id","Payment not exist"));
             return false;
         }
        return true;
    }
}
