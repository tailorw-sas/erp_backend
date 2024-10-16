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

    private final ITransactionService service;

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

    public CreateManualTransactionCommandHandler(ITransactionService service, IManageMerchantService merchantService, IManageHotelService hotelService, IManageAgencyService agencyService, IManageLanguageService languageService, IManageCreditCardTypeService creditCardTypeService, IManageTransactionStatusService transactionStatusService, IManageMerchantHotelEnrolleService merchantHotelEnrolleService, IParameterizationService parameterizationService, IManageVCCTransactionTypeService transactionTypeService, ICreditCardCloseOperationService closeOperationService, TokenService tokenService, MailService mailService, IManageMerchantConfigService merchantConfigService, IManagerMerchantCurrencyService merchantCurrencyService) {
        this.service = service;
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

        RulesChecker.checkRule(new ManualTransactionReservationNumberUniqueRule(this.service, command.getReservationNumber(), command.getHotel()));

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

        Long id = this.service.create(new TransactionDto(
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
        ));
        command.setId(id);

        //Send Mail after create the transaction to the HotelEmailContact in case of this exist
        if(command.getHotelContactEmail() != null){
            SendMailJetEMailRequest request = new SendMailJetEMailRequest();
            request.setTemplateId(6371592); // Cambiar en configuración

            // Variables para el template de email, cambiar cuando keimer genere la plantilla
            List<MailJetVar> vars = Arrays.asList(
                    new MailJetVar("topic", "Transaction Verification"),
                    new MailJetVar("name", command.getGuestName())
            );
            request.setMailJetVars(vars);

            // Recipients
            List<MailJetRecipient> recipients = new ArrayList<>();
            recipients.add(new MailJetRecipient(command.getHotelContactEmail(), command.getGuestName()));
            request.setRecipientEmail(recipients);

            mailService.sendMail(request);
        }

        //Send Mail in case the methodType be Link
        if(command.getMethodType() == MethodType.LINK){
        //Send mail after the crate transaction
        String token = tokenService.generateToken(command.getTransactionUuid());
        if (command.getEmail() != null) {
            SendMailJetEMailRequest request = new SendMailJetEMailRequest();
            request.setTemplateId(6324713); // Cambiar en configuración

            ManagerMerchantConfigDto merchantConfigDto = merchantConfigService.findByMerchantID(merchantDto.getId());
            String baseUrl = UrlGetBase.getBaseUrl(merchantConfigDto.getSuccessUrl());

            // Variables para el template de email
            List<MailJetVar> vars = Arrays.asList(
                    new MailJetVar("payment_link", baseUrl + "payment?token="+ token),
                    new MailJetVar("invoice_amount", command.getAmount().toString())
            );
            request.setMailJetVars(vars);

            // Recipients
            List<MailJetRecipient> recipients = new ArrayList<>();
            recipients.add(new MailJetRecipient(command.getEmail(), command.getGuestName()));
            recipients.add(new MailJetRecipient("keimermo1989@gmail.com", command.getGuestName()));
            request.setRecipientEmail(recipients);

            mailService.sendMail(request);
        }
        }
    }

}
