package com.kynsoft.finamer.settings.application.command.managePaymentTransactionType.create;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.ReplicateManagePaymentTransactionTypeKafka;
import com.kynsoft.finamer.settings.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.settings.domain.rules.managePaymentTransactionType.ManagePaymentTransactionTypeCodeMustBeUniqueRule;
import com.kynsoft.finamer.settings.domain.rules.managePaymentTransactionType.ManagePaymentTransactionTypeCodeSizeRule;
import com.kynsoft.finamer.settings.domain.rules.managePaymentTransactionType.ManagePaymentTransactionTypeDefaultMustBeUniqueRule;
import com.kynsoft.finamer.settings.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.managePaymentTransactionType.ProducerReplicateManagePaymentTransactionTypeService;
import org.springframework.stereotype.Component;

@Component
public class CreateManagePaymentTransactionTypeCommandHandler implements ICommandHandler<CreateManagePaymentTransactionTypeCommand> {

    private final IManagePaymentTransactionTypeService service;

    private final ProducerReplicateManagePaymentTransactionTypeService producerReplicateManagePaymentTransactionTypeService;

    public CreateManagePaymentTransactionTypeCommandHandler(IManagePaymentTransactionTypeService service,
            ProducerReplicateManagePaymentTransactionTypeService producerReplicateManagePaymentTransactionTypeService) {
        this.service = service;
        this.producerReplicateManagePaymentTransactionTypeService = producerReplicateManagePaymentTransactionTypeService;
    }

    @Override
    public void handle(CreateManagePaymentTransactionTypeCommand command) {
        RulesChecker.checkRule(new ManagePaymentTransactionTypeCodeSizeRule(command.getCode()));
        RulesChecker.checkRule(new ManagePaymentTransactionTypeCodeMustBeUniqueRule(service, command.getCode(), command.getId()));
        if (command.getDefaults()) {
            RulesChecker.checkRule(new ManagePaymentTransactionTypeDefaultMustBeUniqueRule(service, command.getId()));
        }

        service.create(new ManagePaymentTransactionTypeDto(
                command.getId(),
                command.getCode(),
                command.getDescription(),
                command.getStatus(),
                command.getName(),
                command.getCash(),
                command.getAgencyRateAmount(),
                command.getNegative(),
                command.getPolicyCredit(),
                command.getRemarkRequired(),
                command.getMinNumberOfCharacter(),
                command.getDefaultRemark(),
                command.getDeposit(),
                command.getApplyDeposit(),
                command.getDefaults()
        ));
        this.producerReplicateManagePaymentTransactionTypeService
                .create(new ReplicateManagePaymentTransactionTypeKafka(
                        command.getId(),
                        command.getCode(),
                        command.getName(),
                        command.getStatus().name(),
                        command.getDeposit(),
                        command.getApplyDeposit(),
                        command.getCash(),
                        command.getRemarkRequired(),
                        command.getMinNumberOfCharacter()
                ));
    }
}
