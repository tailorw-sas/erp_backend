package com.kynsoft.finamer.creditcard.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageB2BPartnerTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManageB2BPartnerTypeService {
    UUID create(ManageB2BPartnerTypeDto dto);

    void update(ManageB2BPartnerTypeDto dto);

    void delete(ManageB2BPartnerTypeDto dto);

    ManageB2BPartnerTypeDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long countByCodeAndNotId(String code, UUID id);
}