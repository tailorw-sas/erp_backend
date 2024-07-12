package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceStatus;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.invoicing.domain.dtoEnum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.generator.EventType;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_invoice")
public class ManageInvoice {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "serial", name = "invoice_gen_id")
    @Generated(event = EventType.INSERT)
    private Long invoice_id;

    @Column(columnDefinition = "serial", name = "inovice_no")
    @Generated(event = EventType.INSERT)
    private Long invoiceNo;

    private String invoiceNumber;

    private LocalDateTime invoiceDate;

    private LocalDateTime dueDate;

    private Boolean isManual;
    private Boolean autoRec;

    private Double invoiceAmount;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "invoice")
    private List<ManageBooking> bookings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "invoice")
    private List<ManageAttachment> attachments;

    @Column(nullable = true)
    private Boolean deleted = false;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    public ManageInvoice(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceDate = dto.getInvoiceDate();
        this.isManual = dto.getIsManual();
        this.invoiceAmount = dto.getInvoiceAmount();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.invoiceType = dto.getInvoiceType() != null ? dto.getInvoiceType() : EInvoiceType.INVOICE;
        this.invoiceStatus = dto.getStatus();
        this.autoRec = false;
        this.bookings = dto.getBookings() != null ? dto.getBookings().stream().map(_booking -> {
            ManageBooking booking = new ManageBooking(_booking);
            booking.setInvoice(this);
            return booking;
        }).collect(Collectors.toList()) : null;
        this.dueDate = dto.getDueDate();
        this.attachments = dto.getAttachments() != null ? dto.getAttachments().stream().map(_attachment -> {
            ManageAttachment attachment = new ManageAttachment(_attachment);
            attachment.setInvoice(this);
            return attachment;
        }).collect(Collectors.toList()) : null;

    }

    public ManageInvoiceDto toAggregateSample() {

        return new ManageInvoiceDto(id, invoice_id, invoiceNo, invoiceNumber, invoiceDate, dueDate, isManual,
                invoiceAmount,
                hotel.toAggregate(), agency.toAggregate(), invoiceType, invoiceStatus,
                autoRec, null, null);

    }

    public ManageInvoiceDto toAggregate() {
        return new ManageInvoiceDto(id, invoice_id,
                invoiceNo, invoiceNumber, invoiceDate, dueDate, isManual, invoiceAmount,
                hotel.toAggregate(), agency.toAggregate(), invoiceType, invoiceStatus,
                autoRec, bookings != null ? bookings.stream().map(b -> {
                    return b.toAggregateSample();
                }).collect(Collectors.toList()) : null, attachments != null ? attachments.stream().map(b -> {
                    return b.toAggregateSample();
                }).collect(Collectors.toList()) : null);
    }

}
