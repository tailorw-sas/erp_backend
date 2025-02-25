package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageRatePlanDto;
import java.util.List;

import java.util.UUID;

import com.kynsoft.finamer.invoicing.domain.dto.ManageRoomTypeDto;
import org.springframework.data.domain.Pageable;

public interface IManageRatePlanService {

    UUID create(ManageRatePlanDto dto);

    void update(ManageRatePlanDto dto);

    void delete(ManageRatePlanDto dto);

    ManageRatePlanDto findById(UUID id);

    ManageRatePlanDto findByCode(String code);

    List<ManageRatePlanDto> findByCodes(List<String> codes);

    boolean existByCode(String code);

    Long countByCodeAndNotId(String code, UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    ManageRatePlanDto findManageRatePlanByCodeAndHotelCode(String code, String hotelCode);
}
