package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.application.mailjet.*;
import com.kynsof.share.core.domain.EMailjetType;
import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsof.share.core.infrastructure.specifications.LogicalOperation;
import com.kynsof.share.core.infrastructure.specifications.SearchOperation;
import com.kynsof.share.utils.BankerRounding;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.TransactionSearchResponse;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.TransactionTotalResume;
import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.ETransactionStatus;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.MethodType;
import com.kynsoft.finamer.creditcard.domain.services.*;
import com.kynsoft.finamer.creditcard.infrastructure.identity.Transaction;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.TransactionWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.ManageEmployeeReadDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.TransactionReadDataJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionWriteDataJPARepository repositoryCommand;

    private final TransactionReadDataJPARepository repositoryQuery;

    private final MailService mailService;

    private final TemplateEntityServiceImpl templateEntityService;

    private final IManageTransactionStatusService transactionStatusService;

    private final ITransactionStatusHistoryService transactionStatusHistoryService;

    private final IParameterizationService parameterizationService;

    private final IMerchantLanguageCodeService merchantLanguageCodeService;
    private final ManageEmployeeReadDataJPARepository employeeReadDataJPARepository;

    public TransactionServiceImpl(TransactionWriteDataJPARepository repositoryCommand,
                                  TransactionReadDataJPARepository repositoryQuery,
                                  MailService mailService,
                                  TemplateEntityServiceImpl templateEntityService,
                                  IManageTransactionStatusService transactionStatusService,
                                  ITransactionStatusHistoryService transactionStatusHistoryService,
                                  IParameterizationService parameterizationService,
                                  IMerchantLanguageCodeService merchantLanguageCodeService,
                                  ManageEmployeeReadDataJPARepository employeeReadDataJPARepository) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.mailService = mailService;
        this.templateEntityService = templateEntityService;
        this.transactionStatusService = transactionStatusService;
        this.transactionStatusHistoryService = transactionStatusHistoryService;
        this.parameterizationService = parameterizationService;
        this.merchantLanguageCodeService = merchantLanguageCodeService;
        this.employeeReadDataJPARepository = employeeReadDataJPARepository;
    }

    @Override
    public TransactionDto create(TransactionDto dto) {
        Transaction entity = new Transaction(dto);
        return this.repositoryCommand.save(entity).toAggregate();
    }

    @Override
    public void update(TransactionDto dto) {
        Transaction entity = new Transaction(dto);
        entity.setUpdateAt(LocalDateTime.now());

        this.repositoryCommand.save(entity);
    }

    @Override
    public void delete(TransactionDto dto) {
        try {
            List<TransactionStatusHistoryDto> histories = this.transactionStatusHistoryService.findByTransactionId(dto.getId());
            histories.forEach(history -> {
                history.setTransaction(null);
                this.transactionStatusHistoryService.delete(history.getId());
            });
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE,
                    new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public TransactionDto findById(Long id) {
        Optional<Transaction> entity = this.repositoryQuery.findById(id);
        if (entity.isPresent()) {
            return entity.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND,
                new ErrorField("id", DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND.getReasonPhrase())));
    }
    @Override
    public TransactionDto findByUuid(UUID uuid) {
        Optional<Transaction> entity = this.repositoryQuery.findByTransactionUuid(uuid);
        if (entity.isPresent()) {
            return entity.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND,
                new ErrorField("id", DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria, UUID employeeId) {
        filterCriteria(filterCriteria);

        List<UUID> agencyIds = this.employeeReadDataJPARepository.findAgencyIdsByEmployeeId(employeeId);
        FilterCriteria fcAgency = new FilterCriteria();
        fcAgency.setKey("agency.id");
        fcAgency.setLogicalOperation(LogicalOperation.AND);
        fcAgency.setOperator(SearchOperation.IN);
        fcAgency.setValue(agencyIds);
        filterCriteria.add(fcAgency);

        List<UUID> hotelIds = this.employeeReadDataJPARepository.findHotelsIdsByEmployeeId(employeeId);
        FilterCriteria fcHotel = new FilterCriteria();
        fcHotel.setKey("hotel.id");
        fcHotel.setLogicalOperation(LogicalOperation.AND);
        fcHotel.setOperator(SearchOperation.IN);
        fcHotel.setValue(hotelIds);
        filterCriteria.add(fcHotel);

        GenericSpecificationsBuilder<Transaction> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<Transaction> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedSearchResponse(data);
    }

    @Override
    public TransactionTotalResume searchTotal(List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);
        GenericSpecificationsBuilder<Transaction> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        double totalAmount = 0.0;
        double commission = 0.0;
        double netAmount = 0.0;

        ParameterizationDto parameterizationDto = this.parameterizationService.findActiveParameterization();

        //si no encuentra la parametrization que agarre 2 decimales por defecto
        int decimals = parameterizationDto != null ? parameterizationDto.getDecimals() : 2;

        return new TransactionTotalResume(BankerRounding.round(totalAmount, decimals),
                BankerRounding.round(commission, decimals), BankerRounding.round(netAmount, decimals));
    }

    @Override
    @Transactional
    public Set<TransactionDto> changeAllTransactionStatus(Set<Long> transactionIds, ETransactionStatus status, UUID employeeId) {
        Set<TransactionDto> transactionsDto = new HashSet<>();
        for (Long transactionId : transactionIds) {
            TransactionDto transactionDto = this.findById(transactionId);
            ManageTransactionStatusDto transactionStatusDto = this.transactionStatusService.findByETransactionStatus(status);
            transactionDto.setStatus(transactionStatusDto);
            this.update(transactionDto);
            this.transactionStatusHistoryService.create(transactionDto, employeeId);
            transactionsDto.add(transactionDto);
        }
        return transactionsDto;
    }

    @Override
    public Long countByReservationNumberAndManageHotelIdAndNotId(String reservationNumber, UUID hotel) {
        return this.repositoryQuery.countByReservationNumberAndManageHotelIdAndNotId(reservationNumber, hotel);
    }

    @Override
    public boolean compareParentTransactionAmount(Long parentId, Double amount) {
        Optional<Transaction> parentTransaction = this.repositoryQuery.findById(parentId);
        if (parentTransaction.isPresent()) {
            double parentAmount = parentTransaction.get().getAmount();
            Optional<Double> sumOfChildrenAmount = this.repositoryQuery.findSumOfAmountByParentId(parentId);
            return (sumOfChildrenAmount.orElse(0.0) + amount) > parentAmount;
        } else {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND,
                    new ErrorField("id", DomainErrorMessage.VCC_TRANSACTION_NOT_FOUND.getReasonPhrase())));
        }
    }

    @Override
    public Double findSumOfAmountByParentId(Long parentId) {
        return this.repositoryQuery.findSumOfAmountByParentId(parentId).orElse(0.0);
    }

    private void filterCriteria(List<FilterCriteria> filterCriteria) {
        for (FilterCriteria filter : filterCriteria) {

            if ("methodType".equals(filter.getKey()) && filter.getValue() instanceof String) {
                try {
                    MethodType enumValue = MethodType.valueOf((String) filter.getValue());
                    filter.setValue(enumValue);
                } catch (IllegalArgumentException e) {
                    System.err.println("Valor inválido para el tipo Enum MethodType: " + filter.getValue());
                }
            }
        }
    }

    private PaginatedResponse getPaginatedSearchResponse(Page<Transaction> data) {
        List<TransactionSearchResponse> responseList = new ArrayList<>();
        for (Transaction entity : data.getContent()) {
            responseList.add(new TransactionSearchResponse(entity.toAggregate(), entity.getHasAttachments()));
        }
        return new PaginatedResponse(responseList, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    //Conformar el correo para confirmar que la transaccion fue Recivida
    @Override
    public void sendTransactionConfirmationVoucherEmail(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto, String responseCodeMessage, byte[] attachment) {
        if(transactionDto.getEmail() != null){
            TemplateDto templateDto = templateEntityService.findByLanguageCodeAndType(transactionDto.getLanguage().getCode(), EMailjetType.PAYMENT_CONFIRMATION_VOUCHER);
            SendMailJetEMailRequest request = new SendMailJetEMailRequest();
            request.setTemplateId(Integer.parseInt(templateDto.getTemplateCode()));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String transactionDateStr = transactionDto.getPaymentDate() != null ? transactionDto.getPaymentDate().format(formatter) : "";

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            String formattedAmount = currencyFormatter.format(transactionDto.getAmount());

            // Variables para el template de email, cambiar cuando keimer genere la plantilla
            List<MailJetVar> vars = Arrays.asList(
                    new MailJetVar("commerce", merchantConfigDto.getName()),
                    new MailJetVar("merchant", transactionDto.getMerchant().getDescription()),
                    new MailJetVar("hotel", transactionDto.getHotel().getName()),
                    new MailJetVar("number_id", transactionDto.getId()),
                    new MailJetVar("transaction_type", "Sale"),
                    new MailJetVar("status", transactionDto.getStatus().getName()),
                    new MailJetVar("response_code", responseCodeMessage),
                    new MailJetVar("payment_date", transactionDateStr),
                    new MailJetVar("authorization_number", "N/A"),
                    new MailJetVar("card_type", transactionDto.getCreditCardType().getName()),
                    new MailJetVar("card_number", transactionDto.getCardNumber()),
                    new MailJetVar("amount_usd", formattedAmount),
                    new MailJetVar("ibtis_usd", "$0.00"),
                    new MailJetVar("reference", transactionDto.getReferenceNumber()),
                    new MailJetVar("user", transactionDto.getGuestName()),
                    new MailJetVar("modality", transactionDto.getMethodType().name())
            );
            request.setMailJetVars(vars);

            // All Recipients
            List<MailJetRecipient> recipients = new ArrayList<>();
            if (transactionDto.getEmail() != null && !transactionDto.getEmail().isEmpty()) {
                String[] emails = transactionDto.getEmail().split(";");
                for (String email : emails) {
                    recipients.add(new MailJetRecipient(email, transactionDto.getGuestName()));
                }
            }
            if (transactionDto.getHotelContactEmail() != null && !transactionDto.getHotelContactEmail().isEmpty()) {
                String[] emails = transactionDto.getHotelContactEmail().split(";");
                for (String email : emails) {
                    recipients.add(new MailJetRecipient(email, transactionDto.getGuestName()));
                }
            }
            request.setRecipientEmail(recipients);

            if (attachment != null) {
                //creando el attachment para adjuntarlo al correo
                List<MailJetAttachment> list = new ArrayList<>();
                MailJetAttachment attach = new MailJetAttachment(
                        "application/pdf",
                        "Voucher_" + transactionDto.getId() + ".pdf",
                        Base64.getEncoder().encodeToString(attachment)
                );
                list.add(attach);
                request.setMailJetAttachments(list);
            }

            mailService.sendMail(request);
        }
    }

    @Override
    public void sendTransactionPaymentLinkEmail(TransactionDto transactionDto, String paymentLink) {
        TemplateDto templateDto = templateEntityService.findByLanguageCodeAndType(transactionDto.getLanguage().getCode(), EMailjetType.PAYMENT_LINK);
        SendMailJetEMailRequest request = new SendMailJetEMailRequest();
        request.setTemplateId(Integer.parseInt(templateDto.getTemplateCode())); // Cambiar en configuración
        //todo: validar que el formato de la fecha se en dependencia del campo transactionDto.getLanguage()
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy", getLocaleFromLanguageCode(transactionDto));

        // Variables para el template de email
        List<MailJetVar> vars = Arrays.asList(
                new MailJetVar("payment_link", paymentLink),
                new MailJetVar("transaction_id", transactionDto.getId().toString()),
                new MailJetVar("transaction_date", transactionDto.getTransactionDate().format(formatter)),
                new MailJetVar("hotel", transactionDto.getHotel().getName()),
                new MailJetVar("hotel_address", transactionDto.getHotel().getAddress()),
                new MailJetVar("hotel_contact", transactionDto.getHotelContactEmail()),
                new MailJetVar("invoice_amount", transactionDto.getAmount().toString()),
                new MailJetVar("reference_number", transactionDto.getReferenceNumber())
        );
        request.setMailJetVars(vars);

        // Recipients
        List<MailJetRecipient> recipients = new ArrayList<>();
        if (transactionDto.getEmail() != null && !transactionDto.getEmail().isEmpty()) {
            String[] emails = transactionDto.getEmail().split(";");
            for (String email : emails) {
                recipients.add(new MailJetRecipient(email, transactionDto.getGuestName()));
            }
        }

        // Add hotel contact recipient if exists
        if (transactionDto.getHotelContactEmail() != null && !transactionDto.getHotelContactEmail().isEmpty()) {
            String[] emails = transactionDto.getHotelContactEmail().split(";");
            for (String email : emails) {
                recipients.add(new MailJetRecipient(email, transactionDto.getGuestName()));
            }
        }
        request.setRecipientEmail(recipients);
        mailService.sendMail(request);
    }

    private Locale getLocaleFromLanguageCode(TransactionDto transactionDto){
        if (transactionDto.getMerchant() != null && transactionDto.getLanguage() != null) {
            String languageCode = this.merchantLanguageCodeService.findMerchantLanguageByMerchantIdAndLanguageId(transactionDto.getMerchant().getId(), transactionDto.getLanguage().getId());
            return (languageCode.equalsIgnoreCase("es") || languageCode.equalsIgnoreCase("esp")) ? new Locale("es", "ES") : Locale.US;
        }
        return Locale.US;
    }
}
