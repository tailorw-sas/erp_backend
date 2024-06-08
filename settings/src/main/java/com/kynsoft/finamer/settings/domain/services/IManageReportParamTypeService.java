package com.kynsoft.finamer.settings.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageReportParamTypeDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManageReportParamTypeService {

    UUID create(ManageReportParamTypeDto dto);

    void update(ManageReportParamTypeDto dto);

    void delete(ManageReportParamTypeDto dto);

    ManageReportParamTypeDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long countByNameAndNotId(String name, UUID id);
}
