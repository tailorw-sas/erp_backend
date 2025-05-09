package com.kynsoft.finamer.creditcard.application.query.objectResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardNetSessionResponse {
    @JsonProperty("SESSION")
    private String session;

    @JsonProperty("session-key")
    private String sessionKey;
}
