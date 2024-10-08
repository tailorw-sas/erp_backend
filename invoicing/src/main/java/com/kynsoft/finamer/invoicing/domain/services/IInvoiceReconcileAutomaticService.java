package com.kynsoft.finamer.invoicing.domain.services;

import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileAuto.InvoiceReconcileAutomaticRequest;
import com.kynsoft.finamer.invoicing.application.query.invoiceReconcile.processstatus.automatic.InvoiceReconcileAutomaticImportProcessStatusRequest;
import com.kynsoft.finamer.invoicing.application.query.invoiceReconcile.reconcileError.automatic.InvoiceReconcileAutomaticImportErrorRequest;
import com.kynsoft.finamer.invoicing.application.query.manageBooking.importbooking.ImportBookingProcessStatusResponse;

public interface IInvoiceReconcileAutomaticService {

    void reconcileAutomatic(InvoiceReconcileAutomaticRequest request);
    PaginatedResponse getImportErrors(InvoiceReconcileAutomaticImportErrorRequest invoiceReconcileAutomaticErrorRequest);
    public ImportBookingProcessStatusResponse getImportBookingProcessStatus(InvoiceReconcileAutomaticImportProcessStatusRequest importProcessStatusRequest);
}
