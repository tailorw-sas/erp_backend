package com.kynsoft.finamer.creditcard.application.command.sendMail;

import com.kynsof.share.core.application.mailjet.*;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.exception.BusinessException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.utils.UrlGetBase;
import com.kynsoft.finamer.creditcard.domain.dto.ManageMerchantHotelEnrolleDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManagerMerchantConfigDto;
import com.kynsoft.finamer.creditcard.domain.dto.TransactionDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageMerchantConfigService;
import com.kynsoft.finamer.creditcard.domain.services.IManageMerchantHotelEnrolleService;
import com.kynsoft.finamer.creditcard.domain.services.ITransactionService;
import com.kynsoft.finamer.creditcard.infrastructure.services.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class SendMailCommandHandler implements ICommandHandler<SendMailCommand> {


    private final MailService mailService;
    private final ITransactionService transactionService;
    private final TokenService tokenService;
    private final IManageMerchantConfigService merchantConfigService;
    private final IManageMerchantHotelEnrolleService merchantHotelEnrolleService;

    public SendMailCommandHandler(MailService mailService, ITransactionService transactionService, TokenService tokenService, IManageMerchantConfigService merchantConfigService, IManageMerchantHotelEnrolleService merchantHotelEnrolleService) {

        this.mailService = mailService;
        this.transactionService = transactionService;
        this.tokenService = tokenService;
        this.merchantConfigService = merchantConfigService;
        this.merchantHotelEnrolleService = merchantHotelEnrolleService;
    }

    @Override
    @Transactional
    public void handle(SendMailCommand command) {
        sendEmail(command, command.getId());
    }

    private void sendEmail(SendMailCommand command, Long id) {

        TransactionDto transactionDto = transactionService.findById(id);
        if (transactionDto.getGuestName().isEmpty() || transactionDto.getEmail().isEmpty()) {
            throw new BusinessException(DomainErrorMessage.MANAGE_TRANSACTION_RESEND_LINK_INVALID, DomainErrorMessage.MANAGE_TRANSACTION_RESEND_LINK_INVALID.getReasonPhrase());
        }

        ManagerMerchantConfigDto merchantConfigDto = merchantConfigService.findByMerchantID(transactionDto.getMerchant().getId());
        String baseUrl = UrlGetBase.getBaseUrl(merchantConfigDto.getSuccessUrl());
        String paymentLink = baseUrl + "payment?token=" + tokenService.generateToken(transactionDto.getTransactionUuid());
        ManageMerchantHotelEnrolleDto hotelEnrolleDto = merchantHotelEnrolleService.findByForeignIds(transactionDto.getMerchant().getId(), transactionDto.getHotel().getId(), transactionDto.getMerchantCurrency().getId());
        try {
            transactionService.sendTransactionPaymentLinkEmail(transactionDto, paymentLink);
            command.setResult(true);
        } catch (Exception e) {
            command.setResult(false);
        }
    }


}
