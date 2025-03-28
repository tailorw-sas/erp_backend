package com.kynsoft.finamer.invoicing.domain.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ManageHotelDto implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private ManageTradingCompaniesDto manageTradingCompanies;
    private Long autogen_code;
    private boolean virtual;
    private String status;
    private boolean requiresFlatRate;
    private Boolean autoApplyCredit;
    private String address;
    private String babelCode;
    private ManagerCountryDto manageCountry;
    private ManageCityStateDto manageCityState;
    private String city;
    private ManageCurrencyDto manageCurrency;
    private Boolean applyByTradingCompany;
    private String prefixToInvoice;
}
