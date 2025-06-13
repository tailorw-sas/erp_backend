package com.kynsoft.finamer.invoicing.infrastructure.repository.command;

import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageAdjustment;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.custom.ManageAdjustmentWriteCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManageAdjustmentWriteDataJpaRepository extends JpaRepository<ManageAdjustment, UUID>, ManageAdjustmentWriteCustomRepository {
}
