package com.kynsoft.finamer.payment.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.payment.application.query.objectResponse.ManageHotelResponse;
import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.IManageHotelService;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageHotel;
import com.kynsoft.finamer.payment.infrastructure.repository.command.ManageHotelWriteDataJPARepository;
import com.kynsoft.finamer.payment.infrastructure.repository.query.ManageHotelReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManageHotelServiceImpl implements IManageHotelService {

    @Autowired
    private final ManageHotelWriteDataJPARepository repositoryCommand;

    @Autowired
    private final ManageHotelReadDataJPARepository repositoryQuery;

    public ManageHotelServiceImpl(ManageHotelWriteDataJPARepository repositoryCommand, ManageHotelReadDataJPARepository repositoryQuery) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
    }

    @Override
    public UUID create(ManageHotelDto dto) {
        ManageHotel entity = new ManageHotel(dto);

        return repositoryCommand.save(entity).getId();
    }

    @Override
    public void update(ManageHotelDto dto) {
        ManageHotel entity = new ManageHotel(dto);

        entity.setUpdatedAt(LocalDateTime.now());

        repositoryCommand.save(entity);
    }

    @Override
    public void delete(ManageHotelDto dto) {
        ManageHotel delete = new ManageHotel(dto);

        delete.setDeleted(Boolean.TRUE);
        delete.setCode(delete.getCode()+ "-" + UUID.randomUUID());
        delete.setDeletedAt(LocalDateTime.now());

        repositoryCommand.save(delete);
    }

    @Override
    public List<ManageHotelDto> findByIds(List<UUID> ids) {
        return repositoryQuery.findAllById(ids).stream().map(ManageHotel::toAggregate).toList();
    }

    @Override
    public Map<UUID, ManageHotelDto> getMapById(List<UUID> ids) {
        if(Objects.isNull(ids)){
            throw new IllegalArgumentException("The Hotel ID list must not be null");
        }
        return this.findByIds(ids).stream().collect(Collectors.toMap(ManageHotelDto::getId, manageHotelDto -> manageHotelDto));
    }

    @Override
    public ManageHotelDto findById(UUID id) {
        Optional<ManageHotel> optionalEntity = repositoryQuery.findById(id);

        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_HOTEL_NOT_FOUND, new ErrorField("id", DomainErrorMessage.MANAGE_HOTEL_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<ManageHotel> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageHotel> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public boolean existByCode(String hotelCode) {
        return repositoryQuery.existsByCode( hotelCode);
    }

    @Override
    public ManageHotelDto findByCode(String hotelCode) {
        return repositoryQuery.findByCode(hotelCode).map(ManageHotel::toAggregate)
                .orElseThrow(()->new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_HOTEL_NOT_FOUND,
                        new ErrorField("code", DomainErrorMessage.MANAGE_HOTEL_NOT_FOUND.getReasonPhrase()))));
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

    private PaginatedResponse getPaginatedResponse(Page<ManageHotel> data) {
        List<ManageHotelResponse> responses = new ArrayList<>();
        for (ManageHotel p : data.getContent()) {
            responses.add(new ManageHotelResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

}
