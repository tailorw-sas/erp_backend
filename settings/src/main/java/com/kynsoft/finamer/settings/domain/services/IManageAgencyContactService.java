package com.kynsoft.finamer.settings.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageAgencyContactDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManageAgencyContactService {

    ManageAgencyContactDto create(ManageAgencyContactDto dto);

    void update(ManageAgencyContactDto dto);

    void delete(ManageAgencyContactDto dto);

    ManageAgencyContactDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    List<ManageAgencyContactDto> findAllToReplicate();
}
