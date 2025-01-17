package com.kynsoft.finamer.invoicing.domain.excel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class GroupByVirtualHotel {

    private String transactionDate;
    private String agency;
    private String hotel;
    private Long hotelInvoiceNumber;
}
