package com.kynsoft.finamer.payment.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.specifications.GenericSpecificationsBuilder;
import com.kynsoft.finamer.payment.application.query.objectResponse.PaymentDetailResponse;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDetailSimpleDto;
import com.kynsoft.finamer.payment.domain.dto.projection.paymentDetails.PaymentDetailSimple;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.services.IPaymentDetailService;
import com.kynsoft.finamer.payment.infrastructure.identity.PaymentDetail;
import com.kynsoft.finamer.payment.infrastructure.repository.command.ManagePaymentDetailWriteDataJPARepository;
import com.kynsoft.finamer.payment.infrastructure.repository.query.ManagePaymentDetailReadDataJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;

@Service
public class PaymentDetailServiceImpl implements IPaymentDetailService {

    @Autowired
    private ManagePaymentDetailWriteDataJPARepository repositoryCommand;

    @Autowired
    private ManagePaymentDetailReadDataJPARepository repositoryQuery;

    @Override
    public PaymentDetailDto create(PaymentDetailDto dto) {
        PaymentDetail paymentDetail = new PaymentDetail(dto);
        return this.repositoryCommand.saveAndFlush(paymentDetail).toAggregate();
    }

    @Override
    public void create(PaymentDetail dto) {
        this.repositoryCommand.save(dto);
    }

    @Override
    public List<PaymentDetailDto> createAll(List<PaymentDetailDto> dto) {
        List<PaymentDetail> paymentDetails = dto.stream().map(PaymentDetail::new).collect(Collectors.toList());
        return this.repositoryCommand.saveAllAndFlush(paymentDetails).stream().map(PaymentDetail::toAggregate).collect(Collectors.toList());
    }

    @Override
    public PaymentDetailDto update(PaymentDetailDto dto) {
        PaymentDetail update = new PaymentDetail(dto);
        update.setUpdatedAt(OffsetDateTime.now());
        return this.repositoryCommand.saveAndFlush(update).toAggregateSimpleNotPayment();
    }

    @Override
    public void delete(PaymentDetailDto dto) {
        PaymentDetail update = new PaymentDetail(dto);

        update.setCanceledTransaction(true);

        this.repositoryCommand.save(update);
//        try{
//            this.repositoryCommand.deleteById(dto.getId());
//        } catch (Exception e){
//            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.NOT_DELETE, new ErrorField("id", DomainErrorMessage.NOT_DELETE.getReasonPhrase())));
//        }
    }

    @Override
    public PaymentDetailDto findById(UUID id) {
        Optional<PaymentDetail> userSystem = this.repositoryQuery.findByIdCustom(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public PaymentDetailDto findByGenId(int id) {
        Optional<PaymentDetail> userSystem = this.repositoryQuery.findByPaymentDetailIdCustom(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND, new ErrorField("paymentDetailId", DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public boolean existByGenId(int id) {
        return repositoryQuery.existsByPaymentDetailId(id);
    }

    @Override
    public List<PaymentDetailDto> findByPaymentId(UUID paymentId) {
        List<PaymentDetail> results = repositoryQuery.findAllByPaymentIdCustom(paymentId);
        return results.stream()
                .map(PaymentDetail::toAggregate)
                .collect(Collectors.toList());
    }

    @Override
    public PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria) {
        filterCriteria(filterCriteria);

        GenericSpecificationsBuilder<PaymentDetail> specifications = new GenericSpecificationsBuilder<>(filterCriteria);
        Page<PaymentDetail> data = this.repositoryQuery.findAllCustom(specifications, pageable);

        return getPaginatedResponse(data);
    }

    @Override
    public List<UUID> bulk(List<PaymentDetailDto> toSave) {

        return this.repositoryCommand.saveAll(toSave.stream().map(PaymentDetail::new).collect(Collectors.toList()))
                .stream().map(PaymentDetail::getId).toList();
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

    private PaginatedResponse getPaginatedResponse(Page<PaymentDetail> data) {
        List<PaymentDetailResponse> responses = new ArrayList<>();
        for (PaymentDetail p : data.getContent()) {
            responses.add(new PaymentDetailResponse(p.toAggregate()));
        }
        return new PaginatedResponse(responses, data.getTotalPages(), data.getNumberOfElements(),
                data.getTotalElements(), data.getSize(), data.getNumber());
    }

    @Override
    public PaymentDetailDto findByPaymentDetailId(Long paymentDetailId) {
        Optional<PaymentDetail> userSystem = this.repositoryQuery.findByPaymentDetailIdCustom(paymentDetailId.intValue());
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public Long countByApplyPaymentAndPaymentId(UUID id) {
        return this.repositoryQuery.countByApplyPaymentAndPaymentId(id);
    }

    @Override
    public PaymentDetailSimpleDto findSimpleDetailByGenId(int id) {
        return this.repositoryQuery.findSimpleDetailByGenId(id).orElse(null);
    }

    @Override
    public PaymentDetailDto findByIdInWrite(UUID id) {
        Optional<PaymentDetail> userSystem = this.repositoryCommand.findById(id);
        if (userSystem.isPresent()) {
            return userSystem.get().toAggregate();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND.getReasonPhrase())));
    }

    @Override
    public List<PaymentDetailDto> findByIdIn(List<UUID> ids) {
        List<PaymentDetailDto> list = new ArrayList<>();
        for (PaymentDetail paymentDetail : this.repositoryQuery.findByIdIn(ids)) {
            list.add(paymentDetail.toAggregate());
        }
        return list;
    }

    @Override
    public List<PaymentDetailDto> change(List<PaymentDetail> pd) {
        List<PaymentDetailDto> list = new ArrayList<>();
        for (PaymentDetail paymentDetail : pd) {
            list.add(paymentDetail.toAggregate());
        }
        return list;
    }

    @Override
    public List<PaymentDetailDto> findSimpleDetailsByPaymentGenIds(List<Long> ids) {
        if(Objects.isNull(ids)){
            throw new IllegalArgumentException("The Ids must not be null");
        }
        return repositoryQuery.findAllByPaymentGenIdInCustom(ids).stream()
                .map(PaymentDetail::toAggregate)
                .toList();
    }

    @Override
    public List<PaymentDetail> findByPaymentDetailsIdIn(List<Long> ids) {
        return this.repositoryQuery.findByPaymentDetailIdIn(ids);
    }

    @Override
    public List<PaymentDetail> findByPaymentDetailsGenIdIn(List<Long> ids) {
        return this.repositoryQuery.findAllByPaymentGenIdIn(ids);
    }

    @Override
    public Long countByPaymentDetailIdAndTransactionTypeDeposit(UUID payment) {
        return this.repositoryQuery.countByPaymentDetailIdAndTransactionTypeDeposit(payment);
    }

    @Override
    @Cacheable(cacheNames = "PaymentDetailSimple", key = "#id", unless = "#result == null")
    public PaymentDetailSimple findPaymentDetailsSimpleCacheableByGenId(int id) {
        Optional<PaymentDetailSimple> userSystem = this.repositoryQuery.findPaymentDetailsSimpleCacheableByGenId(id);
        if (userSystem.isPresent()) {
            return userSystem.get();
        }
        throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND, new ErrorField("id", DomainErrorMessage.PAYMENT_DETAIL_NOT_FOUND.getReasonPhrase())));
    }

}
