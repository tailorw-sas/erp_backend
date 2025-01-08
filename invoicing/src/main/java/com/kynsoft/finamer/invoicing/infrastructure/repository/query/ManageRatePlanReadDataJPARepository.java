package com.kynsoft.finamer.invoicing.infrastructure.repository.query;

import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageRatePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ManageRatePlanReadDataJPARepository extends JpaRepository<ManageRatePlan, UUID>,
        JpaSpecificationExecutor<ManageRatePlan> {

    Page<ManageRatePlan> findAll(Specification specification, Pageable pageable);

    Optional<ManageRatePlan> findManageRatePlanByCode(String code);

    @Query("SELECT b FROM ManageRatePlan b WHERE b.code = :code AND b.hotel.code = :hotelCode")
    Optional<ManageRatePlan> findManageRatePlanByCodeAndHotelCode(@Param("code") String code, @Param("hotelCode") String hotelCode);

    @Query("SELECT COUNT(b) FROM ManageRatePlan b WHERE b.code = :code AND b.id <> :id")
    Long countByCodeAndNotId(@Param("code") String code, @Param("id") UUID id);

    boolean existsByCode(String code);

}
