package com.kynsoft.finamer.payment.infrastructure.repository.query.custom;

import com.kynsoft.finamer.payment.infrastructure.identity.PaymentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentDetailCustomRepository {

    Optional<PaymentDetail> findByIdCustom(UUID id);

    Optional<PaymentDetail> findByPaymentDetailIdCustom(int id);

    List<PaymentDetail> findAllByPaymentIdCustom(UUID id);

    List<PaymentDetail> findChildrenWithDetailsByParentId(UUID parentId);

    List<PaymentDetail> findChildrensByParentId(Long parentId);

    List<PaymentDetail> findAllByPaymentGenIdIn(List<Long> genIds);

    Page<PaymentDetail> findAllCustom(Specification<PaymentDetail> specification, Pageable pageable);

    List<PaymentDetail> findAllByPaymentGenIdInCustom(List<Long> paymentGenIds);
}
