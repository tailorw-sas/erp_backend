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

    public Invoice(ManageInvoiceDto dto) {
        this.id = dto.getId();
        this.invoiceId = dto.getInvoiceId();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceType = dto.getInvoiceType();
        this.invoiceAmount = dto.getInvoiceAmount();
        this.bookings = dto.getBookings() != null ? dto.getBookings().stream()
                .map(bookingDto -> {
                    Booking booking = new Booking(bookingDto);
                    booking.setInvoice(this);
                    return booking;
                })
                .collect(Collectors.toList()) : null;
        this.invoiceNo = dto.getInvoiceNo();
        this.hasAttachment = dto.getHasAttachment();
        this.parent = dto.getParent() != null ? new Invoice(dto.getParent()) : null;
        this.invoiceDate = dto.getInvoiceDate();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.autoRec = dto.getAutoRec();
    }

    public Invoice(ManageInvoiceDto dto, boolean includeBookings) {
        this.id = dto.getId();
        this.invoiceId = dto.getInvoiceId();
        this.invoiceNumber = dto.getInvoiceNumber();
        this.invoiceType = dto.getInvoiceType();
        this.invoiceAmount = dto.getInvoiceAmount();
        if(includeBookings){
            this.bookings = dto.getBookings() != null ? dto.getBookings().stream().map(Booking::new).collect(Collectors.toList()) : null;
        }
        this.invoiceNo = dto.getInvoiceNo();
        this.hasAttachment = dto.getHasAttachment();
        this.parent = dto.getParent() != null ? new Invoice(dto.getParent()) : null;
        this.invoiceDate = dto.getInvoiceDate();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.autoRec = dto.getAutoRec();
    }

    public ManageInvoiceDto toAggregateSample() {
        return new ManageInvoiceDto(
                id,
                invoiceId,
                invoiceNo,
                invoiceNumber,
                invoiceType,
                invoiceAmount,
                null,
                hasAttachment,
                null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec
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
                bookings != null ? bookings.stream().map(_booking -> {
                    ManageBookingDto bookingDto = _booking.toAggregateSimple();
                    bookingDto.setInvoice(_booking.getInvoice().toAggregateParent());
                    if(_booking.getParent() != null) bookingDto.setParent(_booking.getParent().toAggregateSimple());
                    return bookingDto;
                }).collect(Collectors.toList()) : null,
                hasAttachment,
                parent != null ? parent.toAggregateSample() : null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec
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
                bookings != null ? bookings.stream().map(b -> {
                            return b.toAggregateSimple();
                        }).collect(Collectors.toList()) : null,
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
                null,
                hasAttachment,
                null,
                invoiceDate,
                Objects.nonNull(hotel) ? hotel.toAggregate() : null,
                Objects.nonNull(agency) ? agency.toAggregate() : null,
                autoRec
        );
    }
}
