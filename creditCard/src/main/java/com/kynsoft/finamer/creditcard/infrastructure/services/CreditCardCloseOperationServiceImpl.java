package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsof.share.core.infrastructure.util.DateUtil;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.CreditCardCloseOperationResponse;
import com.kynsoft.finamer.creditcard.domain.dto.CreditCardCloseOperationDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Status;
import com.kynsoft.finamer.creditcard.domain.services.ICreditCardCloseOperationService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.CreditCardCloseOperation;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.CreditCardCloseOperationWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.CreditCardCloseOperationReadDataJPARepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CreditCardCloseOperationServiceImpl implements ICreditCardCloseOperationService {

    @Autowired
    private CreditCardCloseOperationWriteDataJPARepository repositoryCommand;

    @Autowired
    private CreditCardCloseOperationReadDataJPARepository repositoryQuery;

    @Override
    public UUID create(CreditCardCloseOperationDto dto) {
        CreditCardCloseOperation data = new CreditCardCloseOperation(dto);
        return this.repositoryCommand.save(data).getId();
    }

    @Override
    public void update(CreditCardCloseOperationDto dto) {
        CreditCardCloseOperation update = new CreditCardCloseOperation(dto);

        update.setUpdatedAt(LocalDateTime.now());

        this.repositoryCommand.save(update);
    }

    @Override
    public void updateAll(List<CreditCardCloseOperationDto> dtos) {
        List<CreditCardCloseOperation> updates = new ArrayList<>();

        for (CreditCardCloseOperationDto dto : dtos) {
            CreditCardCloseOperation up = new CreditCardCloseOperation(dto);
            up.setUpdatedAt(LocalDateTime.now());
            updates.add(up);
        }

        this.repositoryCommand.saveAll(updates);
    }

    @Override
    public void delete(CreditCardCloseOperationDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public CreditCardCloseOperationDto findById(UUID id) {
        Optional<CreditCardCloseOperation> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.VCC_CLOSE_OPERATION_NOT_FOUND, new ErrorField("id", DomainErrorMessage.VCC_CLOSE_OPERATION_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<CreditCardCloseOperation> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<CreditCardCloseOperation> data = this.repositoryQuery.findAll(specifications, pageable);

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

    private PaginatedResponse getPaginatedResponse(Page<CreditCardCloseOperation> data) {
        List<CreditCardCloseOperationResponse> responses = new ArrayList<>();
        for (CreditCardCloseOperation p : data.getContent()) {
            responses.add(new CreditCardCloseOperationResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public Long findByHotelId(UUID hotelId) {
        return this.repositoryQuery.findByHotelId(hotelId);
    }

    @Override
    public List<CreditCardCloseOperationDto> findByHotelIds(List<UUID> hotelIds) {
        return this.repositoryQuery.findByHotelIds(hotelIds)
                .stream()
                .map(CreditCardCloseOperation::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public CreditCardCloseOperationDto findActiveByHotelId(UUID hotelId) {
        Optional<CreditCardCloseOperation> entity = this.repositoryQuery.findActiveByHotelId(hotelId);
        if (entity.isPresent()) {
            return entity.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.VCC_CLOSE_OPERATION_NOT_FOUND, new ErrorField("id", DomainErrorMessage.VCC_CLOSE_OPERATION_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public LocalDateTime hotelCloseOperationDateTime(UUID hotelId) {
        CreditCardCloseOperationDto dto = findActiveByHotelId(hotelId);

        if (DateUtil.getDateForCloseOperation(dto.getBeginDate(), dto.getEndDate())) {
            return LocalDateTime.now(ZoneId.of("UTC"));
        }
        return LocalDateTime.of(dto.getEndDate(), LocalTime.now(ZoneId.of("UTC")));
    }

}
