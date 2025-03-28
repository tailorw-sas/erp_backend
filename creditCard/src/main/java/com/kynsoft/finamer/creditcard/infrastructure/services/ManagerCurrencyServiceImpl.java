package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.ManagerCurrencyResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManagerCurrencyDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import com.kynsoft.finamer.creditcard.domain.services.IManagerCurrencyService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.ManagerCurrency;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.ManagerCurrencyWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.ManagerCurrencyReadDataJPARepository;
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
public class ManagerCurrencyServiceImpl implements IManagerCurrencyService {

    @Autowired
    private ManagerCurrencyWriteDataJPARepository repositoryCommand;

    @Autowired
    private ManagerCurrencyReadDataJPARepository repositoryQuery;

    @Override
    public UUID create(ManagerCurrencyDto dto) {
        ManagerCurrency data = new ManagerCurrency(dto);
        return this.repositoryCommand.save(data).getId();
    }

    @Override
    public void update(ManagerCurrencyDto dto) {
        ManagerCurrency update = new ManagerCurrency(dto);

        update.setUpdateAt(LocalDateTime.now());

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(ManagerCurrencyDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public ManagerCurrencyDto findById(UUID id) {
        Optional<ManagerCurrency> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGER_CURRENCY_NOT_FOUND, new ErrorField("id", "Element cannot be deleted has a related element.")));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<ManagerCurrency> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManagerCurrency> data = this.repositoryQuery.findAll(specifications, pageable);

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

    private PaginatedResponse getPaginatedResponse(Page<ManagerCurrency> data) {
        List<ManagerCurrencyResponse> userSystemsResponses = new ArrayList<>();
        for (ManagerCurrency p : data.getContent()) {
            userSystemsResponses.add(new ManagerCurrencyResponse(p.toAggregate()));
        }
        return new PaginatedResponse(userSystemsResponses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }

    @Override
    public List<ManagerCurrencyDto> findAllToReplicate() {
        List<ManagerCurrency> objects = this.repositoryQuery.findAll();
        List<ManagerCurrencyDto> dtos = new ArrayList<>();

        for(ManagerCurrency object : objects){
            dtos.add(object.toAggregate());
        }

        return dtos;
    }

}
