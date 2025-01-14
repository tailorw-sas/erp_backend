package com.kynsoft.report.applications.command.jasperReportParameter.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateJasperReportParameterRequest {

    private String paramName;
    private String type;
    private String module;
    private String service;
    private String label;
    private String componentType;
    private String reportClass;
    private String reportValidation;
    private UUID reportId;
    private int parameterPosition;
    private String dependentField;
}
