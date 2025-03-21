package com.kynsoft.finamer.payment.infrastructure.repository.query.view;


import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.infrastructure.identity.Payment;
import com.kynsoft.finamer.payment.infrastructure.identity.PaymentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentViewRepository extends JpaRepository<PaymentView, UUID> {

    List<PaymentView> findByPaymentStatus(Status status);
    Page<PaymentView> findAll(Specification specification, Pageable pageable);
}