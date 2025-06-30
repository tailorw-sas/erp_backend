package com.kynsoft.finamer.insis.application.services.roomRate.externalSearch;

import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsoft.finamer.insis.domain.dto.ExternalRoomRateDto;
import com.kynsoft.finamer.insis.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.insis.domain.services.IExternalRoomRateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SearchExternalRoomRatesService {

    private final IExternalRoomRateService externalRoomRateService;

    public SearchExternalRoomRatesService(IExternalRoomRateService externalRoomRateService){
        this.externalRoomRateService = externalRoomRateService;
    }

    public List<ExternalRoomRateDto> getExternalRoomRates(LocalDate invoiceDate,
                                                          ManageHotelDto hotelDto){
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(hotelDto, "Manage Hotel Dto", "The Hotel must not be null"));
        RulesChecker.checkRule(new ValidateObjectNotNullRule<>(invoiceDate, "Invoice Date", "The Invoice Date must not be null"));

        return externalRoomRateService.getTcaRoomRatesByInvoiceDateAndHotel(UUID.randomUUID(),
                invoiceDate,
                hotelDto.getCode());
    }
}
