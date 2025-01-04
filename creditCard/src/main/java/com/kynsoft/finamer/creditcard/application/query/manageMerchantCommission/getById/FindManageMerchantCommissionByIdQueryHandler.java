package com.kynsoft.finamer.creditcard.application.query.manageMerchantCommission.getById;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.ManageMerchantCommissionResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageMerchantCommissionDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageMerchantCommissionService;
import org.springframework.stereotype.Component;

@Component
public class FindManageMerchantCommissionByIdQueryHandler implements IQueryHandler<FindManageMerchantCommissionByIdQuery, ManageMerchantCommissionResponse> {

    private final IManageMerchantCommissionService service;

    public FindManageMerchantCommissionByIdQueryHandler(IManageMerchantCommissionService service) {
        this.service = service;
    }

    @Override
    public ManageMerchantCommissionResponse handle(FindManageMerchantCommissionByIdQuery query) {
        ManageMerchantCommissionDto dto = service.findById(query.getId());

        return new ManageMerchantCommissionResponse(dto);
    }
}
