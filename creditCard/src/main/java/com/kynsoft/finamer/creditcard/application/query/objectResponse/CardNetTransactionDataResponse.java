package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kynsoft.finamer.creditcard.domain.dto.CardNetStatusResponseDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.CardNetResponseStatus;
import lombok.Data;

@Data
public class CardNetTransactionDataResponse {

    @JsonProperty("OrdenID")
    private String orderID;

    @JsonProperty("AuthorizationCode")
    private String authorizationCode;

    @JsonProperty("TxToken")
    private String txToken;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("CreditcardNumber")
    private String creditCardNumber;

    @JsonProperty("RetrivalReferenceNumber")
    private String retrievalReferenceNumber;

    @JsonProperty("RemoteResponseCode")
    private String remoteResponseCode;

    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("merchantStatus")
    private CardNetStatusResponseDto merchantStatus;
}