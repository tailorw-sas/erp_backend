package com.kynsoft.finamer.creditcard.application.command.manageVCCTransactionType.create;

import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateManageVCCTransactionTypeRequest {

    private String code;
    private String description;
    private Status status;
    private String name;
    private Boolean isActive;
    private Boolean negative;
    private Boolean isDefault;
    private Boolean subcategory;
    private Boolean onlyApplyNet;
    private Boolean policyCredit;
    private Boolean remarkRequired;
    private Integer minNumberOfCharacter;
    private String defaultRemark;
    private boolean manual;
    private boolean refund;
}
