package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.infrastructure.identity.MasterPaymentAttachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface MasterPaymentAttachmentReadDataJPARepository extends JpaRepository<MasterPaymentAttachment, UUID>,
        JpaSpecificationExecutor<MasterPaymentAttachment> {

    Page<MasterPaymentAttachment> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(mpa) FROM MasterPaymentAttachment mpa WHERE mpa.resource.id = :resource AND mpa.attachmentType.defaults = true")
    Long countByResourceAndAttachmentTypeIsDefault(@Param("resource") UUID resource);

}
