package com.kynsoft.report.applications.command.jasperReportTemplate.update;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.ConsumerUpdate;
import com.kynsof.share.utils.UpdateIfNotNull;
import com.kynsoft.report.domain.dto.DBConectionDto;
import com.kynsoft.report.domain.dto.JasperReportTemplateDto;
import com.kynsoft.report.domain.dto.JasperReportTemplateType;
import com.kynsoft.report.domain.dto.status.Status;
import com.kynsoft.report.domain.services.IDBConnectionService;
import com.kynsoft.report.domain.services.IJasperReportTemplateService;

import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;

@Component
public class UpdateJasperReportTemplateCommandHandler implements ICommandHandler<UpdateJasperReportTemplateCommand> {

    private final IJasperReportTemplateService service;

    private final IDBConnectionService conectionService;

    public UpdateJasperReportTemplateCommandHandler(IJasperReportTemplateService service, IDBConnectionService conectionService) {
        this.service = service;
        this.conectionService = conectionService;
    }

    @Override
    public void handle(UpdateJasperReportTemplateCommand command) {
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getId(), "id", "JasperReportTemplate ID cannot be null."));
        JasperReportTemplateDto updateDto = this.service.findById(command.getId());

        ConsumerUpdate update = new ConsumerUpdate();
        updateStatus(updateDto::setStatus, command.getStatus(), updateDto.getStatus(), update::setUpdate);
        updateType(updateDto::setType, command.getType(), updateDto.getType(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(updateDto::setDescription, command.getDescription(), updateDto.getDescription(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(updateDto::setFile, command.getFile(), updateDto.getFile(), update::setUpdate);
        UpdateIfNotNull.updateIfStringNotNullNotEmptyAndNotEquals(updateDto::setName, command.getName(), updateDto.getName(), update::setUpdate);

        UpdateIfNotNull.updateDouble(updateDto::setMenuPosition, command.getMenuPosition(), updateDto.getMenuPosition(), update::setUpdate);


        updateDto.setModuleSystems(command.getModuleSystems());
     //   updateDto.setDataValueStatic(command.getDataValueStatic());

        this.updateConection(updateDto::setDbConectionDto, command.getDbConection(), updateDto.getDbConectionDto() != null ? updateDto.getDbConectionDto().getId() : null,update::setUpdate);
            this.service.update(updateDto);

//        if (update.getUpdate() > 0) {
//        }
    }

    private void updateStatus(Consumer<Status> setter, Status newValue, Status oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);
        }
    }

    private void updateType(Consumer<JasperReportTemplateType> setter, JasperReportTemplateType newValue, JasperReportTemplateType oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
            update.accept(1);
        }
    }

    private void updateConection(Consumer<DBConectionDto> setter, UUID newValue, UUID oldValue, Consumer<Integer> update) {
        if (newValue != null && !newValue.equals(oldValue)) {
            DBConectionDto conectionDto = this.conectionService.findById(newValue);
            setter.accept(conectionDto);
            update.accept(1);
        }
    }

}
