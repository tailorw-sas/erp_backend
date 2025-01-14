package com.tailorw.tcaInnsist.infrastructure.repository.query;

import com.tailorw.tcaInnsist.infrastructure.model.ManageConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManageConnectionReadDataJPARepository extends JpaRepository<ManageConnection, UUID> {
}
