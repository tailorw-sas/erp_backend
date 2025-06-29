package com.kynsoft.finamer.invoicing.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessException;
import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceTypeDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.services.IManageInvoiceTypeService;
import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageInvoiceType;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.ManageInvoiceTypeWriteDataJPARepository;
import com.kynsoft.finamer.invoicing.infrastructure.repository.query.ManageInvoiceTypeReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManageInvoiceTypeServiceImpl implements IManageInvoiceTypeService {

    @Autowired
    private final ManageInvoiceTypeWriteDataJPARepository repositoryCommand;

    @Autowired
    private final ManageInvoiceTypeReadDataJPARepository repositoryQuery;

    public ManageInvoiceTypeServiceImpl(ManageInvoiceTypeWriteDataJPARepository repositoryCommand, ManageInvoiceTypeReadDataJPARepository repositoryQuery) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
    }

    @Override
    public UUID create(ManageInvoiceTypeDto dto) {
        ManageInvoiceType entity = new ManageInvoiceType(dto);

        return repositoryCommand.save(entity).getId();
    }

    @Override
    public void update(ManageInvoiceTypeDto dto) {
        ManageInvoiceType entity = new ManageInvoiceType(dto);

        entity.setUpdatedAt(LocalDateTime.now());

        repositoryCommand.save(entity);
    }

    @Override
    public void delete(ManageInvoiceTypeDto dto) {
        ManageInvoiceType delete = new ManageInvoiceType(dto);

        delete.setDeleted(Boolean.TRUE);
//        delete.setCode(delete.getCode()+ "-" + UUID.randomUUID());
        delete.setDeletedAt(LocalDateTime.now());

        repositoryCommand.save(delete);
    }

    @Override
    public ManageInvoiceTypeDto findById(UUID id) {
        Optional<ManageInvoiceType> optionalEntity = repositoryQuery.findById(id);

        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_INVOICE_TYPE_NOT_FOUND, new ErrorField("id", "The source not found.")));
    }



    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }

    @Override
    public ManageInvoiceTypeDto findByEInvoiceType(EInvoiceType invoiceType) {
        return switch (invoiceType) {
            case CREDIT, OLD_CREDIT -> this.repositoryQuery.findByCredit()
                    .map(ManageInvoiceType::toAggregate)
                    .orElseThrow(() -> new BusinessException(
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND,
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND.getReasonPhrase()
                    ));
            case INVOICE -> this.repositoryQuery.findByInvoice()
                    .map(ManageInvoiceType::toAggregate)
                    .orElseThrow(() -> new BusinessException(
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND,
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND.getReasonPhrase()
                    ));
            case INCOME -> this.repositoryQuery.findByIncome()
                    .map(ManageInvoiceType::toAggregate)
                    .orElseThrow(() -> new BusinessException(
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND,
                            DomainErrorMessage.INVOICE_TYPE_NOT_FOUND.getReasonPhrase()
                    ));
        };
    }

    @Override
    public ManageInvoiceTypeDto findByCode(String code) {
        return this.repositoryQuery.findByCode(code).map(ManageInvoiceType::toAggregate).orElse(null);
    }

    @Override
    public ManageInvoiceTypeDto findByIncome() {
        return this.repositoryQuery.findByIncome().map(ManageInvoiceType::toAggregate).orElse(null);
    }

    @Override
    public ManageInvoiceTypeDto findByCredit() {
        return this.repositoryQuery.findByCredit().map(ManageInvoiceType::toAggregate).orElse(null);
    }

    @Override
    public ManageInvoiceTypeDto findByInvoice() {
        return this.repositoryQuery.findByInvoice().map(ManageInvoiceType::toAggregate).orElse(null);
    }

    @Override
    public List<ManageInvoiceTypeDto> findByIds(List<UUID> ids) {
        if(Objects.isNull(ids) || ids.isEmpty()){
            throw new IllegalArgumentException("The invoice type ID list must not be null or empty");
        }

        return this.repositoryQuery.findByIdIn(ids).stream()
                .map(ManageInvoiceType::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public Map<UUID, ManageInvoiceTypeDto> getMapById(List<UUID> invoiceTypeIds) {
        if(Objects.isNull(invoiceTypeIds) || invoiceTypeIds.isEmpty()){
            throw new IllegalArgumentException("The invoice type ID list must not be null or empty");
        }

        return this.findByIds(invoiceTypeIds).stream()
                .collect(Collectors.toMap(ManageInvoiceTypeDto::getId, invoiceTypeDto -> invoiceTypeDto));
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


}
