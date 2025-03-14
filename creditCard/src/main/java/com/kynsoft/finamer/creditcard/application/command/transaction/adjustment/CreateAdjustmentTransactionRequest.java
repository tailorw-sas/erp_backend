package com.kynsoft.finamer.creditcard.application.command.transaction.adjustment;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateAdjustmentTransactionRequest {

    private UUID agency;
    private UUID transactionCategory;
    private UUID transactionSubCategory;
    private Double amount;
    private String reservationNumber;
    private String referenceNumber;
    private String employee;
    private UUID employeeId;
}
