package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.PaymentCloseOperationDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPaymentCloseOperationService {
    UUID create(PaymentCloseOperationDto dto);

    void update(PaymentCloseOperationDto dto);

    void updateAll(List<PaymentCloseOperationDto> dtos);

    void delete(PaymentCloseOperationDto dto);

    List<PaymentCloseOperationDto> findByHotelId(List<UUID> hotelIds);

    PaymentCloseOperationDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    Long countByHotelId(UUID hotelId);

    PaymentCloseOperationDto findByHotelId(UUID hotel);

    PaymentCloseOperationDto findByHotelIdsCacheable(UUID hotel);

    void clearCache();

    Map<UUID, PaymentCloseOperationDto> getMapByHotelId(List<UUID> hotelIds);

    OffsetDateTime getTransactionDate(PaymentCloseOperationDto closeOperationDto);
}
