package com.kynsoft.finamer.creditcard.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankReconciliationStatusHistoryDto {

    private UUID id;
    private ManageBankReconciliationDto bankReconciliation;
    private String description;
    private LocalDateTime createdAt;
    private ManageEmployeeDto employee;
    private ManageReconcileTransactionStatusDto reconcileTransactionStatus;
}
