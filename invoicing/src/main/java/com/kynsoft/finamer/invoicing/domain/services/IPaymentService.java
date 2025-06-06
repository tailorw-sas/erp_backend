package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.domain.dto.PaymentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {
    PaymentDto create(PaymentDto dto);

    void createAll(List<PaymentDto> payments);

    void update(PaymentDto dto);

    void delete(PaymentDto dto);

    PaymentDto findById(UUID id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

}
