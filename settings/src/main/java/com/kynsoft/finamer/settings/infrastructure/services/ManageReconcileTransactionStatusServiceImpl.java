package com.kynsoft.finamer.settings.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.settings.application.query.objectResponse.ManageReconcileTransactionStatusResponse;
import com.kynsoft.finamer.settings.domain.dto.ManageReconcileTransactionStatusDto;
import com.kynsoft.finamer.settings.domain.services.IManageReconcileTransactionStatusService;
import com.kynsoft.finamer.settings.infrastructure.identity.ManageReconcileTransactionStatus;


import com.kynsoft.finamer.settings.infrastructure.repository.command.ManageReconcileTransactionStatusWriteDataJPARepository;
import com.kynsoft.finamer.settings.infrastructure.repository.query.ManageReconcileTransactionStatusReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManageReconcileTransactionStatusServiceImpl implements IManageReconcileTransactionStatusService {

    @Autowired
    private ManageReconcileTransactionStatusReadDataJPARepository repositoryQuery;

    @Autowired
    private ManageReconcileTransactionStatusWriteDataJPARepository repositoryCommand;

    @Override
    public UUID create(ManageReconcileTransactionStatusDto dto) {
        ManageReconcileTransactionStatus entity = new ManageReconcileTransactionStatus(dto);
        ManageReconcileTransactionStatus saved = repositoryCommand.save(entity);

        return saved.getId();
    }

    @Override
    public void update(ManageReconcileTransactionStatusDto dto) {
        repositoryCommand.save(new ManageReconcileTransactionStatus(dto));
    }

    @Override
    public void delete(ManageReconcileTransactionStatusDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public ManageReconcileTransactionStatusDto findById(UUID id) {
        Optional<ManageReconcileTransactionStatus> optionalEntity = repositoryQuery.findById(id);
        if(optionalEntity.isPresent()){
            return optionalEntity.get().toAggregate();
        }

        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.MANAGE_PAYMENT_SOURCE_NOT_FOUND, new ErrorField("id", "The manager payment source not found.")));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<ManageReconcileTransactionStatus> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<ManageReconcileTransactionStatus> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    private PaginatedResponse getPaginatedResponse(Page<ManageReconcileTransactionStatus> data) {
        List<ManageReconcileTransactionStatusResponse> responseList = new ArrayList<>();
        for (ManageReconcileTransactionStatus entity : data.getContent()) {
            responseList.add(new ManageReconcileTransactionStatusResponse(entity.toAggregate()));
        }
        return new PaginatedResponse(responseList, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public List<ManageReconcileTransactionStatusDto> findByIds(List<UUID> ids) {
        return repositoryQuery.findAllById(ids).stream().map(ManageReconcileTransactionStatus::toAggregate).toList();
    }

    @Override
    public Long countByCodeAndNotId(String code, UUID id) {
        return repositoryQuery.countByCodeAndNotId(code, id);
    }
}
