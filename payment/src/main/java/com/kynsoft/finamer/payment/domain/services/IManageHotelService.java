package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IManageHotelService {

    UUID create(ManageHotelDto dto);

    void update(ManageHotelDto dto);

    void delete(ManageHotelDto dto);

    ManageHotelDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    boolean existByCode(String hotelCode);

    ManageHotelDto findByCode(String hotelCode);

    List<ManageHotelDto> findByIds(List<UUID> ids);

    Map<UUID, ManageHotelDto> getMapById(List<UUID> ids);
}
