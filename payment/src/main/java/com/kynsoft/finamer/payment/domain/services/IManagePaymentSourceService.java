package com.kynsoft.finamer.payment.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentSourceDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IManagePaymentSourceService {
    UUID create(ManagePaymentSourceDto dto);

    void update(ManagePaymentSourceDto dto);

    void delete(ManagePaymentSourceDto dto);

    ManagePaymentSourceDto findById(UUID id);

    ManagePaymentSourceDto findByCodeActive(String code);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    ManagePaymentSourceDto findByExpense();

}
