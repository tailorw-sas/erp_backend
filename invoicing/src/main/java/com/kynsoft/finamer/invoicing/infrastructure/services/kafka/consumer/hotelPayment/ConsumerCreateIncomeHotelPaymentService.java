package com.kynsoft.finamer.invoicing.infrastructure.services.kafka.consumer.hotelPayment;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentErrors;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentFailedIncome;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentKafka;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentSuccessIncome;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.invoicing.application.command.checkDatesInCloseOperation.checkDates.CheckDatesInCloseOperationCommand;
import com.kynsoft.finamer.invoicing.application.command.income.create.CreateIncomeCommand;
import com.kynsoft.finamer.invoicing.application.command.income.create.CreateIncomeMessage;
import com.kynsoft.finamer.invoicing.application.command.incomeAdjustment.create.CreateIncomeAdjustmentCommand;
import com.kynsoft.finamer.invoicing.application.command.incomeAdjustment.create.NewIncomeAdjustmentRequest;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.rules.income.CheckAmountNotZeroRule;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceStatusService;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceTypeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConsumerCreateIncomeHotelPaymentService {

    private final IMediator mediator;
    private final IManageInvoiceTypeService invoiceTypeService;
    private final IManageInvoiceStatusService invoiceStatusService;
    private final KafkaTemplate<String, Object> producer;

    public ConsumerCreateIncomeHotelPaymentService(IMediator mediator, IManageInvoiceTypeService invoiceTypeService, IManageInvoiceStatusService invoiceStatusService, KafkaTemplate<String, Object> producer) {
        this.mediator = mediator;
        this.invoiceTypeService = invoiceTypeService;
        this.invoiceStatusService = invoiceStatusService;
        this.producer = producer;
    }

    @KafkaListener(topics = "finamer-replicate-hotel-payment", groupId = "income-entity-replica")
    public void listen(ReplicateHotelPaymentKafka objKafka) {
        String responseTopic = "finamer-replicate-hotel-payment-response";
        List<ReplicateHotelPaymentErrors> errors = new ArrayList<>();
        try {
            checkRules(objKafka.getAmount(), objKafka.getManageHotel());
            CreateIncomeMessage incomeMessage = this.mediator.send(createIncomeCommand(objKafka));
            this.mediator.send(createIncomeAdjustmentCommand(incomeMessage.getId(), objKafka.getAmount()));
            producer.send(responseTopic, new ReplicateHotelPaymentSuccessIncome(incomeMessage.getId(), objKafka.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ReplicateHotelPaymentErrors("Error", e.getMessage()));
            producer.send(responseTopic, new ReplicateHotelPaymentFailedIncome(
                    objKafka.getId(),
                    errors
            ));
        }
    }

    private CreateIncomeCommand createIncomeCommand(ReplicateHotelPaymentKafka objKafka) {
        return new CreateIncomeCommand(
                Status.ACTIVE,
                LocalDateTime.now(),
                false,
                null,
                objKafka.getManageHotel(),
                this.invoiceTypeService.findByEInvoiceType(EInvoiceType.INCOME).getId(),
                objKafka.getAmount(),
                0L,
                LocalDate.now(),
                false,
                null,
                this.invoiceStatusService.findByEInvoiceStatus(EInvoiceStatus.SENT).getId(),
                "",
                null
        );
    }

    private CreateIncomeAdjustmentCommand createIncomeAdjustmentCommand(UUID income, double amount){
        List<NewIncomeAdjustmentRequest> adjustmentRequests = new ArrayList<>();
        NewIncomeAdjustmentRequest adjustmentRequest = new NewIncomeAdjustmentRequest();
        adjustmentRequest.setAmount(amount);
        adjustmentRequest.setDate(LocalDate.now());
        adjustmentRequests.add(adjustmentRequest);
        return new CreateIncomeAdjustmentCommand(
                Status.ACTIVE,
                income,
                "",
                adjustmentRequests
        );
    }

    private void checkRules(double amount, UUID hotelId) {
        RulesChecker.checkRule(new CheckAmountNotZeroRule(amount));

        List<LocalDate> dates = new ArrayList<>();
        dates.add(LocalDate.now());
        this.mediator.send(new CheckDatesInCloseOperationCommand(hotelId, dates));
    }
}
