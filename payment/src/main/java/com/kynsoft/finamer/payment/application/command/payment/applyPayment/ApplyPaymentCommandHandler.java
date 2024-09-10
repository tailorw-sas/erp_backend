package com.kynsoft.finamer.payment.application.command.payment.applyPayment;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsoft.finamer.payment.application.command.paymentDetail.applyPayment.ApplyPaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.createPaymentDetailsTypeCash.CreatePaymentDetailTypeCashCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.createPaymentDetailsTypeCash.CreatePaymentDetailTypeCashMessage;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceService;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ApplyPaymentCommandHandler implements ICommandHandler<ApplyPaymentCommand> {

    private final IManageInvoiceService manageInvoiceService;
    private final IPaymentService paymentService;

    public ApplyPaymentCommandHandler(IPaymentService paymentService,
            IManageInvoiceService manageInvoiceService) {
        this.paymentService = paymentService;
        this.manageInvoiceService = manageInvoiceService;
    }

    @Override
    public void handle(ApplyPaymentCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getPayment(), "id", "Payment ID cannot be null."));
        PaymentDto paymentDto = this.paymentService.findById(command.getPayment());

        List<ManageInvoiceDto> invoiceQueue = new ArrayList<>();

        double paymentBalance = paymentDto.getPaymentBalance();
        double invoiceTotal = 0.0;
        for (UUID invoice : command.getInvoices()) {
            ManageInvoiceDto invoiceDto = this.manageInvoiceService.findById(invoice);
            invoiceQueue.add(invoiceDto);
            invoiceTotal += invoiceDto.getInvoiceAmount();
        }

        Collections.sort(invoiceQueue, Comparator.comparingDouble(m -> m.getInvoiceAmount()));

        double notApplied = paymentDto.getNotApplied();
        for (ManageInvoiceDto manageInvoiceDto : invoiceQueue) {
            List<ManageBookingDto> bookingDtos = new ArrayList<>();
            if (!invoiceQueue.isEmpty()) {
                bookingDtos.addAll(manageInvoiceDto.getBookings());
                Collections.sort(bookingDtos, Comparator.comparingDouble(m -> m.getAmountBalance()));
                for (ManageBookingDto bookingDto : bookingDtos) {
                    if (notApplied > 0) {
                        double amountToApply = Math.min(notApplied, bookingDto.getAmountBalance());
                        CreatePaymentDetailTypeCashMessage message = command.getMediator().send(new CreatePaymentDetailTypeCashCommand(paymentDto, bookingDto.getId(), amountToApply, true));
                        command.getMediator().send(new ApplyPaymentDetailCommand(message.getId(), bookingDto.getId()));
                        notApplied = notApplied - amountToApply;
                        invoiceTotal = invoiceTotal - amountToApply;
                        paymentBalance = paymentBalance - amountToApply;
                    } else {
                        break;
                    }
                }
            }
            if (notApplied == 0) {
                break;
            }
        }
    }

}
// Hay que disminuir el Payment Balance
// Disminuir siempre que se aplique un pago el valor del booking al invoiceTotal y al payment balance.
// Si el valor del payment balance se hace cero y invoiceTotal todavia tiene valor y notApplied todavia es diferente de cero.
// Entonces se puede Aplicar pago sobre trx tipo Deposit si fueron seleccionadas.