package com.kynsoft.finamer.settings.application.command.replicate.objects;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.*;
import com.kynsof.share.core.domain.kafka.entity.vcc.*;
import com.kynsoft.finamer.settings.domain.dto.*;
import com.kynsoft.finamer.settings.domain.services.*;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAgency.ProducerReplicateManageAgencyService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAgencyContact.ProducerReplicateManageAgencyContactService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAgencyType.ProducerReplicateManageAgencyTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageAttachmentType.ProducerReplicateManageAttachmentTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageB2BPartner.ProducerReplicateB2BPartnerService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageB2BPartnerType.ProducerReplicateB2BPartnerTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageBankAccount.ProducerReplicateManageBankAccount;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageCityState.ProducerReplicateManageCityStateService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageClient.ProducerReplicateManageClientService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageContact.ProducerReplicateManageContactService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageCountry.ProducerReplicateManageCountryService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageCreditCardType.ProducerReplicateManageCreditCardTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageCurrency.ProducerReplicateManageCurrencyService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageEmployee.ProducerReplicateManageEmployeeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageHotel.ProducerReplicateManageHotelService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageInvoiceStatus.ProducerReplicateManageInvoiceStatusService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageInvoiceTransactionType.ProducerReplicateManageInvoiceTransactionTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageInvoiceType.ProducerReplicateManageInvoiceTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageLanguage.ProducerReplicateManageLanguageService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageMerchant.ProducerReplicateManageMerchantService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageMerchantConfig.ProducerReplicateManageMerchantConfigService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageMerchantCurency.ProducerReplicateManageMerchantCurrencyService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.managePaymentAttachmentStatus.ProducerReplicateManagePaymentAttachmentStatusService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.managePaymentSource.ProducerReplicateManagePaymentSourceService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.managePaymentStatus.ProducerReplicateManagePaymentStatusService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.managePaymentTransactionType.ProducerReplicateManagePaymentTransactionTypeService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageRegion.ProducerReplicateManageRegionService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageTradigCompany.ProducerReplicateManageTradingCompanyService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageTransactionStatus.ProducerReplicateManageTransactionStatusService;
import com.kynsoft.finamer.settings.infrastructure.services.kafka.producer.manageVCCTransactionType.ProducerReplicateManageVCCTransactionTypeService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CreateReplicateCommandHandler implements ICommandHandler<CreateReplicateCommand> {

    private final IManageInvoiceTypeService invoiceTypeService;
    private final IManageInvoiceTransactionTypeService invoiceTransactionTypeService;
    private final IManagerPaymentStatusService paymentStatusService;
    private final IManagePaymentSourceService paymentSourceService;
    private final IManagePaymentTransactionTypeService paymentTransactionTypeService;
    private final ProducerReplicateManageInvoiceTypeService replicateManageInvoiceTypeService;
    private final ProducerReplicateManagePaymentSourceService replicateManagePaymentSourceService;
    private final ProducerReplicateManagePaymentStatusService replicateManagePaymentStatusService;
    private final ProducerReplicateManagePaymentTransactionTypeService replicateManagePaymentTransactionTypeService;
    private final ProducerReplicateManageInvoiceStatusService replicateManageInvoiceStatusService;
    private final ProducerReplicateManageInvoiceTransactionTypeService replicateManageInvoiceTransactionTypeService;
    private final IManageInvoiceStatusService invoiceStatusService;
    private final IManageAttachmentTypeService attachmentTypeService;
    private final IManageTradingCompaniesService tradingCompaniesService;

    private final IManageAgencyService manageAgencyService;
    private final IManageAgencyTypeService manageAgencyTypeService;
    private final IManageBankAccountService manageBankAccountService;
    private final IManageEmployeeService manageEmployeeService;
    private final IManageHotelService manageHotelService;
    private final IManagerClientService managerClientService;
    private final IManagePaymentAttachmentStatusService managePaymentAttachmentStatusService;

    private final IManagerLanguageService managerLanguageService;
    private final IManageVCCTransactionTypeService manageVCCTransactionTypeService;
    private final IManageTransactionStatusService manageTransactionStatusService;

    private final IManagerB2BPartnerService managerB2BPartnerService;

    private final IManageB2BPartnerTypeService manageB2BPartnerTypeService;

    private final IManageMerchantConfigService manageMerchantConfigService;
    private final IManagerMerchantService iManagerMerchantService;
    private final ProducerReplicateManageMerchantConfigService producerReplicateManageMerchantConfigService;

    private final IManageCityStateService manageCityStateService;

    private final IManagerCountryService managerCountryService;
    private final IManageContactService manageContactService;

    private final ProducerReplicateManageContactService producerReplicateManageContactService;
    private final ProducerReplicateManageAgencyService replicateManageAgencyService;
    private final ProducerReplicateManageBankAccount replicateManageBankAccount;
    private final ProducerReplicateManageEmployeeService replicateManageEmployeeService;
    private final ProducerReplicateManageHotelService replicateManageHotelService;
    private final ProducerReplicateManageClientService replicateManageClientService;
    private final ProducerReplicateManagePaymentAttachmentStatusService replicateManagePaymentAttachmentStatusService;
    private final ProducerReplicateManageAttachmentTypeService replicateManageAttachmentTypeService;
    private final ProducerReplicateManageAgencyTypeService replicateManageAgencyTypeService;
    private final ProducerReplicateManageLanguageService replicateManageLanguageService;
    private final ProducerReplicateManageVCCTransactionTypeService replicateManageVCCTransactionTypeService;
    private final ProducerReplicateManageTransactionStatusService replicateManageTransactionStatusService;

    private final ProducerReplicateB2BPartnerService producerReplicateB2BPartnerService;
    private final ProducerReplicateB2BPartnerTypeService producerReplicateB2BPartnerTypeService;
    private final ProducerReplicateManageMerchantService producerReplicateManageMerchantService;

    private final ProducerReplicateManageCityStateService producerReplicateManageCityStateService;

    private final ProducerReplicateManageCountryService producerReplicateManageCountryService;

    private final ProducerReplicateManageTradingCompanyService producerReplicateManageTradingCompanyService;

    private final IManagerCurrencyService currencyService;
    private final ProducerReplicateManageCurrencyService producerReplicateManageCurrencyService;

    private final IManageRegionService regionService;
    private final ProducerReplicateManageRegionService producerReplicateManageRegionService;

    private final IManageAgencyContactService agencyContactService;
    private final ProducerReplicateManageAgencyContactService producerReplicateManageAgencyContactService;

    private final IManageCreditCardTypeService creditCardTypeService;
    private final ProducerReplicateManageCreditCardTypeService producerReplicateManageCreditCardTypeService;

    private final IManagerMerchantCurrencyService merchantCurrencyService;
    private final ProducerReplicateManageMerchantCurrencyService producerReplicateManageMerchantCurrencyService;

    public CreateReplicateCommandHandler(IManageInvoiceTypeService invoiceTypeService,
                                         IManagerPaymentStatusService paymentStatusService,
                                         IManagePaymentSourceService paymentSourceService,
                                         IManagePaymentTransactionTypeService paymentTransactionTypeService,
                                         ProducerReplicateManageInvoiceTypeService replicateManageInvoiceTypeService,
                                         ProducerReplicateManagePaymentSourceService replicateManagePaymentSourceService,
                                         ProducerReplicateManagePaymentStatusService replicateManagePaymentStatusService,
                                         ProducerReplicateManagePaymentTransactionTypeService replicateManagePaymentTransactionTypeService,
                                         ProducerReplicateManageInvoiceStatusService replicateManageInvoiceStatusService,
                                         ProducerReplicateManageInvoiceTransactionTypeService replicateManageInvoiceTransactionTypeService,
                                         IManageInvoiceStatusService invoiceStatusService, IManageTradingCompaniesService tradingCompaniesService, IManageAgencyService manageAgencyService,
                                         IManageBankAccountService manageBankAccountService, IManageEmployeeService manageEmployeeService,
                                         IManageHotelService manageHotelService, IManagerClientService managerClientService,
                                         IManagePaymentAttachmentStatusService managePaymentAttachmentStatusService,
                                         ProducerReplicateManageAgencyService replicateManageAgencyService,
                                         ProducerReplicateManageBankAccount replicateManageBankAccount,
                                         ProducerReplicateManageEmployeeService replicateManageEmployeeService,
                                         ProducerReplicateManageHotelService replicateManageHotelService,
                                         ProducerReplicateManageClientService replicateManageClientService,
                                         ProducerReplicateManagePaymentAttachmentStatusService replicateManagePaymentAttachmentStatusService,
                                         IManageInvoiceTransactionTypeService invoiceTransactionTypeService,
                                         ProducerReplicateManageAttachmentTypeService replicateManageAttachmentTypeService,
                                         IManageAttachmentTypeService attachmentTypeService,
                                         IManageAgencyTypeService manageAgencyTypeService, IManagerB2BPartnerService managerB2BPartnerService,
                                         ProducerReplicateManageAgencyTypeService replicateManageAgencyTypeService,
                                         IManagerLanguageService managerLanguageService,
                                         IManageVCCTransactionTypeService manageVCCTransactionTypeService,
                                         IManageTransactionStatusService manageTransactionStatusService,
                                         IManageB2BPartnerTypeService manageB2BPartnerTypeService, IManageMerchantConfigService manageMerchantConfigService, IManagerMerchantService iManagerMerchantService, ProducerReplicateManageMerchantConfigService producerReplicateManageMerchantConfigService,
                                         IManageCityStateService manageCityStateService, IManagerCountryService managerCountryService,
                                         ProducerReplicateManageLanguageService replicateManageLanguageService,
                                         ProducerReplicateManageVCCTransactionTypeService replicateManageVCCTransactionTypeService,
                                         ProducerReplicateManageTransactionStatusService replicateManageTransactionStatusService,
                                         ProducerReplicateB2BPartnerService producerReplicateB2BPartnerService,
                                         ProducerReplicateB2BPartnerTypeService producerReplicateB2BPartnerTypeService, ProducerReplicateManageMerchantService producerReplicateManageMerchantService,
                                         ProducerReplicateManageCityStateService producerReplicateManageCityStateService,
                                         ProducerReplicateManageCountryService producerReplicateManageCountryService, ProducerReplicateManageTradingCompanyService producerReplicateManageTradingCompanyService,
                                         IManageContactService manageContactService,
                                         ProducerReplicateManageContactService producerReplicateManageContactService, IManagerCurrencyService currencyService, ProducerReplicateManageCurrencyService producerReplicateManageCurrencyService, IManageRegionService regionService, ProducerReplicateManageRegionService producerReplicateManageRegionService, IManageAgencyContactService agencyContactService, ProducerReplicateManageAgencyContactService producerReplicateManageAgencyContactService, IManageCreditCardTypeService creditCardTypeService, ProducerReplicateManageCreditCardTypeService producerReplicateManageCreditCardTypeService, IManagerMerchantCurrencyService merchantCurrencyService, ProducerReplicateManageMerchantCurrencyService producerReplicateManageMerchantCurrencyService) {
        this.tradingCompaniesService = tradingCompaniesService;
        this.managerB2BPartnerService = managerB2BPartnerService;
        this.managerLanguageService = managerLanguageService;
        this.manageVCCTransactionTypeService = manageVCCTransactionTypeService;
        this.manageTransactionStatusService = manageTransactionStatusService;
        this.invoiceTypeService = invoiceTypeService;
        this.paymentStatusService = paymentStatusService;
        this.paymentSourceService = paymentSourceService;
        this.paymentTransactionTypeService = paymentTransactionTypeService;
        this.replicateManageInvoiceTypeService = replicateManageInvoiceTypeService;
        this.replicateManagePaymentSourceService = replicateManagePaymentSourceService;
        this.replicateManagePaymentStatusService = replicateManagePaymentStatusService;
        this.replicateManagePaymentTransactionTypeService = replicateManagePaymentTransactionTypeService;
        this.replicateManageInvoiceStatusService = replicateManageInvoiceStatusService;
        this.replicateManageInvoiceTransactionTypeService = replicateManageInvoiceTransactionTypeService;
        this.invoiceStatusService = invoiceStatusService;
        this.manageAgencyService = manageAgencyService;
        this.manageBankAccountService = manageBankAccountService;
        this.manageEmployeeService = manageEmployeeService;
        this.manageHotelService = manageHotelService;
        this.managerClientService = managerClientService;
        this.managePaymentAttachmentStatusService = managePaymentAttachmentStatusService;
        this.replicateManageAgencyService = replicateManageAgencyService;
        this.replicateManageBankAccount = replicateManageBankAccount;
        this.replicateManageEmployeeService = replicateManageEmployeeService;
        this.replicateManageHotelService = replicateManageHotelService;
        this.replicateManageClientService = replicateManageClientService;
        this.replicateManagePaymentAttachmentStatusService = replicateManagePaymentAttachmentStatusService;
        this.invoiceTransactionTypeService = invoiceTransactionTypeService;
        this.attachmentTypeService = attachmentTypeService;
        this.replicateManageAttachmentTypeService = replicateManageAttachmentTypeService;
        this.manageAgencyTypeService = manageAgencyTypeService;
        this.replicateManageAgencyTypeService = replicateManageAgencyTypeService;
        this.manageB2BPartnerTypeService = manageB2BPartnerTypeService;
        this.manageMerchantConfigService = manageMerchantConfigService;
        this.iManagerMerchantService = iManagerMerchantService;
        this.producerReplicateManageMerchantConfigService = producerReplicateManageMerchantConfigService;
        this.manageCityStateService = manageCityStateService;
        this.managerCountryService = managerCountryService;
        this.replicateManageLanguageService = replicateManageLanguageService;
        this.replicateManageVCCTransactionTypeService = replicateManageVCCTransactionTypeService;
        this.replicateManageTransactionStatusService = replicateManageTransactionStatusService;
        this.producerReplicateB2BPartnerService = producerReplicateB2BPartnerService;
        this.producerReplicateB2BPartnerTypeService = producerReplicateB2BPartnerTypeService;
        this.producerReplicateManageMerchantService = producerReplicateManageMerchantService;
        this.producerReplicateManageCityStateService = producerReplicateManageCityStateService;
        this.producerReplicateManageCountryService = producerReplicateManageCountryService;
        this.producerReplicateManageTradingCompanyService = producerReplicateManageTradingCompanyService;
        this.manageContactService = manageContactService;
        this.producerReplicateManageContactService = producerReplicateManageContactService;
        this.currencyService = currencyService;
        this.producerReplicateManageCurrencyService = producerReplicateManageCurrencyService;
        this.regionService = regionService;
        this.producerReplicateManageRegionService = producerReplicateManageRegionService;
        this.agencyContactService = agencyContactService;
        this.producerReplicateManageAgencyContactService = producerReplicateManageAgencyContactService;
        this.creditCardTypeService = creditCardTypeService;
        this.producerReplicateManageCreditCardTypeService = producerReplicateManageCreditCardTypeService;
        this.merchantCurrencyService = merchantCurrencyService;
        this.producerReplicateManageMerchantCurrencyService = producerReplicateManageMerchantCurrencyService;
    }

    @Override
    public void handle(CreateReplicateCommand command) {
        for (ObjectEnum object : command.getObjects()) {
            switch (object) {
                case MANAGE_TRANSACTION_STATUS -> {
                    for (ManageTransactionStatusDto transactionStatusDto : this.manageTransactionStatusService.findAllToReplicate()) {
                        this.replicateManageTransactionStatusService.create(new ReplicateManageTransactionStatusKafka(transactionStatusDto.getId(), transactionStatusDto.getCode(), transactionStatusDto.getName()));
                    }
                }
                case MANAGE_CONTACT -> {
                    for (ManageContactDto manageContactDto : this.manageContactService.findAllToReplicate()) {
                        this.producerReplicateManageContactService.create(new ManageContactKafka(manageContactDto.getId(), manageContactDto.getCode(), manageContactDto.getDescription(), manageContactDto.getName(), manageContactDto.getManageHotel().getId(), manageContactDto.getEmail(), manageContactDto.getPhone(), manageContactDto.getPosition()));
                    }
                }
                case MANAGE_VCC_TRANSACTION_TYPE -> {
                    for (ManageVCCTransactionTypeDto transactionTypeDto : this.manageVCCTransactionTypeService.findAllToReplicate()) {
                        this.replicateManageVCCTransactionTypeService.create(new ReplicateManageVCCTransactionTypeKafka(transactionTypeDto.getId(), transactionTypeDto.getCode(), transactionTypeDto.getName(), transactionTypeDto.getIsDefault()));
                    }
                }
                case MANAGE_LANGUAGE -> {
                    for (ManagerLanguageDto managerLanguageDto : this.managerLanguageService.findAllToReplicate()) {
                        this.replicateManageLanguageService.create(new ReplicateManageLanguageKafka(managerLanguageDto.getId(), managerLanguageDto.getCode(), managerLanguageDto.getName(), managerLanguageDto.getDefaults(), managerLanguageDto.getStatus().name()));
                    }
                }
                case MANAGE_INVOICE_TYPE -> {
                    for (ManageInvoiceTypeDto invoiceType : this.invoiceTypeService.findAllToReplicate()) {
                        this.replicateManageInvoiceTypeService.create(new ReplicateManageInvoiceTypeKafka(invoiceType.getId(), invoiceType.getCode(), invoiceType.getName()));
                    }
                }
                case MANAGE_ATTACHMENT_TYPE -> {
                    for (ManageAttachmentTypeDto attachmentTypeDto : this.attachmentTypeService.findAllToReplicate()) {
                        this.replicateManageAttachmentTypeService.create(new ReplicateManageAttachmentTypeKafka(attachmentTypeDto.getId(), attachmentTypeDto.getCode(), attachmentTypeDto.getName(), attachmentTypeDto.getStatus().toString(), attachmentTypeDto.getDefaults(), attachmentTypeDto.getAttachInvDefault()));
                    }
                }
                case MANAGE_AGENCY_TYPE -> {
                    for (ManageAgencyTypeDto agencyTypeDto : this.manageAgencyTypeService.findAllToReplicate()) {
                        this.replicateManageAgencyTypeService.create(new ReplicateManageAgencyTypeKafka(agencyTypeDto.getId(), agencyTypeDto.getCode(), agencyTypeDto.getName(), agencyTypeDto.getStatus().toString()));
                    }
                }
                case MANAGE_AGENCY -> {//
                    for (ManageAgencyDto agencyDto : this.manageAgencyService.findAllToReplicate()) {
                        this.replicateManageAgencyService.create(new ReplicateManageAgencyKafka(
                                agencyDto.getId(),
                                agencyDto.getCode(),
                                agencyDto.getName(),
                                agencyDto.getClient().getId(),
                                agencyDto.getBookingCouponFormat(),
                                agencyDto.getStatus().name(),
                                agencyDto.getGenerationType().name(),
                                agencyDto.getAgencyType().getId(),
                                agencyDto.getCif(),
                                agencyDto.getAddress(),
                                Objects.nonNull(agencyDto.getSentB2BPartner()) ? agencyDto.getSentB2BPartner().getId() : null,
                                Objects.nonNull(agencyDto.getCityState()) ? agencyDto.getCityState().getId() : null,
                                Objects.nonNull(agencyDto.getCountry()) ? agencyDto.getCountry().getId() : null,
                                agencyDto.getMailingAddress(),
                                agencyDto.getZipCode(),
                                agencyDto.getCity(),
                                agencyDto.getCreditDay(),
                                agencyDto.getAutoReconcile(),
                                agencyDto.getValidateCheckout()
                        ));
                    }
                }
                case MANAGE_BANK_ACCOUNT -> {//
                    for (ManageBankAccountDto bankAccountDto : this.manageBankAccountService.findAllToReplicate()) {
                        this.replicateManageBankAccount.create(new ReplicateManageBankAccountKafka(bankAccountDto.getId(), bankAccountDto.getAccountNumber(), bankAccountDto.getStatus().name(), bankAccountDto.getManageBank().getName(), bankAccountDto.getManageHotel().getId()));
                    }
                }
                case MANAGE_EMPLOYEE -> {//
                    for (ManageEmployeeDto employeeDto : this.manageEmployeeService.findAllToReplicate()) {
                        this.replicateManageEmployeeService.create(new ReplicateManageEmployeeKafka(employeeDto.getId(), employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getEmail()));
                    }
                }
                case MANAGE_ATTACHMENT_STATUS -> {//
                    for (ManagePaymentAttachmentStatusDto paymentAttachmentStatusDto : this.managePaymentAttachmentStatusService.findAllToReplicate()) {
                        this.replicateManagePaymentAttachmentStatusService.create(new ReplicateManagePaymentAttachmentStatusKafka(
                                paymentAttachmentStatusDto.getId(), 
                                paymentAttachmentStatusDto.getCode(), 
                                paymentAttachmentStatusDto.getName(), 
                                paymentAttachmentStatusDto.getStatus().name(), 
                                paymentAttachmentStatusDto.getDefaults(),
                                paymentAttachmentStatusDto.isNonNone(),
                                paymentAttachmentStatusDto.isPatWithAttachment(),
                                paymentAttachmentStatusDto.isPwaWithOutAttachment(),
                                paymentAttachmentStatusDto.isSupported(),
                                paymentAttachmentStatusDto.isOtherSupport()
                        ));
                    }
                }
                case MANEGE_CLIENT -> {
                    for (ManageClientDto clientDto : this.managerClientService.findAllToReplicate()) {
                        this.replicateManageClientService.create(new ReplicateManageClientKafka(clientDto.getId(), clientDto.getCode(), clientDto.getName(), clientDto.getStatus().name(), clientDto.getIsNightType()));
                    }
                }
                case MANAGE_INVOICE_STATUS -> {
                    for (ManageInvoiceStatusDto invoiceStatusDto : this.invoiceStatusService.findAllToReplicate()) {
                        this.replicateManageInvoiceStatusService.create(new ReplicateManageInvoiceStatusKafka(invoiceStatusDto.getId(), invoiceStatusDto.getCode(), invoiceStatusDto.getName(), invoiceStatusDto.getShowClone()));
                    }
                }
                case MANAGE_INVOICE_TRANSACTION_TYPE -> {
                    for (ManageInvoiceTransactionTypeDto invoiceTransactionTypeDto : this.invoiceTransactionTypeService.findAllToReplicate()) {
                        this.replicateManageInvoiceTransactionTypeService.create(new ReplicateManageInvoiceTransactionTypeKafka(invoiceTransactionTypeDto.getId(), invoiceTransactionTypeDto.getCode(), invoiceTransactionTypeDto.getName(), invoiceTransactionTypeDto.isDefaults()));
                    }
                }
                case MANAGE_PAYMENT_STATUS -> {
                    for (ManagerPaymentStatusDto paymentStatusDto : this.paymentStatusService.findAllToReplicate()) {
                        this.replicateManagePaymentStatusService.create(new ReplicateManagePaymentStatusKafka(
                                paymentStatusDto.getId(), 
                                paymentStatusDto.getCode(), 
                                paymentStatusDto.getName(), 
                                paymentStatusDto.getStatus().name(), 
                                paymentStatusDto.getApplied(), 
                                paymentStatusDto.isConfirmed(),
                                paymentStatusDto.isCancelled(),
                                paymentStatusDto.isTransit()
                        ));
                    }
                }
                case MANAGE_PAYMENT_SOURCE -> {
                    for (ManagePaymentSourceDto paymentSourceDto : this.paymentSourceService.findAllToReplicate()) {
                        this.replicateManagePaymentSourceService.create(new ReplicateManagePaymentSourceKafka(paymentSourceDto.getId(), paymentSourceDto.getCode(), paymentSourceDto.getName(), paymentSourceDto.getStatus().name(), paymentSourceDto.getExpense()));
                    }
                }
                case MANAGE_PAYMENT_TRANSACTION_TYPE -> {
                    for (ManagePaymentTransactionTypeDto paymentTransactionTypeDto : this.paymentTransactionTypeService.findAllToReplicate()) {
                        this.replicateManagePaymentTransactionTypeService.create(new ReplicateManagePaymentTransactionTypeKafka(
                                paymentTransactionTypeDto.getId(),
                                paymentTransactionTypeDto.getCode(),
                                paymentTransactionTypeDto.getName(),
                                paymentTransactionTypeDto.getStatus().name(),
                                paymentTransactionTypeDto.getDeposit(),
                                paymentTransactionTypeDto.getApplyDeposit(),
                                paymentTransactionTypeDto.getCash(),
                                paymentTransactionTypeDto.getRemarkRequired(),
                                paymentTransactionTypeDto.getMinNumberOfCharacter(),
                                paymentTransactionTypeDto.getDefaultRemark(),
                                paymentTransactionTypeDto.getDefaults(),
                                paymentTransactionTypeDto.getPaymentInvoice(),
                                paymentTransactionTypeDto.getDebit()
                        ));
                    }
                }

                case MANAGE_HOTEL -> {//
                    for (ManageHotelDto hotelDto : this.manageHotelService.findAllToReplicate()) {
                        this.replicateManageHotelService.create(new ReplicateManageHotelKafka(
                                hotelDto.getId(),
                                hotelDto.getCode(),
                                hotelDto.getName(),
                                hotelDto.getIsApplyByVCC(),
                                hotelDto.getManageTradingCompanies() != null ? hotelDto.getManageTradingCompanies().getId() : null,
                                hotelDto.getStatus().name(),
                                hotelDto.getRequiresFlatRate(),
                                hotelDto.getIsVirtual(),
                                hotelDto.getApplyByTradingCompany(),
                                hotelDto.getAutoApplyCredit(),
                                hotelDto.getCity(),
                                hotelDto.getBabelCode(),
                                hotelDto.getAddress(),
                                hotelDto.getManageCityState().getId(),
                                hotelDto.getManageCountry().getId(),
                                hotelDto.getManageCurrency().getId()
                        ));
                    }
                }
                case MANAGE_B2B_PARTNER -> {
                    for (ManagerB2BPartnerDto managerB2BPartnerDto : this.managerB2BPartnerService.findAllToReplicate()) {
                        this.producerReplicateB2BPartnerService.create(new ReplicateB2BPartnerKafka(
                                managerB2BPartnerDto.getId(),
                                managerB2BPartnerDto.getCode(),
                                managerB2BPartnerDto.getName(),
                                managerB2BPartnerDto.getDescription(),
                                managerB2BPartnerDto.getPassword(),
                                managerB2BPartnerDto.getIp(),
                                managerB2BPartnerDto.getToken(),
                                managerB2BPartnerDto.getUrl(),
                                managerB2BPartnerDto.getUserName(),
                                managerB2BPartnerDto.getStatus().name(),
                                managerB2BPartnerDto.getB2BPartnerTypeDto().getId()
                        ));
                    }
                }
                case MANAGE_MERCHANT -> {
                    for (ManagerMerchantDto managerMerchantDto : this.iManagerMerchantService.findAllToReplicate()) {
                        this.producerReplicateManageMerchantService.create(ReplicateManageMerchantKafka.builder()
                                .id(managerMerchantDto.getId())
                                .code(managerMerchantDto.getCode())
                                .description(managerMerchantDto.getDescription())
                                .b2bPartner(managerMerchantDto.getB2bPartner().getId())
                                .defaultm(managerMerchantDto.getDefaultm())
                                .status(managerMerchantDto.getStatus().name())
                                .build());
                    }
                }
                case MANAGE_MERCHANT_CONFIG -> {
                    for (ManagerMerchantConfigDto managerMerchantConfigDto : this.manageMerchantConfigService.findAllToReplicate()) {
                        this.producerReplicateManageMerchantConfigService.create(ReplicateManagerMerchantConfigKafka.builder()
                                .method(managerMerchantConfigDto.getMethod().name())
                                .name(managerMerchantConfigDto.getName())
                                .declinedUrl(managerMerchantConfigDto.getDeclinedUrl())
                                .altUrl(managerMerchantConfigDto.getAltUrl())
                                .url(managerMerchantConfigDto.getUrl())
                                .errorUrl(managerMerchantConfigDto.getErrorUrl())
                                .institutionCode(managerMerchantConfigDto.getInstitutionCode())
                                .merchantType(managerMerchantConfigDto.getMerchantType())
                                .successUrl(managerMerchantConfigDto.getSuccessUrl())
                                .merchantNumber(managerMerchantConfigDto.getMerchantNumber())
                                .merchantTerminal(managerMerchantConfigDto.getMerchantTerminal())
                                .manageMerchant(managerMerchantConfigDto.getManagerMerchantDto().getId())
                                .id(managerMerchantConfigDto.getId())
                                .build());
                    }
                }
                case MANAGE_COUNTRY -> {
                    for (ManagerCountryDto managerCountryDto : this.managerCountryService.findAllToReplicate()) {
                        this.producerReplicateManageCountryService.create(ReplicateManageCountryKafka.builder()
                                .id(managerCountryDto.getId())
                                .status(managerCountryDto.getStatus().name())
                                .code(managerCountryDto.getCode())
                                .name(managerCountryDto.getName())
                                .description(managerCountryDto.getDescription())
                                .build()
                        );
                    }
                }
                case MANAGE_CITY_STATE -> {
                    for (ManageCityStateDto manageCityStateDto : this.manageCityStateService.findAllToReplicate()) {
                        producerReplicateManageCityStateService.create(ReplicateManageCityStateKafka.builder()
                                .id(manageCityStateDto.getId())
                                .code(manageCityStateDto.getCode())
                                .name(manageCityStateDto.getName())
                                .status(manageCityStateDto.getStatus().name())
                                .country(manageCityStateDto.getCountry().getId())
                                .description(manageCityStateDto.getDescription())
                                .build()
                        );
                    }
                }
                case MANAGE_B2B_PARTNER_TYPE -> {
                    for (ManageB2BPartnerTypeDto manageB2BPartnerTypeDto : this.manageB2BPartnerTypeService.findAllToReplicate()) {
                        producerReplicateB2BPartnerTypeService.create(ReplicateB2BPartnerTypeKafka.builder()
                                .id(manageB2BPartnerTypeDto.getId())
                                .code(manageB2BPartnerTypeDto.getCode())
                                .name(manageB2BPartnerTypeDto.getName())
                                .status(manageB2BPartnerTypeDto.getStatus().name())
                                .description(manageB2BPartnerTypeDto.getDescription())
                                .build()
                        );
                    }
                }
                case MANAGE_TRADING_COMPANY -> {
                    for (ManageTradingCompaniesDto manageTradingCompaniesDto : this.tradingCompaniesService.findAll()) {
                        producerReplicateManageTradingCompanyService.create(new ReplicateManageTradingCompanyKafka(manageTradingCompaniesDto.getId(),
                                manageTradingCompaniesDto.getCode(), manageTradingCompaniesDto.getIsApplyInvoice(),
                                manageTradingCompaniesDto.getCif(), manageTradingCompaniesDto.getAddress(), manageTradingCompaniesDto.getCompany()));
                    }
                }
                case MANAGE_CURRENCY -> {
                    for (ManagerCurrencyDto dto: this.currencyService.findAllToReplicate()){
                        this.producerReplicateManageCurrencyService.create(
                                new ReplicateManageCurrencyKafka(
                                        dto.getId(), dto.getCode(), dto.getName(), dto.getStatus().name()
                                )
                        );
                    }
                } case MANAGE_REGION -> {
                    for (ManageRegionDto dto: this.regionService.findAllToReplicate()){
                        this.producerReplicateManageRegionService.create(new ManageRegionKafka(
                                dto.getId(), dto.getCode(), dto.getName()
                        ));
                    }
                } case MANAGE_AGENCY_CONTACT -> {
                    for (ManageAgencyContactDto dto: this.agencyContactService.findAllToReplicate()){
                        this.producerReplicateManageAgencyContactService.create(new ManageAgencyContactKafka(
                                dto.getId(), dto.getManageAgency().getId(), dto.getManageRegion().getId(),
                                dto.getManageHotel().stream().map(ManageHotelDto::getId).collect(Collectors.toList()),
                                dto.getEmailContact()
                        ));
                    }
                } case MANAGE_CREDIT_CARD_TYPE -> {
                    for (ManageCreditCardTypeDto dto : this.creditCardTypeService.findAllToReplicate()){
                        this.producerReplicateManageCreditCardTypeService.create(new ReplicateManageCreditCardTypeKafka(
                                dto.getId(), dto.getCode(), dto.getName()
                        ));
                    }
                } case MANAGE_MERCHANT_CURRENCY -> {
                    for (ManagerMerchantCurrencyDto dto : this.merchantCurrencyService.findAllToReplicate()){
                        this.producerReplicateManageMerchantCurrencyService.create(new ReplicateManageMerchantCurrencyKafka(
                                dto.getId(), dto.getManagerMerchant().getId(),
                                dto.getManagerCurrency().getId(), dto.getValue(),
                                dto.getDescription(), dto.getStatus().name()
                        ));
                    }
                }
                default ->
                    System.out.println("Número inválido. Por favor, intenta de nuevo con un número del 1 al 7.");
            }
        }
    }
}
