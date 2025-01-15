package com.kynsoft.report.applications.query.jasperReportParameter.getById;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.report.applications.query.jasperreporttemplate.getbyid.JasperReportTemplateResponse;
import com.kynsoft.report.domain.dto.DBConectionDto;
import com.kynsoft.report.domain.dto.JasperReportParameterDto;
import com.kynsoft.report.domain.dto.JasperReportTemplateDto;
import com.kynsoft.report.domain.dto.status.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JasperReportParameterResponse implements IResponse {

    private UUID id;
    private String paramName;
    private String type;
    private String module;
    private String service;
    private String label;
    private String componentType;
    private JasperReportTemplateResponse jasperReportTemplate;
    private String reportClass;
    private String reportValidation;
    private int parameterPosition;
    private String dependentField;
    private String filterKeyValue;
    public JasperReportParameterResponse(JasperReportParameterDto dto){
        this.id = dto.getId();
         paramName = dto.getParamName();
         type = dto.getType();
         module = dto.getModule();
         service = dto.getService();
         label = dto.getLabel();
         componentType = dto.getComponentType();
         jasperReportTemplate = new JasperReportTemplateResponse(dto.getJasperReportTemplate());
         reportClass = dto.getReportClass();
         reportValidation = dto.getReportValidation();
         parameterPosition = dto.getParameterPosition();
         dependentField = dto.getDependentField();
         filterKeyValue = dto.getFilterKeyValue();
    }
}
