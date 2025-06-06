package com.kynsoft.report.applications.command.generateTemplate;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GenerateTemplateCommand implements ICommand {
    private  byte[] result;
    private final Map<String, Object> parameters;
    private final String JasperReportCode;
    private final String reportFormatType;

    public GenerateTemplateCommand(Map<String, Object> parameters, String jasperReportCode, String reportFormatType) {

        this.parameters = parameters;
        JasperReportCode = jasperReportCode;
        this.reportFormatType = reportFormatType;
    }

//    public static GenerateTemplateCommand fromRequest(GenerateTemplateRequest request) {
//        return new GenerateTemplateCommand(request.getTitle(), request.getDescription(), request.getType(), request.getImage(), request.getLink());
//    }

    @Override
    public ICommandMessage getMessage() {
        return new GenerateTemplateMessage(result);
    }
}
