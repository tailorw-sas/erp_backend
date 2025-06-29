package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageAgencyDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IManageAgencyService {

    UUID create(ManageAgencyDto dto);

    void update(ManageAgencyDto dto);

    void delete(ManageAgencyDto dto);

    ManageAgencyDto findById(UUID id);

    List<ManageAgencyDto> findByIds(List<UUID> ids);

    boolean existByCode(String agencyCode);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    ManageAgencyDto findByCode(String agencyCode);

    Map<UUID, ManageAgencyDto> getMapById(List<UUID> ids);
}
