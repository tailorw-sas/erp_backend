package com.tailorw.finamer.scheduler.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.tailorw.finamer.scheduler.domain.dto.BusinessProcessSchedulerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessProcessSchedulerResponse implements IResponse {

    private UUID id;
    private FrequencyResponse frequency;
    private IntervalTypeResponse intervalType;
    private Integer interval;
    private ExecutionDateTypeResponse executionDateType;
    private String executionDateValue;
    private LocalDate executionDate;
    private String executionTime;
    private ProcessingDateTypeResponse processingDateType;
    private LocalDate processingDate;
    private Integer processingDateValue;
    private String status;
    private String params;
    private LocalDateTime lastExecutionDatetime;
    private BusinessProcessResponse process;
    private boolean allowsQueueing;

    public BusinessProcessSchedulerResponse(BusinessProcessSchedulerDto dto){
        this.id = dto.getId();
        this.frequency = Objects.nonNull(dto.getFrequency()) ? new FrequencyResponse(dto.getFrequency()) : null;
        this.intervalType = Objects.nonNull(dto.getIntervalType()) ? new IntervalTypeResponse(dto.getIntervalType()) : null;
        this.interval = dto.getInterval();
        this.executionDateType = Objects.nonNull(dto.getExecutionDateType()) ? new ExecutionDateTypeResponse(dto.getExecutionDateType()) : null;
        this.executionDateValue = dto.getExecutionDateValue();
        this.executionDate = dto.getExecutionDate();
        this.executionTime = Objects.nonNull(dto.getExecutionTime()) ? dto.getExecutionTime().format(DateTimeFormatter.ofPattern("HH:mm")) : null;
        this.processingDateType = Objects.nonNull(dto.getProcessingDateType()) ? new ProcessingDateTypeResponse(dto.getProcessingDateType()) : null;
        this.processingDate = dto.getProcessingDate();
        this.processingDateValue = dto.getProcessingDateValue();
        this.status = dto.getStatus().name();
        this.params = dto.getParams();
        this.lastExecutionDatetime = dto.getLastExecutionDatetime();
        this.process = Objects.nonNull(dto.getProcess()) ? new BusinessProcessResponse(dto.getProcess()) : null;
        this.allowsQueueing = dto.isAllowsQueueing();
    }
}
