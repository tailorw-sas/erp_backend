package com.kynsoft.finamer.settings.infrastructure.repository.command;

import com.kynsoft.finamer.settings.infrastructure.identity.ManageModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ManageModuleWriteDataJPARepository extends JpaRepository<ManageModule, UUID> {
}
