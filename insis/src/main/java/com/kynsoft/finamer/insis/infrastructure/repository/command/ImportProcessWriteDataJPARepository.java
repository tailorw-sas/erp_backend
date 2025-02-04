package com.kynsoft.finamer.insis.infrastructure.repository.command;

import com.kynsoft.finamer.insis.infrastructure.model.ImportProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImportProcessWriteDataJPARepository extends JpaRepository<ImportProcess, UUID> {
}
