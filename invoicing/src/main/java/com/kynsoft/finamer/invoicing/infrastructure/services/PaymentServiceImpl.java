package com.kynsoft.finamer.invoicing.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.invoicing.application.query.objectResponse.PaymentResponse;
import com.kynsoft.finamer.invoicing.domain.dto.PaymentDto;
import com.kynsoft.finamer.invoicing.domain.services.IPaymentService;
import com.kynsoft.finamer.invoicing.infrastructure.identity.Payment;
import com.kynsoft.finamer.invoicing.infrastructure.repository.command.PaymentWriteDataJPARepository;
import com.kynsoft.finamer.invoicing.infrastructure.repository.query.PaymentReadDataJPARepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentWriteDataJPARepository repositoryCommand;

    @Autowired
    private PaymentReadDataJPARepository repositoryQuery;

    @Override
    public PaymentDto create(PaymentDto dto) {
        Payment data = new Payment(dto);
        return this.repositoryCommand.save(data).toAggregate();
    }

    @Override
    public void createAll(List<PaymentDto> paymentList) {
        if(Objects.nonNull(paymentList)){
            List<Payment> payments = paymentList.stream().map(Payment::new).collect(Collectors.toList());
            repositoryCommand.saveAll(payments);
        }
    }

    @Override
    public void update(PaymentDto dto) {
        Payment update = new Payment(dto);

        this.repositoryCommand.save(update);
    }

    @Override
    public void delete(PaymentDto dto) {
        try{
            this.repositoryCommand.deleteById(dto.getId());
        } catch (Exception e){
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
        }
    }

    @Override
    public PaymentDto findById(UUID id) {
        Optional<Payment> userSystem = this.repositoryQuery.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {

        GenericSpecificationsBuilder<Payment> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<Payment> data = this.repositoryQuery.findAll(specifications, pageable);

        return getPaginatedResponse(data);
    }

    private PaginatedResponse getPaginatedResponse(Page<Payment> data) {
        List<PaymentResponse> responses = new ArrayList<>();
        for (Payment p : data.getContent()) {
            responses.add(new PaymentResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

}
