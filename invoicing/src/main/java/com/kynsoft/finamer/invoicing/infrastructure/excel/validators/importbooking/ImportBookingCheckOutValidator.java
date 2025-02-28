package com.kynsoft.finamer.invoicing.infrastructure.excel.validators.importbooking;

import com.kynsoft.finamer.invoicing.application.excel.ExcelRuleValidator;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.invoicing.domain.excel.bean.BookingRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class ImportBookingCheckOutValidator extends ExcelRuleValidator<BookingRow> {

    private final String[] validDateFormat = new String[]{"yyyymmdd", "mm/dd/yyyy"};

    @Override
    public boolean validate(BookingRow obj, List<ErrorField> errorFieldList) {

        if (Objects.isNull(obj.getCheckOut()) || obj.getCheckOut().isEmpty()){
            errorFieldList.add(new ErrorField("CheckOut", "CheckOut can't be empty"));
            return false;
        }
        String date = obj.getCheckOut();
        boolean valid = false;
        for (String format : validDateFormat) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                simpleDateFormat.parse(date);
                valid = true;
                break;
            } catch (ParseException e) {
            }
        }
        if (!valid) {
            errorFieldList.add(new ErrorField("CheckOut", "CheckOut has invalid date format"));
            return false;
        }
        return true;
    }
}
