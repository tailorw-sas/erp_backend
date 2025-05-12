package com.kynsoft.finamer.payment.infrastructure.repository.query.custom;

import com.kynsoft.finamer.payment.infrastructure.identity.Booking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManageBookingCustomRepository {

    Optional<Booking> findByIdCustom(UUID id);

    List<Booking> findAllByBookingId(List<Long> ids);

    List<Booking> findAllByCouponIn(List<String> coupons);
}
