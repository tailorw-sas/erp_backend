package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ManagePaymentAttachmentStatusDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_payment_attachment_status")
public class ManagePaymentAttachmentStatus {

    @Id
    @Column(name = "id")
    private UUID id;
    private String code;
    private String name;
    private String status;
    private Boolean defaults;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean nonNone;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean patWithAttachment;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean pwaWithOutAttachment;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean supported;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean otherSupport;

    public ManagePaymentAttachmentStatus(ManagePaymentAttachmentStatusDto dto) {
        this.id = dto.getId();
        this.code = dto.getCode();
        this.name = dto.getName();
        this.status = dto.getStatus();
        this.defaults = dto.getDefaults();
        this.nonNone = dto.isNonNone();
        this.patWithAttachment = dto.isPatWithAttachment();
        this.pwaWithOutAttachment = dto.isPwaWithOutAttachment();
        this.supported = dto.isSupported();
        this.otherSupport = dto.isOtherSupport();
    }

    public ManagePaymentAttachmentStatusDto toAggregate() {
        return new ManagePaymentAttachmentStatusDto(id, code, name, status, defaults, nonNone, patWithAttachment, pwaWithOutAttachment, supported, otherSupport);
    }
}
