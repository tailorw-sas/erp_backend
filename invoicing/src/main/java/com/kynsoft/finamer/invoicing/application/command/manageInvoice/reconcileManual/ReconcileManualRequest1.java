package com.kynsoft.finamer.invoicing.application.command.manageInvoice.reconcileManual;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReconcileManualRequest1 {
    private List<UUID> invoices;
}
