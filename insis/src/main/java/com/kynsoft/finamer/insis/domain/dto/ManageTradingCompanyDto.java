package com.kynsoft.finamer.insis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ManageTradingCompanyDto {

    private UUID id;
    private String code;
    private String company;
    private String innsistCode;
    private String status;
    private LocalDateTime updatedAt;
    private InnsistConnectionParamsDto innsistConnectionParams;
    private boolean hasConnection;
}
