package com.kynsof.share.core.application.excel.validator;

import com.kynsof.share.core.domain.response.ErrorField;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

public abstract class ExcelRuleValidator<T> {

    protected final ApplicationEventPublisher applicationEventPublisher;

    protected ExcelRuleValidator(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public abstract boolean validate(T obj, List<ErrorField> errorFieldList);

    public boolean validate(T obj, List<ErrorField> errorFieldList, ICache cache){
        return validate(obj, errorFieldList);
    }

    protected void sendErrorEvent( Object rowError){
        applicationEventPublisher.publishEvent(rowError);
    }
}

