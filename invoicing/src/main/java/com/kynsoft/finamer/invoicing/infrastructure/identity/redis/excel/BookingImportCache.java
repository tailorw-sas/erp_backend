package com.kynsoft.finamer.invoicing.infrastructure.identity.redis.excel;

import com.kynsoft.finamer.invoicing.domain.excel.bean.BookingRow;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

@Data
@RedisHash(value = "importcache", timeToLive = 18000L)
public class BookingImportCache {

    @Id
    private String id;
    @Indexed
    private String importProcessId;
    @Indexed
    private String generationType;

    @Indexed
    private String transactionDate;

    private String manageHotelCode;

    private String manageAgencyCode;

    private String firstName;

    private String lastName;

    private String checkIn;

    private String checkOut;

    private double nights;

    private double adults;

    private double children;

    private double invoiceAmount;

    private String coupon;
    @Indexed
    private String hotelBookingNumber;

    private String roomType;

    private String ratePlan;

    @Indexed
    private String hotelInvoiceNumber;

    private String remarks;

    private double amountPAX;

    private String roomNumber;

    private double hotelInvoiceAmount;
    @Indexed
    private String bookingDate;

    private String hotelType;

    private String nightType;

    @Indexed
    private int rowNumber;
    
    @Indexed
    private String insistImportProcessId;

    public BookingImportCache() {
    }

    public BookingImportCache(BookingRow bookingRow) {
        this.rowNumber = bookingRow.getRowNumber();
        this.transactionDate = bookingRow.getTransactionDate();
        this.manageHotelCode = bookingRow.getManageHotelCode();
        this.manageAgencyCode = bookingRow.getManageAgencyCode();
        this.firstName = bookingRow.getFirstName();
        this.lastName = bookingRow.getLastName();
        this.checkIn = bookingRow.getCheckIn();
        this.checkOut = bookingRow.getCheckOut();
        this.nights = Objects.nonNull(bookingRow.getNights()) ? bookingRow.getNights() : 0;
        this.adults = Objects.nonNull(bookingRow.getAdults()) ? bookingRow.getAdults() : 0;
        this.children = Objects.nonNull(bookingRow.getChildren()) ? bookingRow.getChildren() : 0;
        this.invoiceAmount = Objects.nonNull(bookingRow.getInvoiceAmount()) ? bookingRow.getInvoiceAmount() : 0;
        this.coupon = bookingRow.getCoupon();
        this.hotelBookingNumber = bookingRow.getHotelBookingNumber();
        this.roomType = bookingRow.getRoomType();
        this.ratePlan = bookingRow.getRatePlan();
        this.hotelInvoiceNumber = bookingRow.getHotelInvoiceNumber();
        this.remarks = bookingRow.getRemarks();
        this.amountPAX = bookingRow.getAmountPAX();
        this.roomNumber = bookingRow.getRoomNumber();
        this.hotelInvoiceAmount = Objects.nonNull(bookingRow.getHotelInvoiceAmount()) ? bookingRow.getHotelInvoiceAmount() : 0;
        this.bookingDate = bookingRow.getBookingDate();
        this.hotelType = bookingRow.getHotelType();
        this.nightType = bookingRow.getNightType();
    }

    public BookingRow toAggregate() {
        BookingRow bookingRow = new BookingRow();
        bookingRow.setRowNumber(this.rowNumber);
        bookingRow.setTransactionDate(this.transactionDate);
        bookingRow.setManageHotelCode(this.manageHotelCode);
        bookingRow.setManageAgencyCode(this.manageAgencyCode);
        bookingRow.setFirstName(this.firstName);
        bookingRow.setLastName(this.lastName);
        bookingRow.setCheckIn(this.checkIn);
        bookingRow.setCheckOut(this.checkOut);
        bookingRow.setNights(this.nights);
        bookingRow.setAdults(this.adults);
        bookingRow.setChildren(this.children);
        bookingRow.setInvoiceAmount(this.invoiceAmount);
        bookingRow.setCoupon(this.coupon);
        bookingRow.setHotelBookingNumber(this.hotelBookingNumber);
        bookingRow.setRoomType(this.roomType);
        bookingRow.setRatePlan(this.ratePlan);
        bookingRow.setHotelInvoiceNumber(this.hotelInvoiceNumber);
        bookingRow.setRemarks(this.remarks);
        bookingRow.setAmountPAX(this.amountPAX);
        bookingRow.setRoomNumber(this.roomNumber);
        bookingRow.setHotelInvoiceAmount(this.hotelInvoiceAmount);
        bookingRow.setBookingDate(this.bookingDate);
        bookingRow.setHotelType(this.hotelType);
        bookingRow.setNightType(this.nightType);
        return bookingRow;
    }

    public BookingRow toAggregateImportInsist() {
        BookingRow bookingRow = new BookingRow();
        bookingRow.setTransactionDate(this.transactionDate);
        bookingRow.setManageHotelCode(this.manageHotelCode);
        bookingRow.setManageAgencyCode(this.manageAgencyCode);
        bookingRow.setFirstName(this.firstName);
        bookingRow.setLastName(this.lastName);
        bookingRow.setCheckIn(this.checkIn);
        bookingRow.setCheckOut(this.checkOut);
        bookingRow.setNights(this.nights);
        bookingRow.setAdults(this.adults);
        bookingRow.setChildren(this.children);
        bookingRow.setInvoiceAmount(this.invoiceAmount);
        bookingRow.setCoupon(this.coupon);
        bookingRow.setHotelBookingNumber(this.hotelBookingNumber);
        bookingRow.setRoomType(this.roomType);
        bookingRow.setRatePlan(this.ratePlan);
        bookingRow.setHotelInvoiceNumber(this.hotelInvoiceNumber);
        bookingRow.setRemarks(this.remarks);
        bookingRow.setAmountPAX(this.amountPAX);
        bookingRow.setRoomNumber(this.roomNumber);
        bookingRow.setHotelInvoiceAmount(this.hotelInvoiceAmount);
        bookingRow.setBookingDate(this.bookingDate);
        bookingRow.setHotelType(this.hotelType);
        bookingRow.setNightType(this.nightType);
        bookingRow.setInsistImportProcessId(insistImportProcessId);
        bookingRow.setImportProcessId(importProcessId);
        return bookingRow;
    }
}
