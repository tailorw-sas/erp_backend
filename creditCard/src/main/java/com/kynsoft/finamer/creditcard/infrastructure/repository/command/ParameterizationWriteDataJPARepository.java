package com.kynsoft.finamer.creditcard.infrastructure.repository.command;

import com.kynsoft.finamer.creditcard.infrastructure.identity.Parameterization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParameterizationWriteDataJPARepository extends JpaRepository<Parameterization, UUID> {
}
