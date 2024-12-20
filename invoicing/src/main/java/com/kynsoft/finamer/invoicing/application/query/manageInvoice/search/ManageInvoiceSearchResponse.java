package com.kynsoft.finamer.invoicing.application.query.manageInvoice.search;

import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.ImportType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ManageInvoiceSearchResponse {
    private UUID id;
    private Long invoiceId;
    private Boolean isManual;
    private Long invoiceNo;
    private Double invoiceAmount;
    private Double dueAmount;
    private LocalDateTime invoiceDate;
    private ManageInvoiceHotelResponse hotel;
    private ManageInvoiceAgencyResponse agency;
    private ManageInvoiceStatusResponse invoiceStatus;
    private Boolean hasAttachments;
    private EInvoiceStatus status;
    private Boolean isInCloseOperation;
    private EInvoiceType invoiceType;
    private String invoiceNumber;
    private ManageInvoiceTypeResponse manageInvoiceType;
    private String sendStatusError;
    private String parent;
    private Boolean autoRec;
    private Double originalAmount;
    private ImportType importType;
    private boolean cloneParent;

    public ManageInvoiceSearchResponse(ManageInvoiceDto projection, Boolean isHasAttachments, Boolean isInCloseOperation) {
        //DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        this.id = projection.getId();
        this.invoiceId = projection.getInvoiceId();
        this.isManual = projection.getIsManual();
        this.invoiceNo = projection.getInvoiceNo();
        this.invoiceAmount = projection.getInvoiceAmount() != null ? projection.getInvoiceAmount() : null;
        this.dueAmount = projection.getDueAmount() != null ? projection.getDueAmount() : null;
        this.invoiceDate = projection.getInvoiceDate();
        this.hotel = new ManageInvoiceHotelResponse(projection.getHotel());
        this.agency = new ManageInvoiceAgencyResponse(projection.getAgency());
        this.invoiceStatus = projection.getManageInvoiceStatus() != null ? new ManageInvoiceStatusResponse(projection.getManageInvoiceStatus()) : null;
        this.hasAttachments = isHasAttachments;
        this.status = projection.getStatus();
        this.isInCloseOperation = isInCloseOperation;
        this.invoiceType = projection.getInvoiceType();
        this.invoiceNumber = deleteHotelInfo(projection.getInvoiceNumber());
        this.manageInvoiceType = projection.getManageInvoiceType() != null ? new ManageInvoiceTypeResponse(projection.getManageInvoiceType()) : null;
        this.sendStatusError = projection.getSendStatusError();
        this.parent = projection.getParent() != null ? projection.getParent().getId().toString() : null;
        this.autoRec = projection.getAutoRec();
        this.originalAmount = projection.getOriginalAmount() != null ? projection.getOriginalAmount() : null;
        this.importType = projection.getImportType();
        this.cloneParent = projection.isCloneParent();
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }
}
