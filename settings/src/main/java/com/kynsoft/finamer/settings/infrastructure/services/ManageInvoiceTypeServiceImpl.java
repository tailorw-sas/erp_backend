package com.kynsoft.finamer.settings.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.settings.application.query.objectResponse.ManageInvoiceTypeResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageInvoiceTypeDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.services.IManageInvoiceTypeService;
import com.kynsoft.finamer.settings.infrastructure.identity.ManageInvoiceType;
import com.kynsoft.finamer.settings.infrastructure.repository.command.ManageInvoiceTypeWriteDataJPARepository;
import com.kynsoft.finamer.settings.infrastructure.repository.query.ManageInvoiceTypeReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        try{
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e){
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
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
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<ManageInvoiceType> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageInvoiceType> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
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

    private PaginatedResponse getPaginatedResponse(Page<ManageInvoiceType> data) {
        List<ManageInvoiceTypeResponse> responseList = new ArrayList<>();
        for (ManageInvoiceType entity : data.getContent()) {
            responseList.add(new ManageInvoiceTypeResponse(entity.toAggregate()));
        }
        return new PaginatedResponse(responseList, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public List<ManageInvoiceTypeDto> findAllToReplicate() {
        List<ManageInvoiceType> objects = this.repositoryQuery.findAll();
        List<ManageInvoiceTypeDto> objectDtos = new ArrayList<>();

        for (ManageInvoiceType object : objects) {
            objectDtos.add(object.toAggregate());
        }

        return objectDtos;
    }

    @Override
    public Long countByCreditAndNotId(UUID id) {
        return this.repositoryQuery.countByCreditAndNotId(id);
    }

    @Override
    public Long countByInvoiceAndNotId(UUID id) {
        return this.repositoryQuery.countByInvoiceAndNotId(id);
    }

    @Override
    public Long countByIncomeAndNotId(UUID id) {
        return this.repositoryQuery.countByIncomeAndNotId(id);
    }
}
