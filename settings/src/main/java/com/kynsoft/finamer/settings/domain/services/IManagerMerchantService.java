package com.kynsoft.finamer.settings.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.settings.domain.dto.ManagerMerchantDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManagerMerchantService {
    UUID create(ManagerMerchantDto dto);

    void update(ManagerMerchantDto dto);

    void delete(ManagerMerchantDto dto);

    ManagerMerchantDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long countByCodeAndNotId(String code, UUID id);

    List<ManagerMerchantDto> findAllToReplicate();
}
