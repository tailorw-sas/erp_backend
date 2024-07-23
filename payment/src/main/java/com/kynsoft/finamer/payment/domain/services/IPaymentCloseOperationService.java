package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.PaymentCloseOperationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaymentCloseOperationService {
    UUID create(PaymentCloseOperationDto dto);

    void update(PaymentCloseOperationDto dto);

    void updateAll(List<PaymentCloseOperationDto> dtos);

    void delete(PaymentCloseOperationDto dto);

    List<PaymentCloseOperationDto> findByHotelIds(List<UUID> hotelIds);

    PaymentCloseOperationDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long findByHotelId(UUID hotelId);

    PaymentCloseOperationDto findByHotelIds(UUID hotel);
}
