package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ResourceTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IManageResourceTypeService {
    UUID create(ResourceTypeDto dto);

    void update(ResourceTypeDto dto);

    void delete(ResourceTypeDto dto);

    ResourceTypeDto findById(UUID id);

    ResourceTypeDto findByCode(String code);

    Long countByCodeAndNotId(String code, UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long countByDefaultsAndNotId(UUID id);

    ResourceTypeDto findByDefaults();

    Map<UUID, ResourceTypeDto> getMapById(List<UUID> ids);
}
