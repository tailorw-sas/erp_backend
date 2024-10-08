package com.kynsof.share.core.domain.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateManageTradingCompanyKafka {

    private UUID id;
    private String code;
    private boolean isApplyInvoice;
    private String cif;
    private String address;
    private String company;

}
