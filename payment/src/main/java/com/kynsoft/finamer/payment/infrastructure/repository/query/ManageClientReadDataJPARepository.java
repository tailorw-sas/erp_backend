package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.infrastructure.identity.ManageClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ManageClientReadDataJPARepository extends JpaRepository<ManageClient, UUID>,
        JpaSpecificationExecutor<ManageClient> {

    Page<ManageClient> findAll(Specification specification, Pageable pageable);

}
