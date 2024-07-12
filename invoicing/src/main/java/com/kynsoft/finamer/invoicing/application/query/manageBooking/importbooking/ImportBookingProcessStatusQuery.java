package com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking;

import com.kynsof.share.core.domain.bus.query.IQuery;
import com.kynsoft.finamer.invoicing.domain.excel.validators.ImportBookingProcessStatusRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImportBookingProcessStatusQuery implements IQuery {
    private ImportBookingProcessStatusRequest request;
}
