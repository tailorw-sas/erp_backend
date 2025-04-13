package com.kynsoft.finamer.payment.infrastructure.repository.query.payments;

import com.kynsoft.finamer.payment.infrastructure.identity.PaymentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public interface PaymentDetailCustomRepository {

    List<PaymentDetail> findAllByPaymentId(UUID id);

    Page<PaymentDetail> findAllCustom(Specification<PaymentDetail> specification, Pageable pageable);
}
