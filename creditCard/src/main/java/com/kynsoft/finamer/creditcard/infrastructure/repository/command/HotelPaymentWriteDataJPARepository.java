package com.kynsoft.finamer.creditcard.infrastructure.repository.command;

import com.kynsoft.finamer.creditcard.infrastructure.identity.HotelPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelPaymentWriteDataJPARepository extends JpaRepository<HotelPayment, UUID> {
}
