package com.kynsoft.finamer.payment.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManagePaymentTransactionTypeResponse;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.payment.infrastructure.identity.ManagePaymentTransactionType;
import com.kynsoft.finamer.payment.infrastructure.repository.command.ManagePaymentTransactionTypeWriteDataJPARepository;
import com.kynsoft.finamer.payment.infrastructure.repository.query.ManagePaymentTransactionTypeReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Service
public class ManagePaymentTransactionTypeServiceImpl implements IManagePaymentTransactionTypeService {

    @Autowired
    private ManagePaymentTransactionTypeReadDataJPARepository repositoryQuery;

    @Autowired
    private ManagePaymentTransactionTypeWriteDataJPARepository repositoryCommand;

    @Override
    public UUID create(ManagePaymentTransactionTypeDto dto) {
        ManagePaymentTransactionType entity = new ManagePaymentTransactionType(dto);
        ManagePaymentTransactionType saved = repositoryCommand.save(entity);

        return saved.getId();
    }

    @Override
    public void update(ManagePaymentTransactionTypeDto dto) {
        repositoryCommand.save(new ManagePaymentTransactionType(dto));
    }

    @Override
    public void delete(ManagePaymentTransactionTypeDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public ManagePaymentTransactionTypeDto findById(UUID id) {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findById(id);
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    @Cacheable(cacheNames = "paymentTransactionType", key = "#code", unless = "#result == null")
    public ManagePaymentTransactionTypeDto findByCode(String code) {
        return repositoryQuery.findByCode(code).map(ManagePaymentTransactionType::toAggregate).orElse(null);
    }

    @CacheEvict(allEntries = true, value = "paymentTransactionType")
    @Override
    public void clearCache() {
        System.out.println("Clearing paymentTransactionType cache");
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<ManagePaymentTransactionType> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManagePaymentTransactionType> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    private void filterCriteria(List<FilterCriteria> filterCriteria) {
        for (FilterCriteria filter : filterCriteria) {

            if ("status".equals(filter.getKey()) && filter.getValue() instanceof String) {
                try {
                    Status enumValue = Status.valueOf((String) filter.getValue());
                    filter.setValue(enumValue);
                } catch (IllegalArgumentException e) {
                    System.err.println("Valor inválido para el tipo Enum Status: " + filter.getValue());
                }
            }
        }
    }

    private PaginatedResponse getPaginatedResponse(Page<ManagePaymentTransactionType> data) {
        List<ManagePaymentTransactionTypeResponse> responses = new ArrayList<>();
        for (ManagePaymentTransactionType p : data.getContent()) {
            responses.add(new ManagePaymentTransactionTypeResponse(p.toAggregate()));
        }

        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public ManagePaymentTransactionTypeDto findByPaymentInvoice() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByPaymentInvoice();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ManagePaymentTransactionType findByPaymentInvoiceEntityGraph() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByPaymentInvoiceEntityGraph();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ManagePaymentTransactionType findByApplyDepositEntityGraph() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByApplyDepositEntityGraph();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    @Cacheable(value = "paymentTransactionTypeCache", unless = "#result == null")
    public ManagePaymentTransactionTypeDto findByPaymentInvoiceCacheable() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByPaymentInvoice();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ManagePaymentTransactionTypeDto findByDeposit() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByDeposit();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ManagePaymentTransactionTypeDto findByApplyDeposit() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByApplyDeposit();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ManagePaymentTransactionTypeDto findByApplyDepositAndDefaults() {
        Optional<ManagePaymentTransactionType> optionalEntity = repositoryQuery.findByApplyDepositAndDefaults();
        if (optionalEntity.isPresent()) {
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_PAYMENT_TRANSACTION_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public List<ManagePaymentTransactionTypeDto> findByCodesAndPaymentInvoice(List<String> codes) {
        if(Objects.nonNull(codes)){
            return repositoryQuery.findByCodeInOrPaymentInvoiceTrue(codes).stream()
                    .map(ManagePaymentTransactionType::toAggregate)
                    .toList();
        }
        throw new IllegalArgumentException("ManagePaymentTransactionType codes must not be null.");
    }

}
