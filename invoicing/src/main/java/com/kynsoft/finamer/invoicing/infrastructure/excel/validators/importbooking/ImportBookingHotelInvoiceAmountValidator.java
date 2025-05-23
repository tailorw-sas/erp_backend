package com.kynsoft.finamer.invoicing.infrastructure.excel.validators.importbooking;

import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.invoicing.application.excel.ExcelRuleValidator;
import com.kynsoft.finamer.invoicing.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.invoicing.domain.excel.bean.BookingRow;
import com.kynsoft.finamer.invoicing.domain.services.IManageHotelService;
import com.kynsoft.finamer.invoicing.infrastructure.utils.InvoiceUtils;

import java.util.List;
import java.util.Objects;

public class ImportBookingHotelInvoiceAmountValidator extends ExcelRuleValidator<BookingRow> {

    private final IManageHotelService manageHotelService;

    public ImportBookingHotelInvoiceAmountValidator(IManageHotelService manageHotelService) {
        this.manageHotelService = manageHotelService;
    }

    @Override
    public boolean validate(BookingRow obj, List<ErrorField> errorFieldList) {
        try {
            if (!manageHotelService.existByCode(InvoiceUtils.upperCaseAndTrim(obj.getManageHotelCode()))) {
                //errorFieldList.add(new ErrorField("Hotel", " Hotel not found."));
                return false;
            }

            ManageHotelDto manageHotelDto = manageHotelService.findByCode(InvoiceUtils.upperCaseAndTrim(obj.getManageHotelCode()));
            //if (manageHotelDto.isVirtual() && !manageHotelDto.isRequiresFlatRate()) {
            if (!manageHotelDto.isRequiresFlatRate()) {
                return true;
            }
            if (Objects.isNull(obj.getHotelInvoiceAmount())) {
                errorFieldList.add(new ErrorField("Hotel Invoice Amount", " Hotel Invoice Amount can't be empty"));
                return false;
            }
            if (errorFieldList.stream().noneMatch(errorField -> "Hotel".equals(errorField.getField()))) {
                //ManageHotelDto manageHotelDto = manageHotelService.findByCode(obj.getManageHotelCode());
                if (manageHotelDto.isRequiresFlatRate() && obj.getHotelInvoiceAmount() <= 0) {
                    errorFieldList.add(new ErrorField("Hotel Invoice Amount", "Hotel Invoice Amount must be greater than 0"));
                    return false;
                }
            }
        } catch (Exception e) {
            errorFieldList.add(new ErrorField("Hotel", " Hotel not found."));
            return false;
        }
        return true;
    }

}
