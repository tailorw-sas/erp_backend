package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.infrastructure.identity.ManagePaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManagePaymentStatusReadDataJpaRepository extends JpaRepository<ManagePaymentStatus, UUID> {
    Page<ManagePaymentStatus> findAll(Specification specification, Pageable pageable);
}
