package com.kynsoft.finamer.invoicing.application.command.manageInvoiceStatus.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.ReplicateManageInvoiceStatusKafka;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceStatusDto;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusCanceledMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusCodeMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusCodeSizeRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusNameMustBeNullRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusProcessedMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusReconciledMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.rules.manageInvoiceStatus.ManageInvoiceStatusSentMustBeUniqueRule;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceStatusService;
import com.kynsoft.finamer.invoicing.infrastructure.services.kafka.producer.manageInvoiceStatus.ProducerReplicateManageInvoiceStatusService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateManageInvoiceStatusCommandHandler implements ICommandHandler<CreateManageInvoiceStatusCommand> {

    private final IManageInvoiceStatusService service;
    private final ProducerReplicateManageInvoiceStatusService producerReplicateManageInvoiceStatusService;

    public CreateManageInvoiceStatusCommandHandler(IManageInvoiceStatusService service,
                                                   ProducerReplicateManageInvoiceStatusService producerReplicateManageInvoiceStatusService) {
        this.service = service;
        this.producerReplicateManageInvoiceStatusService = producerReplicateManageInvoiceStatusService;
    }

    @Override
    public void handle(CreateManageInvoiceStatusCommand command) {
        RulesChecker.checkRule(new ManageInvoiceStatusCodeSizeRule(command.getCode()));
        RulesChecker.checkRule(new ManageInvoiceStatusNameMustBeNullRule(command.getName()));
        RulesChecker.checkRule(new ManageInvoiceStatusCodeMustBeUniqueRule(this.service, command.getCode(), command.getId()));

        if (command.isSentStatus()) {
            RulesChecker.checkRule(new ManageInvoiceStatusSentMustBeUniqueRule(service, command.getId()));
        }
        if (command.isCanceledStatus()) {
            RulesChecker.checkRule(new ManageInvoiceStatusCanceledMustBeUniqueRule(service, command.getId()));
        }
        if (command.isReconciledStatus()) {
            RulesChecker.checkRule(new ManageInvoiceStatusReconciledMustBeUniqueRule(service, command.getId()));
        }
        if (command.getProcessStatus()) {
            RulesChecker.checkRule(new ManageInvoiceStatusProcessedMustBeUniqueRule(service, command.getId()));
        }

        List<ManageInvoiceStatusDto> manageInvoiceStatusDtoList = service.findByIds(command.getNavigate());

        service.create(new ManageInvoiceStatusDto(
                command.getId(),
                command.getCode(),
                command.getDescription(),
                command.getStatus(),
                command.getName(),
                command.getEnabledToPrint(),
                command.getEnabledToPropagate(),
                command.getEnabledToApply(),
                command.getEnabledToPolicy(),
                command.getProcessStatus(),
                manageInvoiceStatusDtoList,
                command.getShowClone(),
                command.isSentStatus(),
                command.isReconciledStatus(),
                command.isCanceledStatus()
        ));

        this.producerReplicateManageInvoiceStatusService.create(new ReplicateManageInvoiceStatusKafka(
                command.getId(), 
                command.getCode(), 
                command.getName(),
                command.getShowClone())
        );
    }
}
