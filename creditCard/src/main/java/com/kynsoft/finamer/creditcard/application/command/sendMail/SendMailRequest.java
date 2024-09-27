package com.kynsoft.finamer.creditcard.application.command.sendMail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailRequest {
    private UUID transactionUuid;

}