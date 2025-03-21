package com.kynsoft.finamer.creditcard.infrastructure.repository.query;

import com.kynsoft.finamer.creditcard.infrastructure.identity.ManageMerchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ManageMerchantReadDataJPARepository extends JpaRepository<ManageMerchant, UUID>,
        JpaSpecificationExecutor<ManageMerchant> {

    Page<ManageMerchant> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManageMerchant b WHERE b.code = :code AND b.id <> :id")
    Long countByCodeAndNotId(@Param("code") String code, @Param("id") UUID id);

    @Query("SELECT COUNT(b) FROM ManageMerchant b WHERE b.defaultm = true AND b.id <> :id")
    Long countByDefaultAndNotId(@Param("id") UUID id);
}
