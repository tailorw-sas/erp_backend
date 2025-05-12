package com.kynsoft.finamer.payment.infrastructure.identity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "invoice")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "invoice",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
@DynamicUpdate
public class Invoice {

    @Id
    @Column(name = "id")
    private UUID id;
    private Long invoiceId;
    private Long invoiceNo;
    private String invoiceNumber;
    private Double invoiceAmount;
    private Double invoiceBalance;
    private LocalDateTime invoiceDate;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private Boolean hasAttachment;

    @ManyToOne(fetch = FetchType.EAGER)
    private Invoice parent;

    @Enumerated(EnumType.STRING)
    private EInvoiceType invoiceType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_hotel")
    private ManageHotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_agency")
    private ManageAgency agency;

    private Boolean autoRec;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_invoice_status")
    private ManageInvoiceStatus status;

    public Invoice(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceId = dto.getInvoiceId();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceType = dto.getInvoiceType();
        this.invoiceAmount = dto.getInvoiceAmount();
        this.invoiceBalance = dto.getInvoiceBalance();
        this.bookings = dto.getBookings() != null ? dto.getBookings().stream().map(Booking::new).collect(Collectors.toList()) : null;
        this.invoiceNo = dto.getInvoiceNo();
        this.hasAttachment = dto.getHasAttachment();
        this.parent = dto.getParent() != null ? new Invoice(dto.getParent()) : null;
        this.invoiceDate = dto.getInvoiceDate();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.autoRec = dto.getAutoRec();
        this.status = Objects.nonNull(dto.getStatus()) ? new ManageInvoiceStatus(dto.getStatus()) : null;
    }

    public ManageInvoiceDto toAggregateSample() {
        return new ManageInvoiceDto(
                id,
                invoiceId,
                invoiceNo,
                invoiceNumber,
                invoiceType,
                invoiceAmount,
                invoiceBalance,
                null,
                hasAttachment,
                null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec,
                Objects.nonNull(status) ? status.toAggregate() : null
        );
    }

    public ManageInvoiceDto toAggregate() {
        return new ManageInvoiceDto(
                id,
                invoiceId,
                invoiceNo,
                invoiceNumber,
                invoiceType,
                invoiceAmount,
                invoiceBalance,
                bookings != null ? bookings.stream().map(_booking -> {
                    ManageBookingDto bookingDto = _booking.toAggregateSimple();
                    bookingDto.setInvoice(_booking.getInvoice().toAggregateParent());
                    return bookingDto;
                }).collect(Collectors.toList()) : null,
                hasAttachment,
                parent != null ? parent.toAggregateSample() : null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec,
                Objects.nonNull(status) ? status.toAggregate() : null
        );
    }

    public ManageInvoiceDto toAggregateApplyPayment() {
        return new ManageInvoiceDto(
                id,
                invoiceId,
                null,
                null,
                invoiceType,
                invoiceAmount,
                invoiceBalance,
                bookings != null ? bookings.stream().map(b -> {
                            return b.toAggregateSimple();
                        }).collect(Collectors.toList()) : null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public ManageInvoiceDto toAggregateParent(){
        return new ManageInvoiceDto(
                id,
                invoiceId,
                invoiceNo,
                invoiceNumber,
                invoiceType,
                invoiceAmount,
                invoiceBalance,
                null,
                hasAttachment,
                null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec,
                Objects.nonNull(status) ? status.toAggregate() : null
        );
    }
}
