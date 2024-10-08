package com.kynsoft.finamer.invoicing.application.command.manageAdjustment.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAdjustmentRequest {

    private UUID id;
    private Double amount;
    private LocalDateTime date;
    private String description;
    private UUID transactionType;
    private UUID roomRate;
}
