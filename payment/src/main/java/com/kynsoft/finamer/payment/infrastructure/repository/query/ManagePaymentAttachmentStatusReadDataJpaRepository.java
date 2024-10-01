package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.infrastructure.identity.ManagePaymentAttachmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ManagePaymentAttachmentStatusReadDataJpaRepository extends JpaRepository<ManagePaymentAttachmentStatus, UUID> {
    Page<ManagePaymentAttachmentStatus> findAll(Specification specification, Pageable pageable);

    @Query("SELECT b FROM ManagePaymentAttachmentStatus b WHERE b.defaults = true")
    Optional<ManagePaymentAttachmentStatus> findByDefault();

    Optional<ManagePaymentAttachmentStatus> findManagePaymentAttachmentStatusByCodeAndStatus(String code,String status);
}
