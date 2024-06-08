package com.kynsoft.finamer.settings.infrastructure.repository.query;

import com.kynsoft.finamer.settings.infrastructure.identity.ManageEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManageEmployeeReadDataJPARepository extends JpaRepository<ManageEmployee, UUID>,
        JpaSpecificationExecutor<ManageEmployee> {

    Page<ManageEmployee> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManageEmployee b WHERE b.loginName = :loginName AND b.id <> :id AND b.deleted = false")
    Long countByLoginNameAndNotId(@Param("loginName") String loginName, @Param("id") UUID id);

    @Query("SELECT COUNT(b) FROM ManageEmployee b WHERE b.email = :email AND b.id <> :id AND b.deleted = false")
    Long countByEmailAndNotId(@Param("email") String email, @Param("id") UUID id);

}
