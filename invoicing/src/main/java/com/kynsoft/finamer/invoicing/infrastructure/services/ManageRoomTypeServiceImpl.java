package com.kynsoft.finamer.invoicing.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.ManageRoomTypeResponse;
import com.kynsoft.finamer.invoicing.domain.dto.ManageRoomTypeDto;
import com.kynsoft.finamer.invoicing.domain.services.IManageRoomTypeService;
import com.kynsoft.finamer.invoicing.infrastructure.identity.ManageRoomType;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.ManageRoomTypeWriteDataJPARepository;
import com.kynsoft.finamer.invoicing.infrastructure.repository.query.ManageRoomTypeReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ManageRoomTypeServiceImpl implements IManageRoomTypeService {

    @Autowired
    private final ManageRoomTypeWriteDataJPARepository repositoryCommand;

    @Autowired
    private final ManageRoomTypeReadDataJPARepository repositoryQuery;

    public ManageRoomTypeServiceImpl(ManageRoomTypeWriteDataJPARepository repositoryCommand, ManageRoomTypeReadDataJPARepository repositoryQuery) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
    }

    @Override
    public UUID create(ManageRoomTypeDto dto) {
        ManageRoomType entity = new ManageRoomType(dto);

        return repositoryCommand.save(entity).getId();
    }

    @Override
    public void update(ManageRoomTypeDto dto) {
        ManageRoomType entity = new ManageRoomType(dto);

        entity.setUpdatedAt(LocalDateTime.now());

        repositoryCommand.save(entity);
    }

    @Override
    public void delete(ManageRoomTypeDto dto) {
        ManageRoomType delete = new ManageRoomType(dto);

        delete.setDeleted(Boolean.TRUE);
        delete.setCode(delete.getCode()+ "-" + UUID.randomUUID());

        delete.setDeletedAt(LocalDateTime.now());

        repositoryCommand.save(delete);
    }

    @Override
    public ManageRoomTypeDto findById(UUID id) {
        Optional<ManageRoomType> optionalEntity = repositoryQuery.findById(id);

        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_FOUND, new ErrorField("id", "Room type not found.")));
    }

    @Override
    @Cacheable(cacheNames = "manageRoomType", key = "#code", unless = "#result == null")
    public ManageRoomTypeDto findByCode(String code) {
        Optional<ManageRoomType> optionalEntity = repositoryQuery.findManageRoomTypeByCode(code);

        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_FOUND, new ErrorField("code", "Room type not found.")));
    }

    @Override
    @Cacheable(cacheNames = "manageRoomType", key = "#codes", unless = "#result == null || #result.isEmpty()")
    public List<ManageRoomTypeDto> findByCodes(List<String> codes) {
        List<ManageRoomType> entities = repositoryQuery.findManageRoomTypesByCodes(codes);

        if (entities.isEmpty()) {
            throw new BusinessNotFoundException(new GlobalBusinessException(
                    DomainErrorMessage.NOT_FOUND,
                    new ErrorField("codes", "Room types not found.")));
        }

        return entities.stream().map(ManageRoomType::toAggregate).collect(Collectors.toList());
    }

    @Override
    public boolean existByCode(String code) {
        return repositoryQuery.existsManageRoomTypeByCode(code);
    }


    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }

    @Override
    public Long countByCodeAndManageHotelIdAndNotId(String code, UUID manageHotelId, UUID id) {
        return repositoryQuery.countByCodeAndManageHotelIdAndNotId(code,  id);
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<ManageRoomType> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageRoomType> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    private PaginatedResponse getPaginatedResponse(Page<ManageRoomType> data) {
        List<ManageRoomTypeResponse> responseList = new ArrayList<>();
        for (ManageRoomType entity : data.getContent()) {
            responseList.add(new ManageRoomTypeResponse(entity.toAggregate()));
        }
        return new PaginatedResponse(responseList, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    @Cacheable(cacheNames = "manageHotel", key = "#code", unless = "#result == null")
    public ManageRoomTypeDto findManageRoomTypenByCodeAndHotelCode(String code, String hotelCode) {
        Optional<ManageRoomType> optionalEntity = repositoryQuery.findManageRatePlanByCodeAndHotelCode(code, hotelCode);

        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_FOUND, new ErrorField("code", "Room type not found.")));
    }

}
