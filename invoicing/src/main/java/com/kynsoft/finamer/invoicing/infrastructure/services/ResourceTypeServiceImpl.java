package com.kynsoft.finamer.invoicing.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.invoicing.application.query.resourceType.search.GetSearchResourceTypeResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ResourceTypeDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.services.IManageResourceTypeService;
import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageResourceType;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.ManageResourceTypeWriteDataJPARepository;
import com.kynsoft.finamer.invoicing.infrastructure.repository.query.ResourceTypeReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceTypeServiceImpl implements IManageResourceTypeService {

    @Autowired
    private ManageResourceTypeWriteDataJPARepository repositoryCommand;

    @Autowired
    private ResourceTypeReadDataJPARepository repositoryQuery;

    @Override
    public UUID create(ResourceTypeDto dto) {
        ManageResourceType data = new ManageResourceType(dto);
        return this.repositoryCommand.save(data).getId();
    }

    @Override
    public void update(ResourceTypeDto dto) {
        ManageResourceType update = new ManageResourceType(dto);

        update.setUpdatedAt(LocalDateTime.now());

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(ResourceTypeDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public ResourceTypeDto findById(UUID id) {
        Optional<ManageResourceType> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.RESOURCE_TYPE_NOT_FOUND, new ErrorField("id", DomainErrorMessage.RESOURCE_TYPE_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public ResourceTypeDto findByCode(String code) {
        Optional<ManageResourceType> userSystem = this.repositoryQuery.findResourceTypeByCode(code);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.RESOURCE_TYPE_NOT_FOUND, new ErrorField("code", DomainErrorMessage.RESOURCE_TYPE_NOT_FOUND.getReasonPhrase())));
    }


    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return this.repositoryQuery.countByCodeAndNotId(code, id);
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);
        GenericSpecificationsBuilder<ManageResourceType> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageResourceType> data = repositoryQuery.findAll(specifications, pageable);
        return getPaginatedResponse(data);
    }

    @Override
    public Long countByDefaultsAndNotId(UUID id) {
        return this.repositoryQuery.countByDefaultsAndNotId(id);
    }

    @Override
    public ResourceTypeDto findByDefaults() {
        return this.repositoryQuery.findByDefaults().map(ManageResourceType::toAggregate).orElse(null);
    }

    @Override
    public Map<UUID, ResourceTypeDto> getMapById(List<UUID> ids) {
        if(Objects.isNull(ids)){
            throw new IllegalArgumentException("The ReourceType ID list must not be null");
        }

        return this.repositoryQuery.findByIdIn(ids).stream().collect(Collectors.toMap(ManageResourceType::getId, ManageResourceType::toAggregate));
    }

    private PaginatedResponse getPaginatedResponse(Page<ManageResourceType> data) {
        List<GetSearchResourceTypeResponse> responses = data.stream()
                .map(resourceType -> new GetSearchResourceTypeResponse(resourceType.toAggregate())
                ).toList();
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(), data.getTotalElements(), data.getSize(), data.getNumber());
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
