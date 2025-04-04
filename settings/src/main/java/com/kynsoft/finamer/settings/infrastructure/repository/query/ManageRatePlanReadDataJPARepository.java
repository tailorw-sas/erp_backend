package com.kynsoft.finamer.settings.infrastructure.repository.query;

import com.kynsoft.finamer.settings.infrastructure.identity.ManageRatePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManageRatePlanReadDataJPARepository extends JpaRepository<ManageRatePlan, UUID>,
        JpaSpecificationExecutor<ManageRatePlan> {

    Page<ManageRatePlan> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManageRatePlan b WHERE b.code = :code AND b.hotel.id = :hotelId AND b.id <> :id")
    Long countByCodeAndNotId(@Param("code") String code, @Param("id") UUID id, @Param("hotelId") UUID hotelId);
}
