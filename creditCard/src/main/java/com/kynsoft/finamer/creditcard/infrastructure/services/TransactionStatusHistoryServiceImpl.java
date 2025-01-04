package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.creditcard.application.query.TransactionStatusHistory.TransactionStatusHistoryResponse;
import com.kynsoft.finamer.creditcard.domain.dto.ManageEmployeeDto;
import com.kynsoft.finamer.creditcard.domain.dto.TransactionDto;
import com.kynsoft.finamer.creditcard.domain.dto.TransactionStatusHistoryDto;
import com.kynsoft.finamer.creditcard.domain.services.IManageEmployeeService;
import com.kynsoft.finamer.creditcard.domain.services.ITransactionStatusHistoryService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.TransactionStatusHistory;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.TransactionsStatusHistoryWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.TransactionStatusHistoryReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionStatusHistoryServiceImpl implements ITransactionStatusHistoryService {

    private final TransactionsStatusHistoryWriteDataJPARepository repositoryCommand;

    private final TransactionStatusHistoryReadDataJPARepository repositoryQuery;

    private final IManageEmployeeService employeeService;

    public TransactionStatusHistoryServiceImpl(TransactionsStatusHistoryWriteDataJPARepository repositoryCommand, TransactionStatusHistoryReadDataJPARepository repositoryQuery, IManageEmployeeService employeeService) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.employeeService = employeeService;
    }

    @Override
    public TransactionStatusHistoryDto create(TransactionStatusHistoryDto dto) {
        TransactionStatusHistory transactionStatusHistory = new TransactionStatusHistory(dto);
        return this.repositoryCommand.save(transactionStatusHistory).toAggregate();
    }

    @Override
    public void update(TransactionStatusHistoryDto dto) {
        TransactionStatusHistory transactionStatusHistory = new TransactionStatusHistory(dto);
        transactionStatusHistory.setUpdatedAt(LocalDateTime.now());
        this.repositoryCommand.save(transactionStatusHistory);
    }

    @Override
    public void delete(UUID id) {
        try {
            this.repositoryCommand.deleteById(id);
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public TransactionStatusHistoryDto findById(UUID id) {
        return this.repositoryQuery.findById(id)
                .map(TransactionStatusHistory::toAggregate)
                .orElseThrow(()->{
                    throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_FOUND, new ErrorField("id", DomainErrorMessage.NOT_FOUND.getReasonPhrase())));
            }
        );
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<TransactionStatusHistory> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<TransactionStatusHistory> data = repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public List<TransactionStatusHistoryDto> findByTransactionId(Long transactionId) {
        return this.repositoryQuery.findByTransactionId(transactionId).stream().map(TransactionStatusHistory::toAggregate).collect(Collectors.toList());
    }

    @Override
    public TransactionStatusHistoryDto create(TransactionDto dto, UUID employeeId) {
        ManageEmployeeDto employeeDto = null;
        if (employeeId != null) {
            employeeDto = this.employeeService.findById(employeeId);
        }
        return this.create(new TransactionStatusHistoryDto(
                UUID.randomUUID(),
                dto,
                "The transaction status change to "+dto.getStatus().getCode() + "-" +dto.getStatus().getName()+".",
                null,
                employeeDto,
                dto.getStatus(),
                0L
        ));
    }

    private PaginatedResponse getPaginatedResponse(Page<TransactionStatusHistory> data) {
        List<TransactionStatusHistoryResponse> responses = new ArrayList<>();
        for (TransactionStatusHistory p : data.getContent()) {
            responses.add(new TransactionStatusHistoryResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }
}
