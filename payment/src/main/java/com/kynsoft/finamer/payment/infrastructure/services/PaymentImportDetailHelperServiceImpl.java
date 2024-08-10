package com.kynsoft.finamer.payment.infrastructure.services;

import com.kynsof.share.core.application.excel.ExcelBean;
import com.kynsof.share.core.application.excel.ReaderConfiguration;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsof.share.core.infrastructure.excel.ExcelBeanReader;
import com.kynsoft.finamer.payment.application.command.paymentImport.detail.PaymentImportDetailRequest;
import com.kynsoft.finamer.payment.domain.dto.ManagePaymentTransactionTypeDto;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.domain.excel.PaymentImportCache;
import com.kynsoft.finamer.payment.domain.excel.bean.detail.PaymentDetailRow;
import com.kynsoft.finamer.payment.domain.excel.bean.Row;
import com.kynsoft.finamer.payment.domain.excel.error.PaymentDetailRowError;
import com.kynsoft.finamer.payment.domain.services.IManagePaymentTransactionTypeService;
import com.kynsoft.finamer.payment.domain.services.AbstractPaymentImportHelperService;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import com.kynsoft.finamer.payment.infrastructure.excel.PaymentCacheFactory;
import com.kynsoft.finamer.payment.infrastructure.excel.event.createPayment.CreatePaymentDetailEvent;
import com.kynsoft.finamer.payment.infrastructure.excel.validators.detail.PaymentDetailValidatorFactory;
import com.kynsoft.finamer.payment.infrastructure.repository.redis.PaymentImportCacheRepository;
import com.kynsoft.finamer.payment.infrastructure.repository.redis.error.PaymentImportDetailErrorRepository;
import io.jsonwebtoken.lang.Assert;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentImportDetailHelperServiceImpl extends AbstractPaymentImportHelperService {

    private final PaymentImportCacheRepository paymentImportCacheRepository;

    private final PaymentDetailValidatorFactory paymentDetailValidatorFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final IManagePaymentTransactionTypeService transactionTypeService;
    private final IPaymentService paymentService;

    private final PaymentImportDetailErrorRepository detailErrorRepository;


    public PaymentImportDetailHelperServiceImpl(PaymentImportCacheRepository paymentImportCacheRepository,
                                                PaymentDetailValidatorFactory paymentDetailValidatorFactory,
                                                RedisTemplate<String, String> redisTemplate,
                                                ApplicationEventPublisher applicationEventPublisher,
                                                IManagePaymentTransactionTypeService transactionTypeService,
                                                IPaymentService paymentService,
                                                PaymentImportDetailErrorRepository detailErrorRepository) {
        super(redisTemplate);
        this.paymentImportCacheRepository = paymentImportCacheRepository;
        this.paymentDetailValidatorFactory = paymentDetailValidatorFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.transactionTypeService = transactionTypeService;
        this.paymentService = paymentService;
        this.detailErrorRepository = detailErrorRepository;
    }

    @Override
    public void readExcel(ReaderConfiguration readerConfiguration, Object rawRequest) {
        PaymentImportDetailRequest request = (PaymentImportDetailRequest) rawRequest;
        paymentDetailValidatorFactory.createValidators();
        ExcelBeanReader<PaymentDetailRow> excelBeanReader = new ExcelBeanReader<>(readerConfiguration, PaymentDetailRow.class);
        ExcelBean<PaymentDetailRow> excelBean = new ExcelBean<>(excelBeanReader);
        for (PaymentDetailRow row : excelBean) {
            row.setImportProcessId(request.getImportProcessId());
            row.setImportType(request.getImportPaymentType().name());
            if (paymentDetailValidatorFactory.validate(row)) {
                cachingPaymentImport(row);
            }
        }
    }

    @Override
    public void cachingPaymentImport(Row paymentRow) {
        PaymentImportCache paymentImportCache = PaymentCacheFactory.getPaymentImportCache(paymentRow);
        paymentImportCacheRepository.save(paymentImportCache);
    }

    @Override
    public void clearPaymentImportCache(String importProcessId) {
        Pageable pageable = PageRequest.of(0, 500, Sort.by(Sort.Direction.ASC, "id"));
        Page<PaymentImportCache> cacheList;
        do {
            cacheList = paymentImportCacheRepository.findAllByImportProcessId(importProcessId, pageable);
            paymentImportCacheRepository.deleteAll(cacheList.getContent());
            pageable.next();
        } while (cacheList.hasNext());
    }

    @Override
    public List<UUID> readPaymentCacheAndSave(Object rawRequest) {
        PaymentImportDetailRequest request = (PaymentImportDetailRequest) rawRequest;
        Pageable pageable = PageRequest.of(0, 500, Sort.by(Sort.Direction.ASC, "id"));
        Page<PaymentImportCache> cacheList;
        do {
            cacheList = paymentImportCacheRepository.findAllByImportProcessId(request.getImportProcessId(), pageable);
            cacheList.forEach(paymentImportCache -> {
                ManagePaymentTransactionTypeDto managePaymentTransactionTypeDto = transactionTypeService.findByCode(paymentImportCache.getTransactionId());
                Assert.notNull(managePaymentTransactionTypeDto, "Transaction type is null");
                PaymentDto paymentDto = paymentService.findByPaymentId(Long.parseLong(paymentImportCache.getPaymentId()));
                this.sendCreatePaymentDetail(paymentDto.getId(),
                        Double.parseDouble(paymentImportCache.getPaymentAmount()),
                        UUID.fromString(request.getEmployeeId()),
                        managePaymentTransactionTypeDto.getId(),
                        paymentImportCache.getRemarks());
            });
            pageable.next();
        } while (cacheList.hasNext());
        return Collections.EMPTY_LIST;
    }

    @Override
    public PaginatedResponse getPaymentImportErrors(String importProcessId, Pageable pageable) {
        Page<PaymentDetailRowError> page = detailErrorRepository.findAllByImportProcessId(importProcessId, pageable);
        return new PaginatedResponse(page.getContent(), page.getTotalPages(), page.getNumberOfElements(), page.getTotalElements(), page.getSize(), page.getNumber());
    }

    private void sendCreatePaymentDetail(UUID paymentId, double amount,
                                         UUID employee,
                                         UUID transactionType,
                                         String remarks) {
        CreatePaymentDetailEvent createPaymentDetailEvent = new CreatePaymentDetailEvent(this);
        createPaymentDetailEvent.setPayment(paymentId);
        createPaymentDetailEvent.setAmount(amount);
        createPaymentDetailEvent.setStatus(Status.ACTIVE);
        createPaymentDetailEvent.setEmployee(employee);
        createPaymentDetailEvent.setTransactionType(transactionType);
        createPaymentDetailEvent.setRemark(remarks);
        applicationEventPublisher.publishEvent(createPaymentDetailEvent);
    }


}