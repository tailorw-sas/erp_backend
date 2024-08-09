package com.kynsoft.finamer.invoicing.domain.excel.bean;

import com.kynsof.share.core.application.excel.CustomCellType;
import com.kynsof.share.core.application.excel.annotation.Cell;
import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EImportType;
import com.kynsoft.finamer.invoicing.domain.excel.util.DateUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

@Data
public class BookingRow {
    private String importProcessId;
    @Cell(position = -1)
    private int rowNumber;
    @Cell(position = 0, cellType = CustomCellType.DATAFORMAT)
    private String transactionDate;
    @Cell(position = 1)
    private String manageHotelCode;
    @Cell(position = 2)
    private String manageAgencyCode;
    @Cell(position = 3)
    private String firstName;
    @Cell(position = 4)
    private String lastName;
    @Cell(position = 5, cellType = CustomCellType.DATAFORMAT)
    private String checkIn;
    @Cell(position = 6, cellType = CustomCellType.DATAFORMAT)
    private String checkOut;
    @Cell(position = 7, cellType = CustomCellType.NUMERIC)
    private Double nights;
    @Cell(position = 8, cellType = CustomCellType.NUMERIC)
    private Double adults;
    @Cell(position = 9, cellType = CustomCellType.NUMERIC)
    private Double children;
    @Cell(position = 10, cellType = CustomCellType.NUMERIC)
    private Double invoiceAmount;
    @Cell(position = 11, cellType = CustomCellType.DATAFORMAT)
    private String coupon;
    @Cell(position = 12, cellType = CustomCellType.DATAFORMAT)
    private String hotelBookingNumber;
    @Cell(position = 13)
    private String roomType;
    @Cell(position = 14)
    private String ratePlan;
    @Cell(position = 15, cellType = CustomCellType.DATAFORMAT)
    private String hotelInvoiceNumber;
    @Cell(position = 16)
    private String remarks;
    @Cell(position = 17, cellType = CustomCellType.FORMULA)
    private Double amountPAX;
    @Cell(position = 18, cellType = CustomCellType.NUMERIC)
    private Double roomNumber;
    @Cell(position = 19, cellType = CustomCellType.NUMERIC)
    private Double hotelInvoiceAmount;
    @Cell(position = 20, cellType = CustomCellType.DATAFORMAT)
    private String bookingDate;
    @Cell(position = 21)
    private String hotelType;


    private String trendingCompany;
    public BookingRow() {
        this.firstName="";
        this.lastName="";
    }

    public ManageBookingDto toAggregate() {
        ManageBookingDto manageBookingDto = new ManageBookingDto();
        manageBookingDto.setAdults(Objects.nonNull(this.adults) ? this.adults.intValue() : 0);
        manageBookingDto.setChildren(Objects.nonNull(this.children) ? this.children.intValue() : 0);
        manageBookingDto.setCheckIn(DateUtil.parseDateToDateTime(this.checkIn));
        manageBookingDto.setCheckOut(DateUtil.parseDateToDateTime(this.checkOut));
        manageBookingDto.setCouponNumber(Objects.nonNull(this.coupon) ? this.coupon : "");
        manageBookingDto.setHotelAmount(this.hotelInvoiceAmount);
        manageBookingDto.setFirstName(Objects.nonNull(this.firstName) ? this.firstName : "");
        manageBookingDto.setLastName(Objects.nonNull(this.lastName) ? this.lastName : "");
        manageBookingDto.setFullName(Objects.nonNull(this.firstName) ? this.firstName : " " + " " + (Objects.nonNull(this.lastName) ? this.lastName : ""));
        manageBookingDto.setHotelBookingNumber(Objects.nonNull(this.hotelBookingNumber) ? this.hotelBookingNumber : "");
        manageBookingDto.setHotelInvoiceNumber(Objects.nonNull(this.hotelInvoiceNumber) ? this.hotelInvoiceNumber : "");
        manageBookingDto.setDescription(Objects.nonNull(this.remarks) ? this.remarks : "");
        manageBookingDto.setRoomNumber(String.valueOf(Objects.nonNull(this.roomNumber) ? this.roomNumber : 0));
        manageBookingDto.setInvoiceAmount(this.invoiceAmount);
        manageBookingDto.setDueAmount(this.invoiceAmount);
        // manageBookingDto.setAmountPax();
        manageBookingDto.setBookingDate(DateUtil.parseDateToDateTime(this.bookingDate));
        return manageBookingDto;
    }

}
