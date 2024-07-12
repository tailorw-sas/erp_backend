package com.kynsoft.finamer.invoicing.domain.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@AllArgsConstructor
public class ImportBookingErrorRequest {
    private String importProcessId;
    private Pageable pageable;
}
