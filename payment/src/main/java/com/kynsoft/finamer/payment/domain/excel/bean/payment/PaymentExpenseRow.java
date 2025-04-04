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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentExpenseRow extends Row implements Serializable {

    @Cell(position = -1, headerName = "")
    protected int rowNumber;
    @Cell(position = 0, headerName = "Agency")
    private String manageAgencyCode;
    @Cell(position = 1, headerName = "Hotel")
    private String manageHotelCode;
    @Cell(position = 2, cellType = CustomCellType.NUMERIC, headerName = "Payment Exp")
    private Double amount;
    @Cell(position = 3, cellType = CustomCellType.DATAFORMAT, headerName = "Remarks")
    private String remarks;
    @Cell(position = 4, cellType = CustomCellType.DATAFORMAT, headerName = "Transaction Date")
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
        paymentDto.setTransactionDate(DateUtil.parseDateToLocalDate(this.transactionDate, "dd/MM/yyyy"));
        return paymentDto;
    }
}
