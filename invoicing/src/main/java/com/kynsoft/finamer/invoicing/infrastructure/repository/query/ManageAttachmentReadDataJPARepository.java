package com.kynsoft.finamer.invoicing.infrastructure.repository.query;

import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageAttachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManageAttachmentReadDataJPARepository extends JpaRepository<ManageAttachment, UUID>,
        JpaSpecificationExecutor<ManageAttachment> {

    Page<ManageAttachment> findAll(Specification specification, Pageable pageable);

    @Query("SELECT b FROM ManageAttachment b WHERE b.invoice.id = :invoice")
    List<ManageAttachment> findAllByInvoiceId(@Param("invoice") UUID invoiceId);

}
