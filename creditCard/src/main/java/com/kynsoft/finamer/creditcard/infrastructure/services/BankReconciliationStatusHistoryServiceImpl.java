package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.creditcard.application.query.bankReconciliationStatusHistory.BankReconciliationStatusHistoryResponse;
import com.kynsoft.finamer.creditcard.domain.dto.BankReconciliationStatusHistoryDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManageBankReconciliationDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManageEmployeeDto;
import com.kynsoft.finamer.creditcard.domain.services.IBankReconciliationStatusHistoryService;
import com.kynsoft.finamer.creditcard.domain.services.IManageEmployeeService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.BankReconciliationStatusHistory;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.BankReconciliationStatusHistoryWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.BankReconciliationStatusHistoryReadDataJPARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BankReconciliationStatusHistoryServiceImpl implements IBankReconciliationStatusHistoryService {

    private final BankReconciliationStatusHistoryWriteDataJPARepository repositoryCommand;

    private final BankReconciliationStatusHistoryReadDataJPARepository repositoryQuery;

    private final IManageEmployeeService employeeService;

    public BankReconciliationStatusHistoryServiceImpl(BankReconciliationStatusHistoryWriteDataJPARepository repositoryCommand, BankReconciliationStatusHistoryReadDataJPARepository repositoryQuery, IManageEmployeeService employeeService) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.employeeService = employeeService;
    }

    @Override
    public BankReconciliationStatusHistoryDto create(BankReconciliationStatusHistoryDto dto) {
        BankReconciliationStatusHistory entity = new BankReconciliationStatusHistory(dto);
        return this.repositoryCommand.save(entity).toAggregate();
    }

    @Override
    public void update(BankReconciliationStatusHistoryDto dto) {
        BankReconciliationStatusHistory entity = new BankReconciliationStatusHistory(dto);
        entity.setUpdatedAt(LocalDateTime.now());
        this.repositoryCommand.save(entity);
    }

    @Override
    public void delete(UUID id) {
        try{
            this.repositoryCommand.deleteById(id);
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public BankReconciliationStatusHistoryDto findById(UUID id) {
        return this.repositoryQuery.findById(id)
                .map(BankReconciliationStatusHistory::toAggregate)
                .orElseThrow(()-> new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_FOUND, new ErrorField("id", DomainErrorMessage.NOT_FOUND.getReasonPhrase()))));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<BankReconciliationStatusHistory> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<BankReconciliationStatusHistory> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public BankReconciliationStatusHistoryDto create(ManageBankReconciliationDto dto, UUID employeeId) {
        ManageEmployeeDto employeeDto = null;
        if (employeeId != null) {
            employeeDto = this.employeeService.findById(employeeId);
        }
        BankReconciliationStatusHistoryDto history = new BankReconciliationStatusHistoryDto(
                UUID.randomUUID(),
                dto,
                "The reconcile status is "+ dto.getReconcileStatus().getCode()+"-"+ dto.getReconcileStatus().getName()+".",
                null,
                employeeDto,
                dto.getReconcileStatus()
        );
        BankReconciliationStatusHistory data = new BankReconciliationStatusHistory(history);
        return this.repositoryCommand.save(data).toAggregate();
    }

    private PaginatedResponse getPaginatedResponse(Page<BankReconciliationStatusHistory> data) {
        List<BankReconciliationStatusHistoryResponse> responses = new ArrayList<>();
        for (BankReconciliationStatusHistory p : data.getContent()) {
            responses.add(new BankReconciliationStatusHistoryResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }
}
