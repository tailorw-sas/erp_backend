package com.kynsoft.finamer.invoicing.application.query.collections.invoice;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceService;
import com.kynsoft.finamer.invoicing.infrastructure.interfacesEntity.ManageInvoiceSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InvoiceCollectionsSummaryQueryHandler implements IQueryHandler<InvoiceCollectionsSummaryQuery, InvoiceCollectionResponse> {

    private final IManageInvoiceService manageInvoiceService;

    public InvoiceCollectionsSummaryQueryHandler(IManageInvoiceService manageInvoiceService) {
        this.manageInvoiceService = manageInvoiceService;
    }

    @Override
    public InvoiceCollectionResponse handle(InvoiceCollectionsSummaryQuery query) {
        Page<ManageInvoiceSearchProjection> invoicePage = manageInvoiceService.getInvoiceForSummary(
                query.getPageable(), query.getFilter(), query.getEmployeeId()
        );

        List<ManageInvoiceSearchProjection> invoices = invoicePage.getContent();
        Map<Integer, List<ManageInvoiceSearchProjection>> agingGroups = groupInvoicesByAging(invoices);

        InvoiceCollectionsSummaryResponse summaryResponse = buildSummaryResponse(invoices, agingGroups);
        PaginatedResponse paginatedResponse = buildPaginatedResponse(invoicePage);

        return new InvoiceCollectionResponse(paginatedResponse, summaryResponse);
    }

    private Map<Integer, List<ManageInvoiceSearchProjection>> groupInvoicesByAging(List<ManageInvoiceSearchProjection> invoices) {
        return invoices.stream().collect(Collectors.groupingBy(invoice -> {
            return resolveAgingBucket(invoice.getAging());
        }));
    }

    private int resolveAgingBucket(long days) {
        if (days > 0 && days <= 30) return 30;
        else if (days <= 60) return 60;
        else if (days <= 90) return 90;
        else if (days > 90) return 120;
        return 0;
    }

    private InvoiceCollectionsSummaryResponse buildSummaryResponse(List<ManageInvoiceSearchProjection> invoices,
                                                                   Map<Integer, List<ManageInvoiceSearchProjection>> agingGroups) {
        int total = invoices.size();

        long count30 = agingGroups.getOrDefault(30, List.of()).size();
        long count60 = agingGroups.getOrDefault(60, List.of()).size();
        long count90 = agingGroups.getOrDefault(90, List.of()).size();
        long count120 = agingGroups.getOrDefault(120, List.of()).size();
        long count0 = agingGroups.getOrDefault(0, List.of()).size();

        return InvoiceCollectionsSummaryResponse.builder()
                .totalInvoiceWith30(count30)
                .totalInvoiceWithAging60(count60)
                .totalInvoiceWithAging90(count90)
                .totalInvoiceWithAging120(count120)
                .totalInvoiceWithAging0(count0)
                .totalInvoicePercentWithAging30(percent(count30, total))
                .totalInvoicePercentWithAging60(percent(count60, total))
                .totalInvoicePercentWithAging90(percent(count90, total))
                .totalInvoicePercentWithAging120(percent(count120, total))
                .totalInvoiceBalanceWithAging30(balance(agingGroups.getOrDefault(30, List.of())))
                .totalInvoiceBalanceWithAging60(balance(agingGroups.getOrDefault(60, List.of())))
                .totalInvoiceBalanceWithAging90(balance(agingGroups.getOrDefault(90, List.of())))
                .totalInvoiceBalanceWithAging120(balance(agingGroups.getOrDefault(120, List.of())))
                .totalInvoiceAmount(invoices.stream().mapToDouble(ManageInvoiceSearchProjection::getInvoiceAmount).sum())
                .totalInvoiceDueAmount(invoices.stream().mapToDouble(ManageInvoiceSearchProjection::getDueAmount).sum())
                .build();
    }

    private long percent(long partial, int total) {
        return total > 0 ? Math.round(partial * 100.0 / total) : 0;
    }

    private double balance(List<ManageInvoiceSearchProjection> invoices) {
        return invoices.stream().mapToDouble(ManageInvoiceSearchProjection::getDueAmount).sum();
    }

    private PaginatedResponse buildPaginatedResponse(Page<ManageInvoiceSearchProjection> page) {
        return new PaginatedResponse(
                page.getContent(),
                page.getTotalPages(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber()
        );
    }
}
