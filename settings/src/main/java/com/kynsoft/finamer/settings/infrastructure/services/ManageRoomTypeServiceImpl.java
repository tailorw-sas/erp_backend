package com.kynsoft.finamer.settings.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsof.share.core.infrastructure.specifications.LogicalOperation;
import com.kynsof.share.core.infrastructure.specifications.SearchOperation;
import com.kynsoft.finamer.settings.application.query.objectResponse.ManageRoomTypeResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageRoomTypeDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.services.IManageRoomTypeService;
import com.kynsoft.finamer.settings.infrastructure.identity.ManageRoomType;
import com.kynsoft.finamer.settings.infrastructure.repository.command.ManageRoomTypeWriteDataJPARepository;
import com.kynsoft.finamer.settings.infrastructure.repository.query.ManageEmployeeReadDataJPARepository;
import com.kynsoft.finamer.settings.infrastructure.repository.query.ManageRoomTypeReadDataJPARepository;
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
public class ManageRoomTypeServiceImpl implements IManageRoomTypeService {

    @Autowired
    private final ManageRoomTypeWriteDataJPARepository repositoryCommand;

    @Autowired
    private final ManageRoomTypeReadDataJPARepository repositoryQuery;

    private final ManageEmployeeReadDataJPARepository employeeReadDataJPARepository;

    public ManageRoomTypeServiceImpl(ManageRoomTypeWriteDataJPARepository repositoryCommand, 
                                     ManageRoomTypeReadDataJPARepository repositoryQuery,
                                     ManageEmployeeReadDataJPARepository employeeReadDataJPARepository) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.employeeReadDataJPARepository = employeeReadDataJPARepository;
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
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
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
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria, UUID employeeId) {
        filterCriteria(filterCriteria, employeeId);

        GenericSpecificationsBuilder<ManageRoomType> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageRoomType> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }

    @Override
    public Long countByCodeAndManageHotelIdAndNotId(String code, UUID manageHotelId, UUID id) {
        return repositoryQuery.countByCodeAndManageHotelIdAndNotId(code, manageHotelId, id);
    }

    private void filterCriteria(List<FilterCriteria> filterCriteria, UUID employeeId) {
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
        List<UUID> ids = this.employeeReadDataJPARepository.findHotelsIdsByEmployeeId(employeeId);
        FilterCriteria fc = new FilterCriteria();
        fc.setKey("manageHotel.id");
        fc.setLogicalOperation(LogicalOperation.AND);
        fc.setOperator(SearchOperation.IN);
        fc.setValue(ids);
        filterCriteria.add(fc);
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
    public List<ManageRoomTypeDto> findAllToReplicate() {
        List<ManageRoomType> objects = this.repositoryQuery.findAll();
        List<ManageRoomTypeDto> objectDtos = new ArrayList<>();

        for (ManageRoomType object : objects) {
            objectDtos.add(object.toAggregate());
        }

        return objectDtos;
    }

}
