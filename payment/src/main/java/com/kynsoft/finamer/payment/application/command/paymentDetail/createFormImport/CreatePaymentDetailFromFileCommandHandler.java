package com.kynsoft.finamer.payment.application.command.paymentDetail.createFormImport;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.finamer.payment.application.command.paymentDetail.applyPayment.ApplyPaymentDetailCommand;
import com.kynsoft.finamer.payment.application.command.paymentDetail.applyPayment.ApplyPaymentDetailMessage;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.rules.paymentDetail.CheckAmountGreaterThanZeroStrictlyRule;
import com.kynsoft.finamer.payment.domain.rules.paymentDetail.CheckAmountIfGreaterThanPaymentBalanceRule;
import com.kynsoft.finamer.payment.domain.rules.paymentDetail.CheckIfNewPaymentDetailIsApplyDepositRule;
import com.kynsoft.finamer.payment.domain.rules.paymentDetail.CheckPaymentDetailAmountGreaterThanZeroRule;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.payment.domain.services.IPaymentDetailService;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class CreatePaymentDetailFromFileCommandHandler implements ICommandHandler<CreatePaymentDetailFromFileCommand> {

    private final IPaymentDetailService paymentDetailService;
    private final IManagePaymentTransactionTypeService paymentTransactionTypeService;
    private final IPaymentService paymentService;

    public CreatePaymentDetailFromFileCommandHandler(IPaymentDetailService paymentDetailService,
                                                     IManagePaymentTransactionTypeService paymentTransactionTypeService,
                                                     IPaymentService paymentService) {
        this.paymentDetailService = paymentDetailService;
        this.paymentTransactionTypeService = paymentTransactionTypeService;
        this.paymentService = paymentService;
    }

    @Override
    //@Transactional
    public void handle(CreatePaymentDetailFromFileCommand command) {

        ManagePaymentTransactionTypeDto paymentTransactionTypeDto = this.paymentTransactionTypeService.findById(command.getTransactionType());
        PaymentDto paymentDto = this.paymentService.findById(command.getPayment());

        ConsumerUpdate updatePayment = new ConsumerUpdate();

        RulesChecker.checkRule(new CheckPaymentDetailAmountGreaterThanZeroRule(command.getAmount()));
        RulesChecker.checkRule(new CheckIfNewPaymentDetailIsApplyDepositRule(paymentTransactionTypeDto.getApplyDeposit()));

        //identified and notIdentified
        if (paymentTransactionTypeDto.getCash()) {
            RulesChecker.checkRule(new CheckAmountGreaterThanZeroStrictlyRule(command.getAmount(), paymentDto.getPaymentBalance()));
            UpdateIfNotNull.updateDouble(paymentDto::setIdentified, paymentDto.getIdentified() + command.getAmount(), updatePayment::setUpdate);
            UpdateIfNotNull.updateDouble(paymentDto::setNotIdentified, paymentDto.getNotIdentified() - command.getAmount(), updatePayment::setUpdate);

            //Suma de trx tipo check Cash + Check Apply Deposit  en el Manage Payment Transaction Type
            UpdateIfNotNull.updateDouble(paymentDto::setApplied, paymentDto.getApplied() + command.getAmount(), updatePayment::setUpdate);

            //Las transacciones de tipo Cash se restan al Payment Balance.
            UpdateIfNotNull.updateDouble(paymentDto::setPaymentBalance, paymentDto.getPaymentBalance() - command.getAmount(), updatePayment::setUpdate);
            UpdateIfNotNull.updateDouble(paymentDto::setNotApplied, paymentDto.getNotApplied() - command.getAmount(), updatePayment::setUpdate);

            //Aplicando regla para el campo Remark
            if (!paymentTransactionTypeDto.getRemarkRequired()) {
                //RulesChecker.checkRule(new CheckMinNumberOfCharacterInRemarkRule(paymentTransactionTypeDto.getMinNumberOfCharacter(), command.getRemark()));
//                command.setRemark(paymentTransactionTypeDto.getDefaultRemark());
            }
        }

        //Other Deductions
        if (!paymentTransactionTypeDto.getCash() && !paymentTransactionTypeDto.getDeposit()) {
            UpdateIfNotNull.updateDouble(paymentDto::setOtherDeductions, paymentDto.getOtherDeductions() + command.getAmount(), updatePayment::setUpdate);

            //Aplicando regla para el campo Remark
            if (!paymentTransactionTypeDto.getRemarkRequired()) {
                //RulesChecker.checkRule(new CheckMinNumberOfCharacterInRemarkRule(paymentTransactionTypeDto.getMinNumberOfCharacter(), command.getRemark()));
//                command.setRemark(paymentTransactionTypeDto.getDefaultRemark());
            }
        }

        //Deposit Amount and Deposit Balance
        PaymentDetailDto newDetailDto = new PaymentDetailDto(
                command.getId(),
                command.getStatus() != null ? command.getStatus() : Status.ACTIVE,
                paymentDto,
                paymentTransactionTypeDto,
                command.getAmount(),
                command.getRemark(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false
        );
        if (paymentTransactionTypeDto.getDeposit()) {
            // Crear regla que valide que el Amount ingresado no debe de ser mayor que el valor del Payment Balance y mayor que cero.
            RulesChecker.checkRule(new CheckAmountIfGreaterThanPaymentBalanceRule(command.getAmount(), paymentDto.getPaymentBalance(), paymentDto.getDepositAmount()));
            UpdateIfNotNull.updateDouble(paymentDto::setDepositAmount, paymentDto.getDepositAmount() + command.getAmount(), updatePayment::setUpdate);
            UpdateIfNotNull.updateDouble(paymentDto::setDepositBalance, paymentDto.getDepositBalance() + command.getAmount(), updatePayment::setUpdate);
            if (paymentDto.getNotApplied() == null) {
                paymentDto.setNotApplied(0.0);
            }
            UpdateIfNotNull.updateDouble(paymentDto::setNotApplied, paymentDto.getNotApplied() - command.getAmount(), updatePayment::setUpdate);
            //Los Deposit deben de ser restados del Payment Balance, pero si sobre un Detalle de tipo Deposit se realiza Apply Deposit, ese valor hay que devolverselo al Payment Balance.
            UpdateIfNotNull.updateDouble(paymentDto::setPaymentBalance, paymentDto.getPaymentBalance() - command.getAmount(), updatePayment::setUpdate);
            newDetailDto.setAmount(command.getAmount() * -1);
            newDetailDto.setApplyDepositValue(command.getAmount());
            //Validar el Close Operation
            newDetailDto.setTransactionDate(OffsetDateTime.now(ZoneId.of("UTC")));
            newDetailDto.setPaymentDetailId(this.paymentDetailService.queryForNextPaymentDetailId());
        }

        this.paymentDetailService.create(newDetailDto);

        if (updatePayment.getUpdate() > 0) {
            this.paymentService.update(paymentDto);
//            createPaymentAttachmentStatusHistory(employeeDto, paymentDto, paymentDetail, msg);
        }
       // if (command.getApplyPayment() && paymentTransactionTypeDto.getCash()) {
            ApplyPaymentDetailMessage message = command.getMediator().send(new ApplyPaymentDetailCommand(command.getId(), command.getBooking(), command.getEmployee()));
            paymentDto.setApplyPayment(message.getPayment().isApplyPayment());
            paymentDto.setPaymentStatus(message.getPayment().getPaymentStatus());
       // }

        command.setPaymentResponse(paymentDto);
    }
}
