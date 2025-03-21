package com.kynsoft.finamer.payment.domain.excel.bean.detail;

import com.kynsof.share.core.application.excel.CustomCellType;
import com.kynsof.share.core.application.excel.annotation.Cell;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.excel.bean.Row;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailRow extends Row implements Serializable {

    @Cell(position = -1, headerName = "")
    protected int rowNumber;
    @Cell(position = 0, cellType = CustomCellType.DATAFORMAT, headerName = "Payment Id")
    private String paymentId;
    @Cell(position = 1, cellType = CustomCellType.DATAFORMAT, headerName = "Coupon")
    private String coupon;
    @Cell(position = 2, cellType = CustomCellType.DATAFORMAT, headerName = "Invoice No")
    private String invoiceNo;
    @Cell(position = 3, cellType = CustomCellType.NUMERIC, headerName = "Balance")
    private Double balance;
    @Cell(position = 4, headerName = "Trans. Category")
    private String transactionType;
    @Cell(position = 5, cellType = CustomCellType.NUMERIC, headerName = "ANTI")
    private Double anti;
    @Cell(position = 6, cellType = CustomCellType.DATAFORMAT, headerName = "Remark")
    private String remarks;
    @Cell(position = 7, cellType = CustomCellType.DATAFORMAT, headerName = "First Name")
    private String firstName;
    @Cell(position = 8, cellType = CustomCellType.DATAFORMAT, headerName = "Last Name")
    private String lastName;
    @Cell(position = 9, cellType = CustomCellType.DATAFORMAT, headerName = "Booking No")
    private String bookingNo;
    @Cell(position = 10, cellType = CustomCellType.DATAFORMAT, headerName = "Book Id")
    private String bookId;
    @Cell(position = 11, cellType = CustomCellType.DATAFORMAT, headerName = "Imp Status")
    private String impStatus;

    private UUID externalPaymentId;
    private List<UUID> agencys = new ArrayList<>();
    private List<UUID> hotels = new ArrayList<>();

    public PaymentDetailDto toAggregate() {
        PaymentDetailDto paymentDetailDto = new PaymentDetailDto();
        paymentDetailDto.setAmount(this.balance);
        paymentDetailDto.setRemark(this.remarks);
        paymentDetailDto.setTransactionDate(OffsetDateTime.now(ZoneId.of("UTC")));
        paymentDetailDto.setCouponNo(coupon);
        return paymentDetailDto;
    }
}
