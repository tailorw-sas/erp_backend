package com.kynsoft.finamer.payment.domain.excel.bean.payment;

import com.kynsof.share.core.application.excel.CustomCellType;
import com.kynsof.share.core.application.excel.annotation.Cell;
import com.kynsof.share.core.infrastructure.util.DateUtil;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.excel.bean.Row;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBankRow extends Row implements Serializable {

    @Cell(position = -1, headerName = "")
    protected int rowNumber;
    @Cell(position = 0, headerName = "Agency")
    private String manageAgencyCode;
    @Cell(position = 1, headerName = "Hotel")
    private String manageHotelCode;
    @Cell(position = 2, cellType = CustomCellType.ALFANUMERIC, headerName = "Bank Account")
    private String bankAccount;
    @Cell(position = 3, cellType = CustomCellType.NUMERIC, headerName = "Payment Amount")
    private Double amount;
    @Cell(position = 4, cellType = CustomCellType.DATAFORMAT, headerName = "Remarks")
    private String remarks;
    @Cell(position = 5, cellType = CustomCellType.DATAFORMAT, headerName = "Transaction Date")
    private String transactionDate;

    private String manageClientCode;

    private List<UUID> agencys = new ArrayList<>();
    private List<UUID> hotels = new ArrayList<>();

    public PaymentDto toAggregate() {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentAmount(this.amount);
        paymentDto.setPaymentBalance(this.amount);
        paymentDto.setNotApplied(this.amount);
        paymentDto.setNotIdentified(this.amount);
        paymentDto.setIdentified(0.0);
        paymentDto.setDepositAmount(0.0);
        paymentDto.setDepositBalance(0.0);
        paymentDto.setOtherDeductions(0.0);
        paymentDto.setApplied(0.0);
        paymentDto.setRemark(this.remarks);
        try {
            paymentDto.setTransactionDate(DateUtil.parseDateToLocalDate(this.transactionDate, "dd/MM/yyyy"));

        } catch (Exception ex) {
            paymentDto.setTransactionDate(DateUtil.parseDateToLocalDate("20/04/2029"));
        }
        return paymentDto;
    }
}
