package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsof.share.utils.ScaleAmount;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.ImportType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.InvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invoice")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "invoice",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class Invoice {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "serial", name = "invoice_gen_id")
    @Generated(event = EventType.INSERT)
    private Long invoiceId;

    private Long invoiceNo;

    private String invoiceNumber;
    private String invoiceNumberPrefix;

    private LocalDateTime invoiceDate;

    private LocalDate dueDate;

    private Boolean isManual;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean autoRec;

    private Boolean reSend;
    private Boolean isCloned;

    private LocalDate reSendDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Invoice parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_type_id")
    private ManageInvoiceType manageInvoiceType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_status_id")
    private ManageInvoiceStatus manageInvoiceStatus;

    private Double originalAmount;
    private Double invoiceAmount;

    @Column(name = "due_amount", nullable = false)
    @ColumnDefault("0")
    private Double dueAmount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_hotel")
    private ManageHotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_agency")
    private ManageAgency agency;

    @Enumerated(EnumType.STRING)
    private EInvoiceType invoiceType;

    @Enumerated(EnumType.STRING)
    private EInvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private ImportType importType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice", cascade = CascadeType.MERGE)
    private List<ManageAttachment> attachments;

    private Double credits;

    private String sendStatusError;
    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean hasAttachments = false;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean deleteInvoice;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;
    @Column(nullable = false,columnDefinition = "integer default 0")
    private Integer aging;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean cloneParent;

    public Invoice(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceDate = dto.getInvoiceDate();
        this.isManual = dto.getIsManual();
        this.invoiceAmount = dto.getInvoiceAmount() != null ? ScaleAmount.scaleAmount(dto.getInvoiceAmount()) : null;
        this.originalAmount = dto.getOriginalAmount() != null ? ScaleAmount.scaleAmount(dto.getOriginalAmount()) : null;
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.invoiceType = dto.getInvoiceType() != null ? dto.getInvoiceType() : EInvoiceType.INVOICE;
        this.invoiceStatus = dto.getStatus();
        this.autoRec = dto.getAutoRec() != null ? dto.getAutoRec() : false;
        this.bookings = dto.getBookings() != null ? dto.getBookings().stream().map(_booking -> {
            Booking booking = new Booking(_booking);
            booking.setInvoice(this);
            return booking;
        }).collect(Collectors.toList()) : null;
        this.dueDate = dto.getDueDate();
        this.attachments = dto.getAttachments() != null ? dto.getAttachments().stream().map(_attachment -> {
            ManageAttachment attachment = new ManageAttachment(_attachment);
            attachment.setInvoice(this);
            return attachment;
        }).collect(Collectors.toList()) : null;
        this.reSend = dto.getReSend();
        this.reSendDate = dto.getReSendDate();
        this.manageInvoiceType = dto.getManageInvoiceType() != null ? new ManageInvoiceType(dto.getManageInvoiceType())
                : null;
        this.manageInvoiceStatus = dto.getManageInvoiceStatus() != null
                ? new ManageInvoiceStatus(dto.getManageInvoiceStatus())
                : null;
        this.dueAmount = dto.getDueAmount() != null ? ScaleAmount.scaleAmount(dto.getDueAmount()) : 0.0;
        this.invoiceNo = dto.getInvoiceNo();
        this.invoiceNumberPrefix = InvoiceType.getInvoiceTypeCode(dto.getInvoiceType()) + "-" + dto.getInvoiceNo();
        this.isCloned = dto.getIsCloned();
        this.parent = dto.getParent() != null ? new Invoice(dto.getParent()) : null;
        this.credits = dto.getCredits();
        this.sendStatusError = dto.getSendStatusError();
        this.aging = dto.getAging();
        this.importType = dto.getImportType() != null ? dto.getImportType() : ImportType.NONE;
        this.deleteInvoice = dto.isDeleteInvoice();
        this.cloneParent = dto.isCloneParent();
        this.hasAttachments = attachments != null && !attachments.isEmpty();
    }

    public ManageInvoiceDto toAggregateSample() {

        ManageInvoiceDto manageInvoiceDto = new ManageInvoiceDto(id, invoiceId, invoiceNo, invoiceNumber, invoiceDate, dueDate, isManual,
                invoiceAmount != null ? ScaleAmount.scaleAmount(invoiceAmount) : null, 
                dueAmount != null ? ScaleAmount.scaleAmount(dueAmount) : null,
                hotel.toAggregate(), agency.toAggregate(), invoiceType, invoiceStatus,
                autoRec, null, null, reSend, reSendDate,
                manageInvoiceType != null ? manageInvoiceType.toAggregate() : null,
                manageInvoiceStatus != null ? manageInvoiceStatus.toAggregate() : null, createdAt, isCloned,
                null, credits,aging);
        manageInvoiceDto.setOriginalAmount(originalAmount != null ? ScaleAmount.scaleAmount(originalAmount) : null);
        manageInvoiceDto.setImportType(importType);
        manageInvoiceDto.setDeleteInvoice(deleteInvoice);
        manageInvoiceDto.setCloneParent(cloneParent);
        return manageInvoiceDto;
    }

    public ManageInvoiceDto toAggregate() {
        ManageInvoiceDto manageInvoiceDto = new ManageInvoiceDto(id, invoiceId,
                invoiceNo, invoiceNumber, invoiceDate, dueDate, isManual, 
                invoiceAmount != null ? ScaleAmount.scaleAmount(invoiceAmount) : null, 
                dueAmount != null ? ScaleAmount.scaleAmount(dueAmount) : null,
                hotel.toAggregate(), agency.toAggregate(), invoiceType, invoiceStatus,
                autoRec,
                bookings != null ? bookings.stream().map(Booking::toAggregate).collect(Collectors.toList()) : null,
                attachments != null ? attachments.stream().map(ManageAttachment::toAggregateSample).collect(Collectors.toList()) : null,
                reSend,
                reSendDate,
                manageInvoiceType != null ? manageInvoiceType.toAggregate() : null,
                manageInvoiceStatus != null ? manageInvoiceStatus.toAggregate() : null, createdAt, isCloned,
                parent != null ? parent.toAggregateSample() : null, credits,aging);
        manageInvoiceDto.setOriginalAmount(originalAmount != null ? ScaleAmount.scaleAmount(originalAmount) : null);
        manageInvoiceDto.setImportType(importType);
        manageInvoiceDto.setDeleteInvoice(deleteInvoice);
        manageInvoiceDto.setCloneParent(cloneParent);
        return manageInvoiceDto;
    }

    public ManageInvoiceDto toAggregateSearch() {

        ManageInvoiceDto manageInvoiceDto = new ManageInvoiceDto(id, invoiceId, invoiceNo, invoiceNumber, invoiceDate, dueDate, isManual,
                invoiceAmount != null ? ScaleAmount.scaleAmount(invoiceAmount) : null, 
                dueAmount != null ? ScaleAmount.scaleAmount(dueAmount) : null,
                hotel.toAggregate(), agency.toAggregate(), invoiceType, invoiceStatus,
                autoRec, null, null, reSend, reSendDate,
                manageInvoiceType != null ? manageInvoiceType.toAggregate() : null,
                manageInvoiceStatus != null ? manageInvoiceStatus.toAggregate() : null, createdAt, isCloned,
                parent != null ? parent.toAggregateSample() : null, credits,aging);
        manageInvoiceDto.setSendStatusError(sendStatusError);
        manageInvoiceDto.setOriginalAmount(originalAmount != null ? ScaleAmount.scaleAmount(originalAmount) : null);
        manageInvoiceDto.setImportType(importType);
        manageInvoiceDto.setDeleteInvoice(deleteInvoice);
        manageInvoiceDto.setCloneParent(cloneParent);
        return manageInvoiceDto;
    }

    @PostLoad
    public void initDefaultValue() {
        if (dueAmount == null) {
            dueAmount = 0.0;
        }
        hasAttachments = (attachments != null && !attachments.isEmpty());
    }

}
