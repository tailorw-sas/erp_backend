package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_booking")
public class ManageBooking {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "serial", name = "booking_gen_id")
    @Generated(event = EventType.INSERT)
    private Long booking_id;

    private LocalDateTime hotelCreationDate;
    private LocalDateTime bookingDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    private String hotelBookingNumber;
    private String firstName;
    private String lastName;
    private Double invoiceAmount;
    private String roomNumber;
    private String couponNumber;
    private Integer adults;
    private Integer children;
    private Double rateAdult;
    private Double rateChild;
    private String hotelInvoiceNumber;
    private String folioNumber;
    private Double hotelAmount;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_invoice", nullable = true)
    private ManageInvoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_rate_plan", nullable = true)
    private ManageRatePlan ratePlan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_night_type", nullable = true)
    private ManageNightType nightType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_room_type", nullable = true)
    private ManageRoomType roomType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_room_category", nullable = true)
    private ManageRoomCategory roomCategory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "booking")
    private List<ManageRoomRate> roomRates;

    @Column(nullable = true)
    private Boolean deleted = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    public ManageBooking(ManageBookingDto dto) {
        this.id = dto.getId();
        this.hotelCreationDate = dto.getHotelCreationDate();
        this.bookingDate = dto.getBookingDate();
        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.hotelBookingNumber = dto.getHotelBookingNumber();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.invoiceAmount = dto.getInvoiceAmount();
        this.roomNumber = dto.getRoomNumber();
        this.couponNumber = dto.getCouponNumber();
        this.adults = dto.getAdults();
        this.children = dto.getChildren();
        this.rateAdult = dto.getRateAdult();
        this.rateChild = dto.getRateChild();
        this.hotelInvoiceNumber = dto.getHotelInvoiceNumber();
        this.folioNumber = dto.getFolioNumber();
        this.hotelAmount = dto.getHotelAmount();
        this.description = dto.getDescription();
        this.invoice = dto.getInvoice() != null ? new ManageInvoice(dto.getInvoice()) : null;
        this.ratePlan = dto.getRatePlan() != null ? new ManageRatePlan(dto.getRatePlan()) : null;
        this.nightType = dto.getNightType() != null ? new ManageNightType(dto.getNightType()) : null;
        this.roomType = dto.getRoomType() != null ? new ManageRoomType(dto.getRoomType()) : null;
        this.roomCategory = dto.getRoomCategory() != null ? new ManageRoomCategory(dto.getRoomCategory()) : null;
        this.roomRates = dto.getRoomRates() != null ? dto.getRoomRates().stream().map(r -> {
            ManageRoomRate roomRate = new ManageRoomRate(r);
            roomRate.setBooking(this);
            return roomRate;
        }).collect(Collectors.toList()) : null;
    }

    public ManageBookingDto toAggregate() {
        return new ManageBookingDto(id, booking_id, hotelCreationDate, bookingDate, checkIn, checkOut,
                hotelBookingNumber, firstName, lastName, invoiceAmount, roomNumber, couponNumber, adults, children,
                rateAdult, rateChild, hotelInvoiceNumber, folioNumber, hotelAmount, description,
                invoice != null ? invoice.toAggregate() : null, ratePlan != null ? ratePlan.toAggregate() : null,
                nightType != null ? nightType.toAggregate() : null, roomType != null ? roomType.toAggregate() : null,
                roomCategory != null ? roomCategory.toAggregate() : null,
                roomRates != null ? roomRates.stream().map(b -> {
                    return b.toAggregateSample();
                }).collect(Collectors.toList()) : null);
    }

    public ManageBookingDto toAggregateSample() {
        return new ManageBookingDto(id, booking_id, hotelCreationDate, bookingDate, checkIn, checkOut,
                hotelBookingNumber, firstName, lastName, invoiceAmount, roomNumber, couponNumber, adults, children,
                rateAdult, rateChild, hotelInvoiceNumber, folioNumber, hotelAmount, description,
                null, ratePlan != null ? ratePlan.toAggregate() : null,
                nightType != null ? nightType.toAggregate() : null, roomType != null ? roomType.toAggregate() : null,
                roomCategory != null ? roomCategory.toAggregate() : null,
                roomRates != null ? roomRates.stream().map(b -> {
                    return b.toAggregateSample();
                }).collect(Collectors.toList()) : null);
    }
}
