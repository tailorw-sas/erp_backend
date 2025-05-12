package com.kynsoft.finamer.payment.infrastructure.repository.query.custom;

import com.kynsoft.finamer.payment.infrastructure.identity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceCustomRepository {

    Page<Invoice> findAllCustom(Specification<Invoice> specification, Pageable pageable);

    Optional<Invoice> findByIdCustom(UUID id);

    Optional<Invoice> findByInvoiceIdCustom(Long invoiceId);

    List<Invoice> findAllByIdInCustom(List<UUID> ids);
}
