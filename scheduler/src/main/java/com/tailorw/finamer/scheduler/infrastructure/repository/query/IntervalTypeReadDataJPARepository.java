package com.tailorw.finamer.scheduler.infrastructure.repository.query;

import com.tailorw.finamer.scheduler.infrastructure.model.IntervalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IntervalTypeReadDataJPARepository extends JpaRepository<IntervalType, UUID>, JpaSpecificationExecutor<IntervalType> {

    Optional<IntervalType> findByCodeAndDeletedAtIsNull(String code);

    @Query("select s FROM IntervalType s WHERE s.code = :code and s.status IN ('ACTIVE', 'INACTIVE')")
    Optional<IntervalType> findByCode(String code);

    Page<IntervalType> findAll(Specification specification, Pageable pageable);
}
