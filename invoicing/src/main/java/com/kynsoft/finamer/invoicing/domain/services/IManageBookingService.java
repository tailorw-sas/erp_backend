package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IManageBookingService {

    ManageBookingDto create(ManageBookingDto dto);

    UUID insert(ManageBookingDto dto);

    void update(ManageBookingDto dto);

    void calculateInvoiceAmount(ManageBookingDto dto);

    void delete(ManageBookingDto dto);
    boolean existByBookingHotelNumber(String bookingHotelNumber);

    ManageBookingDto findById(UUID id);

    ManageBookingDto findByIdWithRates(UUID id);

    boolean existsByExactLastChars(String lastChars, UUID hotelId);

    Optional<ManageBookingDto> findManageBookingByBookingNumber(String reservationNumber);

    void calculateHotelAmount(ManageBookingDto dto);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);

    List<ManageBookingDto> findByIds(List<UUID> ids);

    List<ManageBookingDto> findBookingsWithRoomRatesByInvoiceIds(List<UUID> invoiceIds);

    List<ManageBookingDto> findAllToReplicate();

    void deleteInvoice(ManageBookingDto dto);

    boolean existsByHotelInvoiceNumber(String hotelInvoiceNumber, UUID hotelId);

    ManageBookingDto findBookingId(Long bookingId);

    void updateAll(List<ManageBookingDto> bookingList);
}
