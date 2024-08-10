package com.kynsoft.finamer.payment.infrastructure.services.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.kynsoft.finamer.payment.application.query.report.PaymentReportResponse;
import com.kynsoft.finamer.payment.domain.dto.PaymentDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EPaymentReportType;
import com.kynsoft.finamer.payment.domain.services.IPaymentReport;
import com.kynsoft.finamer.payment.domain.services.IPaymentService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

@Service(PaymentReportInvoiceRelatedService.BEAN_ID)
public class PaymentReportInvoiceRelatedService implements IPaymentReport {
    public  static final String BEAN_ID= "INVOICE_RELATED";
    private final IPaymentService paymentService;

    public PaymentReportInvoiceRelatedService(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public PaymentReportResponse generateReport(EPaymentReportType reportType, UUID paymentId) {
        PaymentDto paymentDto = paymentService.findById(paymentId);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, data);
            document.open();
            document.addTitle("Report INVOICE_RELATED");
            document.addSubject("Using iText (usando iText)");
            document.addAuthor("FINAMER");
            Font chapterFont = new Font(Font.FontFamily.HELVETICA);
            Chunk chunk = new Chunk("INVOICE_RELATED", chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph("INVOICE_RELATED REPORT", chapterFont));
            chunk.setBackground(BaseColor.GRAY);
            document.add(chapter);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }


        return new PaymentReportResponse("payment-INVOICE_RELATED",data);
    }
}
