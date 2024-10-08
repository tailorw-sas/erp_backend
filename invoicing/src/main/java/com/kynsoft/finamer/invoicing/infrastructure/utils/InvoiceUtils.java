package com.kynsoft.finamer.invoicing.infrastructure.utils;

import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;

import java.util.Objects;

public class InvoiceUtils {

    public static double calculateInvoiceAmount(ManageInvoiceDto manageInvoice){
        return manageInvoice.getBookings().stream().filter(manageBookingDto -> Objects.nonNull(manageBookingDto.getInvoiceAmount()))
                .mapToDouble(ManageBookingDto::getInvoiceAmount).sum();
    }
    public static String getInvoiceNumberPrefix(String invoiceNumber) {
        if (invoiceNumber != null) {
            int firstIndex = invoiceNumber.indexOf("-");
            int lastIndex = invoiceNumber.lastIndexOf("-");

            if (firstIndex != -1 && lastIndex != -1 && firstIndex != lastIndex) {
                String beginInvoiceNumber = invoiceNumber.substring(0, firstIndex);
                String lastInvoiceNumber = invoiceNumber.substring(lastIndex + 1);

                return beginInvoiceNumber + "-" + lastInvoiceNumber;

            } else {
                return invoiceNumber;
            }

        }
        return "";
    }

    public static String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }
}
