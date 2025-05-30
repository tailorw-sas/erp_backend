package com.kynsoft.finamer.payment.infrastructure.repository.query;

import com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionControlAmountBalance;
import com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionSimple;
import com.kynsoft.finamer.payment.infrastructure.identity.Booking;
import java.util.List;

import com.kynsoft.finamer.payment.infrastructure.repository.query.custom.ManageBookingCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ManageBookingReadDataJPARepository extends JpaRepository<Booking, UUID>, 
        JpaSpecificationExecutor<Booking>, ManageBookingCustomRepository {

    @Override
    Page<Booking> findAll(Specification specification, Pageable pageable);

    boolean existsManageBookingByBookingId(long bookingId);

    Optional<Booking> findManageBookingByBookingId(long bookingId);

    @Query("SELECT new com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionSimple(" +
            "pd.id, pd.bookingId, pd.amountBalance, pp.invoiceType, pp.hotel.id, pp.agency.client.name, pp.agency.id) " +
            "FROM Booking pd " +
            "JOIN pd.invoice pp " +
            "WHERE pd.bookingId = :id")
    Optional<BookingProjectionSimple> findSimpleDetailByGenId(@Param("id") long id);

    @Query("SELECT new com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionControlAmountBalance(" +
            "pd.id, pd.bookingId, pd.amountBalance, pd.couponNumber) " +
            "FROM Booking pd " +
            "WHERE pd.bookingId = :id")
    Optional<BookingProjectionControlAmountBalance> findSimpleBookingByGenId(@Param("id") long id);

    @Query("SELECT new com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionControlAmountBalance(" +
            "pd.id, pd.bookingId, pd.amountBalance, pd.couponNumber) " +
            "FROM Booking pd " +
            "WHERE pd.bookingId IN :ids")
    List<BookingProjectionControlAmountBalance> findBookingsControlAmountBalanceProjectionByGenId(@Param("ids") List<Long> ids);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.invoice " +
            "LEFT JOIN FETCH b.parent " +
            "WHERE b.bookingId IN :ids")
    List<Booking> findByBookingIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT new com.kynsoft.finamer.payment.domain.dto.projection.booking.BookingProjectionControlAmountBalance(" +
            "pd.id, pd.bookingId, pd.amountBalance, pd.couponNumber) " +
            "FROM Booking pd " +
            "WHERE pd.couponNumber = :couponNumber")
    Optional<BookingProjectionControlAmountBalance> findByCouponNumber(@Param("couponNumber") String couponNumber);

    Long countByCouponNumber(String couponNumber);

    @EntityGraph(attributePaths = {"invoice", "parent"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Booking> findBookingWithEntityGraphByBookingIdIn(List<Long> ids);

    @Query("SELECT DISTINCT b "+
            "FROM Booking b "+
            "LEFT JOIN FETCH b.invoice "+
            "LEFT JOIN FETCH b.parent "+
            "WHERE b.couponNumber IN :couponNumbers")
    List<Booking> findAllByCouponNumber(@Param("couponNumbers") List<String> couponNumbers);

    @Query("SELECT DISTINCT b "+
            "FROM Booking b "+
            "LEFT JOIN FETCH b.invoice "+
            "LEFT JOIN FETCH b.parent "+
            "WHERE b.id IN :ids")
    List<Booking> findAllById(@Param("ids") List<UUID> ids);
}
