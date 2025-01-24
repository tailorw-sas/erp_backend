package com.kynsoft.finamer.invoicing.infrastructure.repository.query;

import com.kynsoft.finamer.invoicing.infrastructure.identity.Booking;
import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageRoomRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;


@Repository
public interface ManageRoomRateReadDataJPARepository extends JpaRepository<ManageRoomRate, UUID>,
        JpaSpecificationExecutor<ManageRoomRate> {

    Page<ManageRoomRate> findAll(Specification specification, Pageable pageable);

   List<ManageRoomRate> findByBooking(Booking booking);
}
