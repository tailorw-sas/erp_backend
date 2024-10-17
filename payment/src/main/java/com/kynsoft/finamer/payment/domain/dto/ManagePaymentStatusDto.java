package com.kynsoft.finamer.payment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManagePaymentStatusDto {
    private UUID id;
    private String code;
    private String name;
    private String status;
    private Boolean applied;
    private boolean confirmed;
    private boolean cancelled;
}
