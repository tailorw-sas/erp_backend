package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionControlAmountBalance;
import com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionSimple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManageBookingService {

    void create(ManageBookingDto dto);

    void update(ManageBookingDto dto);

    ManageBookingDto findById(UUID id);

    ManageBookingDto findByGenId(long id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    boolean exitBookingByGenId(long id);

    void deleteAll();

    BookingProjectionSimple findSimpleDetailByGenId(long id);

    List<ManageBookingDto> findByBookingIdIn(List<Long> ids);

    BookingProjectionControlAmountBalance findSimpleBookingByGenId(long id);
}
