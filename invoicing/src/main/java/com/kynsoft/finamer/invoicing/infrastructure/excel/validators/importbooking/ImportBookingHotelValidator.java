package com.kynsoft.finamer.invoicing.infrastructure.excel.validators.importbooking;

import com.kynsoft.finamer.invoicing.application.excel.ExcelRuleValidator;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.invoicing.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.excel.bean.BookingRow;
import com.kynsoft.finamer.invoicing.domain.services.IManageHotelService;
import com.kynsoft.finamer.invoicing.infrastructure.utils.InvoiceUtils;

import java.util.List;
import java.util.Objects;

public class ImportBookingHotelValidator extends ExcelRuleValidator<BookingRow> {

    private final IManageHotelService manageHotelService;

    public ImportBookingHotelValidator(IManageHotelService manageHotelService) {
        this.manageHotelService = manageHotelService;
    }

    @Override
    public boolean validate(BookingRow obj, List<ErrorField> errorFieldList) {
        if (Objects.isNull(obj.getManageHotelCode()) || obj.getManageHotelCode().isEmpty()) {
            errorFieldList.add(new ErrorField("Hotel", " Hotel can't be empty"));
            return false;
        }
        if (!manageHotelService.existByCode(InvoiceUtils.upperCaseAndTrim(obj.getManageHotelCode()))) {
            errorFieldList.add(new ErrorField("Hotel", " Hotel not exists"));
            return false;
        } else {
            ManageHotelDto manageHotelDto = manageHotelService.findByCode(InvoiceUtils.upperCaseAndTrim(obj.getManageHotelCode()));
            if (!obj.getHotels().contains(manageHotelDto.getId())) {
                errorFieldList.add(new ErrorField("Hotel", "The employee does not have access to the hotel."));
                return false;
            }
            if (Status.INACTIVE.name().equals(manageHotelDto.getStatus())) {
                errorFieldList.add(new ErrorField("Hotel", " Hotel is inactive"));
                return false;
            }
        }
        return true;
    }
}
