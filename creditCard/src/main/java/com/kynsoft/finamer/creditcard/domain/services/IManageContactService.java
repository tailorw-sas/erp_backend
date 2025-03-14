package com.kynsoft.finamer.creditcard.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageContactDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManageContactService {

    UUID create(ManageContactDto dto);

    void update(ManageContactDto dto);

    void delete(ManageContactDto dto);

    ManageContactDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    List<ManageContactDto> findByHotelId(UUID hotelId);
}
