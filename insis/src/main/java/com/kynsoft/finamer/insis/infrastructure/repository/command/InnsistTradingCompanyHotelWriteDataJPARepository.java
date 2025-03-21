package com.kynsoft.finamer.insis.infrastructure.repository.command;

import com.kynsoft.finamer.insis.infrastructure.model.InnsistHotelRoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InnsistTradingCompanyHotelWriteDataJPARepository extends JpaRepository<InnsistHotelRoomType, UUID> {
}
