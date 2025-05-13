package com.kynsoft.finamer.insis.domain.dto;

import com.kynsoft.finamer.insis.infrastructure.model.enums.RoomRateStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomRateDto {

    private UUID id;
    private RoomRateStatus status;
    private ManageHotelDto hotel;
    private LocalDateTime updatedAt;
    private String agency;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int stayDays;
    private String reservationCode;
    private String guestName;
    private String firstName;
    private String lastName;
    private Double amount;
    private String roomType;
    private String couponNumber;
    private int totalNumberOfGuest;
    private int adults;
    private int childrens;
    private String ratePlan;
    private LocalDate invoicingDate;
    private LocalDate hotelCreationDate;
    private Double originalAmount;
    private Double amountPaymentApplied;
    private Double rateByAdult;
    private Double rateByChild;
    private String remarks;
    private String roomNumber;
    private Double hotelInvoiceAmount;
    private String hotelInvoiceNumber;
    private String invoiceFolioNumber;
    private Double quote;
    private String renewalNumber;
    private String hash;
    private BookingDto booking;
}
