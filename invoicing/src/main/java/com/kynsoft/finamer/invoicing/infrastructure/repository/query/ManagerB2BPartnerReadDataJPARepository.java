package com.kynsoft.finamer.invoicing.infrastructure.repository.query;

import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageB2BPartner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ManagerB2BPartnerReadDataJPARepository extends JpaRepository<ManageB2BPartner, UUID>,
        JpaSpecificationExecutor<ManageB2BPartner> {

    Page<ManageB2BPartner> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManageB2BPartner b WHERE b.code = :code AND b.id <> :id")
    Long countByCodeAndNotId(@Param("code") String code, @Param("id") UUID id);

}