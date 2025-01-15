package com.kynsoft.finamer.invoicing.infrastructure.identity;

import com.kynsoft.finamer.invoicing.domain.dto.ManageRoomRateDto;
import com.kynsof.audit.infrastructure.core.annotation.RemoteAudit;
import com.kynsof.audit.infrastructure.listener.AuditEntityListener;
import com.kynsof.share.utils.ScaleAmount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "room_rate")
@EntityListeners(AuditEntityListener.class)
@RemoteAudit(name = "room_rate",id="7b2ea5e8-e34c-47eb-a811-25a54fe2c604")
public class ManageRoomRate {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(columnDefinition = "serial", name = "room_rate_serial_id")
    @Generated(event = EventType.INSERT)
    private Long roomRateId;

    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double invoiceAmount;
    private String roomNumber;
    private Integer adults;
    private Integer children;
    private Double rateAdult;
    private Double rateChild;
    private Double hotelAmount;
    private String remark;
    private Long nights;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking")
    private Booking booking;

    @Column(nullable = true)
    private Boolean deleted = false;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean deleteInvoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "roomRate", orphanRemoval = true)
    private List<ManageAdjustment> adjustments;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    public ManageRoomRate(ManageRoomRateDto dto) {
        this.id = dto.getId();

        this.checkIn = dto.getCheckIn();
        this.checkOut = dto.getCheckOut();
        this.roomRateId = dto.getRoomRateId();
        this.invoiceAmount = dto.getInvoiceAmount() != null ? ScaleAmount.scaleAmount(dto.getInvoiceAmount()) : null;
        this.roomNumber = dto.getRoomNumber();

        this.adults = dto.getAdults();
        this.children = dto.getChildren();
        this.rateAdult = dto.getRateAdult() != null ? ScaleAmount.scaleAmount(dto.getRateAdult()) : null;
        this.rateChild = dto.getRateChild() != null ? ScaleAmount.scaleAmount(dto.getRateChild()) : null;

        this.remark = dto.getRemark();
        this.hotelAmount = dto.getHotelAmount() != null ? ScaleAmount.scaleAmount(dto.getHotelAmount()) : null;

        this.booking = dto.getBooking() != null ? new Booking(dto.getBooking()) : null;
        this.adjustments = dto.getAdjustments() != null ? dto.getAdjustments().stream().map(a -> {
            ManageAdjustment adjustment = new ManageAdjustment(a);
            adjustment.setRoomRate(this);
            return adjustment;
        }).collect(Collectors.toList()) : null;

        this.nights = dto.getCheckIn() != null && dto.getCheckOut() != null ? dto.getCheckIn().until(dto.getCheckOut(), ChronoUnit.DAYS) : 0L;
        this.deleteInvoice = dto.isDeleteInvoice();
    }

    public ManageRoomRateDto toAggregate() {
        return new ManageRoomRateDto(id, roomRateId, checkIn, checkOut,
                invoiceAmount != null ? ScaleAmount.scaleAmount(invoiceAmount) : null,
                roomNumber, adults, children,
                rateAdult != null ? ScaleAmount.scaleAmount(rateAdult) : null,
                rateChild != null ? ScaleAmount.scaleAmount(rateChild) : null,
                hotelAmount != null ? ScaleAmount.scaleAmount(hotelAmount) : null,
                remark,
                null,
            //    booking != null ? booking.toAggregate() : null,
//                adjustments != null ? adjustments.stream().map(b -> {
//                            return b.toAggregateSample();
//                        }).collect(Collectors.toList()) : null,
                null,
                nights, deleteInvoice);
    }

    public ManageRoomRateDto toAggregateSample() {
        return new ManageRoomRateDto(id, roomRateId, checkIn, checkOut,
                invoiceAmount != null ? ScaleAmount.scaleAmount(invoiceAmount) : null,
                roomNumber, adults, children,
                rateAdult != null ? ScaleAmount.scaleAmount(rateAdult) : null,
                rateChild != null ? ScaleAmount.scaleAmount(rateChild) : null,
                hotelAmount != null ? ScaleAmount.scaleAmount(hotelAmount) : null,
                remark, null,
//                adjustments != null ? adjustments.stream().map(b -> {
//                            return b.toAggregateSample();
//                        }).collect(Collectors.toList()) : null,
                null,
                nights, deleteInvoice);
    }
}
