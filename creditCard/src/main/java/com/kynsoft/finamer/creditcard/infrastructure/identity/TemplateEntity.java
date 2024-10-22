package com.kynsoft.finamer.creditcard.infrastructure.identity;

import com.kynsoft.finamer.creditcard.domain.dto.TemplateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "template_entity")
public class TemplateEntity {

    @Id
    private UUID id;

    private String templateCode;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "boolean DEFAULT FALSE")
    private boolean deleted;

    private String languageCode;

    private String type;

    public TemplateEntity(TemplateDto dto) {
        this.id = dto.getId();
        this.templateCode = dto.getTemplateCode();
        this.name = dto.getName();
        this.languageCode = dto.getLanguageCode();
        this.type = dto.getType();
    }


    public TemplateDto toAggregate() {
        return new TemplateDto(this.id, this.templateCode, this.name, languageCode, type);
    }
}
