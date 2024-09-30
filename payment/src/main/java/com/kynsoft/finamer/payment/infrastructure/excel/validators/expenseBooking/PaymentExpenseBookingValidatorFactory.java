package com.kynsoft.finamer.payment.infrastructure.excel.validators.expenseBooking;

import com.kynsof.share.core.application.excel.validator.IValidatorFactory;
import com.kynsoft.finamer.payment.domain.dto.ManageAgencyDto;
import com.kynsoft.finamer.payment.domain.dto.ManageClientDto;
import com.kynsoft.finamer.payment.domain.excel.bean.payment.PaymentExpenseBookingRow;
import com.kynsoft.finamer.payment.domain.excel.bean.payment.PaymentExpenseRow;
import com.kynsoft.finamer.payment.domain.excel.error.PaymentExpenseBookingRowError;
import com.kynsoft.finamer.payment.domain.excel.error.PaymentExpenseRowError;
import com.kynsoft.finamer.payment.domain.services.IManageAgencyService;
import com.kynsoft.finamer.payment.domain.services.IManageBookingService;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.payment.infrastructure.excel.event.error.expense.PaymentImportExpenseErrorEvent;
import com.kynsoft.finamer.payment.infrastructure.excel.event.error.expenseToBooking.PaymentImportExpenseToBookingErrorEvent;
import com.kynsoft.finamer.payment.infrastructure.excel.validators.CommonImportValidators;
import com.kynsoft.finamer.payment.infrastructure.excel.validators.expense.PaymentImportAmountValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PaymentExpenseBookingValidatorFactory extends IValidatorFactory<PaymentExpenseBookingRow> {

    private  BookingFieldValidator bookingFieldValidator;
    private  BookingBalanceValidator bookingBalanceValidator;
    private PaymentExpenseBookingTransactionTypeValidation transactionTypeValidation;
    private RemarksValidator remarksValidator;
    private final IManageBookingService bookingService;

    private final IManagePaymentTransactionTypeService paymentTransactionTypeService;


    public PaymentExpenseBookingValidatorFactory(ApplicationEventPublisher applicationEventPublisher,
                                                 IManageBookingService bookingService,
                                                 IManagePaymentTransactionTypeService paymentTransactionTypeService) {
        super(applicationEventPublisher);

        this.bookingService = bookingService;
        this.paymentTransactionTypeService = paymentTransactionTypeService;
    }
    @Override
    public void createValidators() {
            bookingBalanceValidator= new BookingBalanceValidator(applicationEventPublisher);
            bookingFieldValidator =  new BookingFieldValidator(applicationEventPublisher,bookingService);
            transactionTypeValidation = new PaymentExpenseBookingTransactionTypeValidation(applicationEventPublisher,paymentTransactionTypeService);
            remarksValidator = new RemarksValidator(applicationEventPublisher,paymentTransactionTypeService);
    }
    @Override
    public boolean validate(PaymentExpenseBookingRow toValidate) {
        bookingFieldValidator.validate(toValidate,errorFieldList);
        bookingBalanceValidator.validate(toValidate,errorFieldList);
        transactionTypeValidation.validate(toValidate,errorFieldList);
        remarksValidator.validate(toValidate,errorFieldList);
        if (this.hasErrors()) {
            PaymentImportExpenseToBookingErrorEvent paymentImportErrorEvent =
                    new PaymentImportExpenseToBookingErrorEvent(new PaymentExpenseBookingRowError(null, toValidate.getRowNumber(),
                            toValidate.getImportProcessId(), errorFieldList, toValidate));
            this.sendErrorEvent(paymentImportErrorEvent);
        }
        boolean result = !this.hasErrors();
        this.clearErrors();
        return result;
    }
}
