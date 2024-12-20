package com.kynsoft.finamer.payment.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.payment.application.query.objectResponse.search.PaymentSearchResponse;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import com.kynsoft.finamer.payment.infrastructure.identity.Payment;
import com.kynsoft.finamer.payment.infrastructure.repository.command.PaymentWriteDataJPARepository;
import com.kynsoft.finamer.payment.infrastructure.repository.query.PaymentReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentWriteDataJPARepository repositoryCommand;

    @Autowired
    private PaymentReadDataJPARepository repositoryQuery;

    @Override
    public PaymentDto create(PaymentDto dto) {
        Payment data = new Payment(dto);
        data.setPaymentId(this.findMaxId());
        return this.repositoryCommand.save(data).toAggregateBasicPayment();
    }

    @Override
    public List<PaymentDto> createBulk(List<PaymentDto> dtoList) {
        List<Payment> save = new ArrayList<>();
        Long paymentId = this.findMaxId();
        for (PaymentDto paymentDto : dtoList) {
            Payment newPayment = new Payment(paymentDto);
            newPayment.setPaymentId(paymentId);
            save.add(newPayment);
            paymentId = paymentId + 1;
        }
        return this.repositoryQuery.saveAllAndFlush(save).stream().map(Payment::toAggregate).toList();
    }

    @Override
    public void update(PaymentDto dto) {
        Payment update = new Payment(dto);

        update.setUpdatedAt(OffsetDateTime.now(ZoneId.of("UTC")));

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(PaymentDto dto) {
        try {
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public PaymentDto findById(UUID id) {
        Optional<Payment> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregateWihtDetails();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaymentDto findPaymentByIdAndDetails(UUID id) {
        Optional<Payment> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregateWihtDetails();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public boolean existPayment(long genId) {
        return repositoryQuery.existsPaymentByPaymentId(genId);
    }

    @Override
    public PaymentDto findByPaymentId(long paymentId) {
        return repositoryQuery.findPaymentByPaymentId(paymentId).map(Payment::toAggregate)
                .orElseThrow(() -> new BusinessNotFoundException(
                        new GlobalBusinessException(DomainErrorMessage.PAYMENT_NOT_FOUND,
                                new ErrorField("payment id", DomainErrorMessage.PAYMENT_NOT_FOUND.getReasonPhrase()))));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<Payment> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<Payment> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public Page<PaymentDto> paymentCollectionSummary(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<Payment> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        return this.repositoryQuery.findAll(specifications, pageable).map(Payment::toAggregate);


    }

    @Override
    public PaginatedResponse searchExcelExporter(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<Payment> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<Payment> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedExcelExporter(data);
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

    private PaginatedResponse getPaginatedResponse(Page<Payment> data) {
        List<PaymentSearchResponse> responses = new ArrayList<>();
        for (Payment p : data.getContent()) {
            responses.add(new PaymentSearchResponse(p.toAggregateWihtDetails()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    private PaginatedResponse getPaginatedExcelExporter(Page<Payment> data) {
        List<PaymentDto> responses = new ArrayList<>();
        for (Payment p : data.getContent()) {
            responses.add(p.toAggregateWihtDetails());
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public Long countByAgency(UUID agencyId) {
        return this.repositoryQuery.countByAgency(agencyId);
    }

    @Override
    public Long countByAgencyOther(UUID agencyId) {
        return this.repositoryQuery.countByAgencyOther(agencyId);
    }

    @Override
    public Long findMaxId() {
        return this.repositoryQuery.findMaxId() + 1;
    }

}
