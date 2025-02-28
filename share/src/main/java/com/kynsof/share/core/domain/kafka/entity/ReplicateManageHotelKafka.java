package com.kynsof.share.core.domain.kafka.entity;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReplicateManageHotelKafka implements Serializable {
    private UUID id;
    private String code;
    private String name;
    private Boolean isApplyByVCC;
    private UUID manageTradingCompany;
    private String status;
    private boolean requiresFlatRate;
    private Boolean isVirtual;
    private Boolean applyByTradingCompany;
    private Boolean autoApplyCredit;

    private String city;
    private String babelCode;
    private String address;
    private UUID manageCityState;
    private UUID manageCountry;
    private UUID manageCurrency;
    private String prefixToInvoice;
}
