package com.kynsoft.finamer.invoicing.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.InvoiceStatusHistoryResponse;
import com.kynsoft.finamer.invoicing.domain.dto.InvoiceStatusHistoryDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageEmployeeDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import com.kynsoft.finamer.invoicing.domain.services.IInvoiceStatusHistoryService;
import com.kynsoft.finamer.invoicing.domain.services.IManageEmployeeService;
import com.kynsoft.finamer.invoicing.infrastructure.identity.InvoiceStatusHistory;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.InvoiceStatusHistoryWriteDataJPARepository;
import com.kynsoft.finamer.invoicing.infrastructure.repository.query.InvoiceStatusHistoryReadDataJPARepository;
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
public class InvoiceStatusHistoryServiceImpl implements IInvoiceStatusHistoryService {

    @Autowired
    private InvoiceStatusHistoryWriteDataJPARepository repositoryCommand;

    @Autowired
    private InvoiceStatusHistoryReadDataJPARepository repositoryQuery;

    private final IManageEmployeeService employeeService;

    public InvoiceStatusHistoryServiceImpl(IManageEmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public UUID create(InvoiceStatusHistoryDto dto) {
        InvoiceStatusHistory data = new InvoiceStatusHistory(dto);
        return this.repositoryCommand.save(data).getId();
    }

    @Override
    public void update(InvoiceStatusHistoryDto dto) {
        InvoiceStatusHistory update = new InvoiceStatusHistory(dto);

        update.setUpdatedAt(LocalDateTime.now());

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(InvoiceStatusHistoryDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public InvoiceStatusHistoryDto findById(UUID id) {
        Optional<InvoiceStatusHistory> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_CLOSE_OPERATION_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_CLOSE_OPERATION_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<InvoiceStatusHistory> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<InvoiceStatusHistory> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public UUID create(ManageInvoiceDto invoiceDto, String employee) {
        ManageEmployeeDto employeeC = null;
        String employeeFullName = "";
        try {
            employeeC = this.employeeService.findById(UUID.fromString(employee));
            employeeFullName = employeeC.getFirstName() + " " + employeeC.getLastName();
        } catch (Exception e) {
            employeeFullName = employee;
        }
        InvoiceStatusHistoryDto dto = new InvoiceStatusHistoryDto(
                UUID.randomUUID(),
                invoiceDto,
                "The invoice data was inserted.",
                null,
                //employee,
                employeeFullName,
                invoiceDto.getStatus(),
                0L
        );
        InvoiceStatusHistory data = new InvoiceStatusHistory(dto);
        return this.repositoryCommand.save(data).getId();
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

    private PaginatedResponse getPaginatedResponse(Page<InvoiceStatusHistory> data) {
        List<InvoiceStatusHistoryResponse> responses = new ArrayList<>();
        for (InvoiceStatusHistory p : data.getContent()) {
            responses.add(new InvoiceStatusHistoryResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }



}
