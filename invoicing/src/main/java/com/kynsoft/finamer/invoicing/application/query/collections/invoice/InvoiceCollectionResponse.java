package com.kynsoft.finamer.invoicing.application.query.collections.invoice;

import com.kynsof.share.core.domain.bus.query.IResponse;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InvoiceCollectionResponse implements IResponse {
    private PaginatedResponse paginatedResponse;
    private InvoiceCollectionsSummaryResponse summaryResponse;
}
