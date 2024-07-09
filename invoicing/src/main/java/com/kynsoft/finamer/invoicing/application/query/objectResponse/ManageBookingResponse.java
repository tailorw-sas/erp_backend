package com.kynsoft.finamer.invoicing.application.query.objectResponse;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsoft.finamer.invoicing.domain.dto.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageBookingResponse implements IResponse {
    private UUID id;
    private Long booking_id;
    private Long reservationNumber;
    private LocalDateTime hotelCreationDate;
    private LocalDateTime bookingDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String hotelBookingNumber;
    private String fullName;
    private Double invoiceAmount;
    private String roomNumber;
    private String couponNumber;
    private Integer adults;
    private Integer children;
    private Double rateAdult;
    private Double rateChild;
    private String hotelInvoiceNumber;
    private String folioNumber;
    private Double hotelAmount;
    private String description;
    private ManageInvoiceDto invoice;
    private ManageRatePlanDto ratePlan;
    private ManageNightTypeDto nightType;
    private ManageRoomTypeDto roomType;
    private ManageRoomCategoryDto roomCategory;

    public ManageBookingResponse(ManageBookingDto dto) {
        this.id = dto.getId();
        this.booking_id = dto.getBooking_id();
        this.hotelCreationDate = dto.getHotelCreationDate();
        this.bookingDate = dto.getBookingDate();
        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.hotelBookingNumber = dto.getHotelBookingNumber();
        this.fullName = dto.getFullName();
        this.invoiceAmount = dto.getInvoiceAmount();
        this.roomNumber = dto.getRoomNumber();
        this.couponNumber = dto.getCouponNumber();
        this.adults = dto.getAdults();
        this.children = dto.getChildren();
        this.rateAdult = dto.getRateAdult();
        this.rateChild = dto.getRateChild();
        this.hotelInvoiceNumber = dto.getHotelInvoiceNumber();
        this.folioNumber = dto.getFolioNumber();
        this.hotelAmount = dto.getHotelAmount();
        this.description = dto.getDescription();
        this.invoice = dto.getInvoice();
        this.ratePlan = dto.getRatePlan();
        this.nightType = dto.getNightType();
        this.roomType = dto.getRoomType();
        this.roomCategory = dto.getRoomCategory();
        this.reservationNumber = dto.getReservationNumber();
    }
}
