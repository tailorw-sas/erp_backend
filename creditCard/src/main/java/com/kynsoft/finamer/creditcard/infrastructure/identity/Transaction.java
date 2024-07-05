package com.kynsoft.finamer.creditcard.infrastructure.identity;

import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.MethodType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vcc_transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_merchant_id")
    private ManageMerchant merchant;

    @Enumerated(EnumType.STRING)
    private MethodType methodType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_hotel_id")
    private ManageHotel hotel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_agency_id")
    private ManageAgency agency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_language_id")
    private ManageLanguage language;

    private Double amount;

    private LocalDate checkIn;

    private String reservationNumber;

    private String referenceNumber;

    private String hotelContactEmail;

    private String guestName;

    private String email;

    private String enrolleCode;

    private String cardNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_credit_card_type_id")
    private ManageCreditCardType creditCardType;

    private Double commission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_status_id")
    private ManageTransactionStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manage_transaction_parent_id")
    private Transaction parent;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Transaction(TransactionDto dto) {
        this.id = dto.getId();
        this.merchant = dto.getMerchant() != null ? new ManageMerchant(dto.getMerchant()) : null;
        this.methodType = dto.getMethodType();
        this.hotel = dto.getHotel() != null ? new ManageHotel(dto.getHotel()) : null;
        this.agency = dto.getAgency() != null ? new ManageAgency(dto.getAgency()) : null;
        this.language = dto.getLanguage() != null ? new ManageLanguage(dto.getLanguage()) : null;
        this.amount = dto.getAmount();
        this.checkIn = dto.getCheckIn();
        this.reservationNumber = dto.getReservationNumber();
        this.referenceNumber = dto.getReferenceNumber();
        this.hotelContactEmail = dto.getHotelContactEmail();
        this.guestName = dto.getGuestName();
        this.email = dto.getEmail();
        this.enrolleCode = dto.getEnrolleCode();
        this.cardNumber = dto.getCardNumber();
        this.creditCardType = dto.getCreditCardType() != null ? new ManageCreditCardType(dto.getCreditCardType()) : null;
        this.commission = dto.getCommission();
        this.status = dto.getStatus() != null ? new ManageTransactionStatus(dto.getStatus()) : null;
        this.parent = dto.getParent() != null ? new Transaction(dto.getParent()) : null;
    }

    private TransactionDto toAggregateParent() {
        return new TransactionDto(
                id, checkIn, reservationNumber, referenceNumber,
                createdAt.toLocalDate());
    }

    public TransactionDto toAggregate(){
        return new TransactionDto(
                id,
                merchant.toAggregate(), methodType,
                hotel != null ? hotel.toAggregate() : null,
                agency != null ? agency.toAggregate() : null,
                language != null ? language.toAggregate() : null,
                amount,
                checkIn, reservationNumber, referenceNumber, hotelContactEmail,
                guestName, email, enrolleCode, cardNumber,
                creditCardType != null ? creditCardType.toAggregate() : null,
                commission,
                status != null ? status.toAggregate() : null,
                parent != null ? parent.toAggregateParent() : null,
                createdAt.toLocalDate()
        );
    }
}
