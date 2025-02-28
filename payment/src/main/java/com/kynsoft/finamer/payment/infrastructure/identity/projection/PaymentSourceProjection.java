package com.kynsoft.finamer.payment.infrastructure.identity.projection;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PaymentSourceProjection {

    private UUID id;
    private String code;
    private String name;
    private String status;
    private Boolean expense;
    private Boolean isBank;

    public PaymentSourceProjection(UUID id, String code, String name, String status, Boolean expense, Boolean isBank) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
        this.expense = expense;
        this.isBank = isBank;
    }
}
