package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.AttachmentStatusHistoryResponse;
import com.kynsoft.finamer.creditcard.domain.dto.AttachmentDto;
import com.kynsoft.finamer.creditcard.domain.dto.AttachmentStatusHistoryDto;
import com.kynsoft.finamer.creditcard.domain.dto.ManageEmployeeDto;
import com.kynsoft.finamer.creditcard.domain.services.IAttachmentStatusHistoryService;
import com.kynsoft.finamer.creditcard.domain.services.IManageEmployeeService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.AttachmentStatusHistory;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.AttachmentStatusHistoryWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.AttachmentStatusHistoryReadDataJPARepository;
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
public class AttachmentStatusHistoryServiceImpl implements IAttachmentStatusHistoryService {

    private final AttachmentStatusHistoryWriteDataJPARepository repositoryCommand;

    private final AttachmentStatusHistoryReadDataJPARepository repositoryQuery;

    private final IManageEmployeeService employeeService;

    public AttachmentStatusHistoryServiceImpl(AttachmentStatusHistoryWriteDataJPARepository repositoryCommand, AttachmentStatusHistoryReadDataJPARepository repositoryQuery, IManageEmployeeService employeeService) {
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.employeeService = employeeService;
    }

    @Override
    public UUID create(AttachmentStatusHistoryDto dto) {
        AttachmentStatusHistory data = new AttachmentStatusHistory(dto);
        return this.repositoryCommand.save(data).getId();
    }

    @Override
    public void update(AttachmentStatusHistoryDto dto) {
        AttachmentStatusHistory update = new AttachmentStatusHistory(dto);

        update.setUpdatedAt(LocalDateTime.now());

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(AttachmentStatusHistoryDto dto) {
        try{
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e){
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public AttachmentStatusHistoryDto findById(UUID id) {
        Optional<AttachmentStatusHistory> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.ATTACHMENT_STATUS_HISTORY_NOT_FOUND, new ErrorField("id", DomainErrorMessage.ATTACHMENT_STATUS_HISTORY_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        GenericSpecificationsBuilder<AttachmentStatusHistory> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<AttachmentStatusHistory> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public UUID create(AttachmentDto attachmentDto, String action) {
        ManageEmployeeDto employeeDto = this.employeeService.findById(attachmentDto.getEmployeeId());
        AttachmentStatusHistoryDto dto = new AttachmentStatusHistoryDto(
                UUID.randomUUID(),
                "An attachment was " + action +". The file name: " + attachmentDto.getFilename(),
                attachmentDto.getAttachmentId(),
                attachmentDto.getTransaction(),
                employeeDto,
                null,
                null,
                attachmentDto.getHotelPayment()
        );

        AttachmentStatusHistory data = new AttachmentStatusHistory(dto);
        return this.repositoryCommand.save(data).getId();
    }

    private PaginatedResponse getPaginatedResponse(Page<AttachmentStatusHistory> data) {
        List<AttachmentStatusHistoryResponse> responses = new ArrayList<>();
        for (AttachmentStatusHistory p : data.getContent()) {
            responses.add(new AttachmentStatusHistoryResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

}
