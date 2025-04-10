package com.kynsoft.finamer.settings.application.query.manageHotel.search;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.settings.domain.dto.*;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageHotelSearchResponse implements IResponse, Serializable {

    private UUID id;
    private String code;
    private String description;
    private Status status;
    private String name;
    private String babelCode;
    private ManageTradingCompaniesDto manageTradingCompanies;
    private Boolean applyByTradingCompany;
    private String prefixToInvoice;
    private Boolean isVirtual;
    private Boolean requiresFlatRate;
    private Boolean isApplyByVCC;
    private Boolean autoApplyCredit;

    public ManageHotelSearchResponse(ManageHotelDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.name = dto.getName();
        this.babelCode = dto.getBabelCode();
        this.manageTradingCompanies = dto.getManageTradingCompanies();
        this.applyByTradingCompany = dto.getApplyByTradingCompany();
        this.prefixToInvoice = dto.getPrefixToInvoice();
        this.isVirtual = dto.getIsVirtual();
        this.requiresFlatRate = dto.getRequiresFlatRate();
        this.isApplyByVCC = dto.getIsApplyByVCC();
        this.autoApplyCredit = dto.getAutoApplyCredit();
    }
}
