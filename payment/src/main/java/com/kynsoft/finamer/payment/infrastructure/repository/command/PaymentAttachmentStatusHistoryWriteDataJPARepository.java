package com.kynsoft.finamer.payment.infrastructure.repository.command;

import com.kynsoft.finamer.payment.infrastructure.identity.PaymentAttachmentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentAttachmentStatusHistoryWriteDataJPARepository extends JpaRepository<PaymentAttachmentStatusHistory, UUID> {
}
