package com.kynsoft.finamer.creditcard.application.command.transaction.manual;

import com.kynsof.share.core.application.mailjet.MailJetRecipient;
import com.kynsof.share.core.application.mailjet.MailJetVar;
import com.kynsof.share.core.application.mailjet.MailService;
import com.kynsof.share.core.application.mailjet.SendMailJetEMailRequest;
import com.kynsof.share.core.domain.RulesChecker;
import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.rules.ValidateObjectNotNullRule;
import com.kynsof.share.utils.UrlGetBase;
import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.ETransactionStatus;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.MethodType;
import com.kynsoft.finamer.creditcard.domain.rules.manualTransaction.*;
import com.kynsoft.finamer.creditcard.domain.services.*;
import com.kynsoft.finamer.creditcard.infrastructure.services.TokenService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CreateManualTransactionCommandHandler implements ICommandHandler<CreateManualTransactionCommand> {

    private final ITransactionService transactionService;

    private final IManageMerchantService merchantService;

    private final IManageHotelService hotelService;

    private final IManageAgencyService agencyService;

    private final IManageLanguageService languageService;

    private final IManageCreditCardTypeService creditCardTypeService;

    private final IManageTransactionStatusService transactionStatusService;

    private final IManageMerchantHotelEnrolleService merchantHotelEnrolleService;

    private final IParameterizationService parameterizationService;

    private final IManageVCCTransactionTypeService transactionTypeService;

    private final ICreditCardCloseOperationService closeOperationService;

    private final TokenService tokenService;

    private final MailService mailService;

    private final IManageMerchantConfigService merchantConfigService;

    private final IManagerMerchantCurrencyService merchantCurrencyService;

    public CreateManualTransactionCommandHandler(ITransactionService transactionService, IManageMerchantService merchantService, IManageHotelService hotelService, IManageAgencyService agencyService, IManageLanguageService languageService, IManageCreditCardTypeService creditCardTypeService, IManageTransactionStatusService transactionStatusService, IManageMerchantHotelEnrolleService merchantHotelEnrolleService, IParameterizationService parameterizationService, IManageVCCTransactionTypeService transactionTypeService, ICreditCardCloseOperationService closeOperationService, TokenService tokenService, MailService mailService, IManageMerchantConfigService merchantConfigService, IManagerMerchantCurrencyService merchantCurrencyService) {
        this.transactionService = transactionService;
        this.merchantService = merchantService;
        this.hotelService = hotelService;
        this.agencyService = agencyService;
        this.languageService = languageService;
        this.creditCardTypeService = creditCardTypeService;
        this.transactionStatusService = transactionStatusService;
        this.merchantHotelEnrolleService = merchantHotelEnrolleService;
        this.parameterizationService = parameterizationService;
        this.transactionTypeService = transactionTypeService;
        this.closeOperationService = closeOperationService;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.merchantConfigService = merchantConfigService;
        this.merchantCurrencyService = merchantCurrencyService;
    }

    @Override
    public void handle(CreateManualTransactionCommand command) {
        RulesChecker.checkRule(new ManualTransactionAmountRule(command.getAmount()));
        RulesChecker.checkRule(new ManualTransactionCheckInBeforeRule(command.getCheckIn()));
        RulesChecker.checkRule(new ManualTransactionCheckInCloseOperationRule(this.closeOperationService, command.getCheckIn(), command.getHotel()));

        ManageMerchantDto merchantDto = this.merchantService.findById(command.getMerchant());
        ManageHotelDto hotelDto = this.hotelService.findById(command.getHotel());

        RulesChecker.checkRule(new ManualTransactionReservationNumberUniqueRule(this.transactionService, command.getReservationNumber(), command.getHotel()));

        ManageAgencyDto agencyDto = this.agencyService.findById(command.getAgency());
        ManageLanguageDto languageDto = this.languageService.findById(command.getLanguage());
        ManagerMerchantCurrencyDto merchantCurrencyDto = this.merchantCurrencyService.findById(command.getMerchantCurrency());

//        RulesChecker.checkRule(new ManualTransactionAgencyBookingFormatRule(agencyDto.getBookingCouponFormat()));
//        RulesChecker.checkRule(new ManualTransactionReservationNumberRule(command.getReservationNumber(), agencyDto.getBookingCouponFormat()));

        ManageCreditCardTypeDto creditCardTypeDto = null;

        ManageTransactionStatusDto transactionStatusDto = this.transactionStatusService.findByETransactionStatus(ETransactionStatus.SENT);
        ManageVCCTransactionTypeDto transactionCategory = this.transactionTypeService.findByIsDefault();

        if (command.getMethodType().compareTo(MethodType.LINK) == 0) {
            RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getGuestName(), "gestName", "Guest name cannot be null."));
            RulesChecker.checkRule(new ValidateObjectNotNullRule<>(command.getEmail(), "email", "Email cannot be null."));
        }

        ManageMerchantHotelEnrolleDto merchantHotelEnrolleDto = this.merchantHotelEnrolleService.findByManageMerchantAndManageHotel(
                merchantDto, hotelDto
        );

        double commission = 0;
        double netAmount = command.getAmount() - commission;
        TransactionDto newTransaction = new TransactionDto(
                command.getTransactionUuid(),
                merchantDto,
                command.getMethodType(),
                hotelDto,
                agencyDto,
                languageDto,
                command.getAmount(),
                command.getCheckIn(),
                command.getReservationNumber(),
                command.getReferenceNumber(),
                command.getHotelContactEmail(),
                command.getGuestName(),
                command.getEmail(),
                merchantHotelEnrolleDto.getEnrolle(),
                null,
                creditCardTypeDto,
                commission,
                transactionStatusDto,
                null,
                transactionCategory,
                null,
                netAmount,
                true,
                merchantCurrencyDto,
                true
        );
        Long id = this.transactionService.create(newTransaction);
        command.setId(id);

        //Send Mail in case the methodType be Link
        if (command.getMethodType() == MethodType.LINK) {
            //Send mail after the crate transaction
            if (command.getEmail() != null && !command.getEmail().isEmpty()) {
                String token = tokenService.generateToken(command.getTransactionUuid());
                ManagerMerchantConfigDto merchantConfigDto = merchantConfigService.findByMerchantID(merchantDto.getId());
                String baseUrl = UrlGetBase.getBaseUrl(merchantConfigDto.getSuccessUrl());
                String paymentLink = baseUrl + "payment?token=" + token;
                transactionService.sendTransactionPaymentLinkEmail(newTransaction, paymentLink);
            }
        }
    }

}
