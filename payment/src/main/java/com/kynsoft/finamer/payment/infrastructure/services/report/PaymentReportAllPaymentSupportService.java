package com.kynsoft.finamer.payment.infrastructure.services.report;

import com.kynsof.share.core.infrastructure.util.PDFUtils;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EPaymentContentProvider;
import com.kynsoft.finamer.payment.domain.services.IPaymentReport;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import com.kynsoft.finamer.payment.infrastructure.services.report.content.AbstractReportContentProvider;
import com.kynsoft.finamer.payment.infrastructure.services.report.content.ReportContentProviderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service(value = PaymentReportAllPaymentSupportService.BEAN_ID)
public class PaymentReportAllPaymentSupportService implements IPaymentReport {
    public static final String BEAN_ID = "ALL_SUPPORT";
    private final ReportContentProviderFactory reportContentProviderFactory;
    private final IPaymentService paymentService;
    @Value("${report.code.payment.details:aaa}")
    private String reportCode;

    public PaymentReportAllPaymentSupportService(ReportContentProviderFactory reportContentProviderFactory,
                                                 IPaymentService paymentService) {
        this.reportContentProviderFactory = reportContentProviderFactory;
        this.paymentService = paymentService;
    }

    @Override
    public Optional<byte[]> generateReport(UUID paymentId) {
        try {
            PaymentDto paymentDto = paymentService.findById(paymentId);
            Optional<byte[]> paymentAllSupport = this.getPaymentAllSupportContent(paymentDto.getId().toString());
            List<InputStream> contentToMerge = new LinkedList<>();
            paymentAllSupport.ifPresent(content -> {
                contentToMerge.add(new ByteArrayInputStream(content));
            });
            if (!contentToMerge.isEmpty()) {
                return Optional.of(PDFUtils.mergePDFtoByte(contentToMerge));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Optional<byte[]> getPaymentAllSupportContent(String paymentId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("paymentId", paymentId);
        AbstractReportContentProvider contentProvider = reportContentProviderFactory
                .getReportContentProvider(EPaymentContentProvider.PAYMENT_ALL_SUPPORT_CONTENT);
        return contentProvider.getContent(parameters,reportCode);
    }

}
