package com.kynsof.identity.infrastructure.identity;

import com.kynsof.identity.domain.dto.ModuleDto;
import com.kynsof.identity.domain.dto.PermissionDto;
import com.kynsof.identity.domain.dto.enumType.ModuleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "module_system")
public class ModuleSystem {

    @Id
    @Column(name = "id")
    protected UUID id;
    private String name;
    private String image;
    private String description;

    @Enumerated(EnumType.STRING)
    private ModuleStatus status;

    @Column(unique = true)
    private String code;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Permission> permissions = new HashSet<>();

    public ModuleSystem(ModuleDto module) {
        this.id = module.getId();
        this.name = module.getName();
        this.image = module.getImage();
        this.description = module.getDescription();
        this.status = module.getStatus();
        this.code = module.getCode();
    }

    public ModuleDto toAggregate () {
        List<PermissionDto> p = new ArrayList<>();
        for (Permission permission : permissions) {
            p.add(new PermissionDto(permission.getId(), permission.getCode(), permission.getDescription()));
        }
        
        return new ModuleDto(id, name, image, description, p, createdAt, status, code);
    }
}
