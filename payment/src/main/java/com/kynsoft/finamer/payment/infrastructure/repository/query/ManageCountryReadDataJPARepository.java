package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.infrastructure.identity.ManageCountry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ManageCountryReadDataJPARepository extends JpaRepository<ManageCountry, UUID> {
    Page<ManageCountry> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManageCountry b WHERE b.code = :code AND b.id <> :id")
    Long countByCodeAndNotId(@Param("code") String code, @Param("id") UUID id);

    @Query("SELECT COUNT(b) FROM ManageCountry b WHERE b.name = :name AND b.id <> :id")
    Long countByNameAndNotId(@Param("name") String name, @Param("id") UUID id);
}
