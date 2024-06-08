package com.kynsoft.finamer.settings.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.settings.application.query.objectResponse.ManagePaymentSourceResponse;
import com.kynsoft.finamer.settings.domain.dto.ManagePaymentSourceDto;
import com.kynsoft.finamer.settings.domain.dtoEnum.Status;
import com.kynsoft.finamer.settings.domain.services.IManagePaymentSourceService;
import com.kynsoft.finamer.settings.infrastructure.identity.ManagePaymentSource;
import com.kynsoft.finamer.settings.infrastructure.repository.command.ManagePaymentSourceWriteDataJPARepository;
import com.kynsoft.finamer.settings.infrastructure.repository.query.ManagePaymentSourceReadDataJPARepository;
import java.time.LocalDateTime;
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
public class ManagePaymentSourceServiceImpl implements IManagePaymentSourceService {

    @Autowired
    private ManagePaymentSourceReadDataJPARepository repositoryQuery;

    @Autowired
    private ManagePaymentSourceWriteDataJPARepository repositoryCommand;

    @Override
    public UUID create(ManagePaymentSourceDto dto) {
        ManagePaymentSource entity = new ManagePaymentSource(dto);
        ManagePaymentSource saved = repositoryCommand.save(entity);

        return saved.getId();
    }

    @Override
    public void update(ManagePaymentSourceDto dto) {
        ManagePaymentSource update = new ManagePaymentSource(dto);
        update.setUpdatedAt(LocalDateTime.now());
        repositoryCommand.save(update);
    }

    @Override
    public void delete(ManagePaymentSourceDto dto) {
        ManagePaymentSource delete = new ManagePaymentSource(dto);

        delete.setDeleted(Boolean.TRUE);
        delete.setCode(delete.getCode()+ "-" + UUID.randomUUID());
        delete.setStatus(Status.INACTIVE);
        delete.setDeletedAt(LocalDateTime.now());

        this.repositoryCommand.save(delete);
    }

    @Override
    public ManagePaymentSourceDto findById(UUID id) {
        Optional<ManagePaymentSource> optionalEntity = repositoryQuery.findById(id);
        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_SOURCE_NOT_FOUND, new ErrorField("id", "The manager payment source not found.")));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<ManagePaymentSource> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManagePaymentSource> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    private PaginatedResponse getPaginatedResponse(Page<ManagePaymentSource> data) {
        List<ManagePaymentSourceResponse> responseList = new ArrayList<>();
        for (ManagePaymentSource entity : data.getContent()) {
            responseList.add(new ManagePaymentSourceResponse(entity.toAggregate()));
        }
        return new PaginatedResponse(responseList, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }
}
