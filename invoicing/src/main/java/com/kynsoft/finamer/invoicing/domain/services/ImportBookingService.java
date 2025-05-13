package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.application.command.manageBooking.importbooking.ImportBookingFromFileRequest;
import com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking.ImportBookingErrorRequest;
import com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking.ImportBookingFromFileErrorResponse;
import com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking.ImportBookingProcessStatusResponse;
import com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking.ImportBookingProcessStatusRequest;
import com.kynsoft.finamer.invoicing.infrastructure.identity.redis.excel.BookingRowError;

import java.util.List;

public interface ImportBookingService {

    void importBookingFromFile(ImportBookingFromFileRequest importBookingFromFileRequest);

    PaginatedResponse getImportError(ImportBookingErrorRequest importBookingErrorRequest);

    ImportBookingProcessStatusResponse getImportBookingProcessStatus(ImportBookingProcessStatusRequest importBookingProcessStatusRequest);

}
