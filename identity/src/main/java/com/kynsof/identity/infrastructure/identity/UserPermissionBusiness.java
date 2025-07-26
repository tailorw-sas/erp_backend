package com.kynsof.identity.infrastructure.identity;

import com.kynsof.identity.domain.dto.UserPermissionBusinessDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_permission_business")
public class UserPermissionBusiness {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserSystem user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id")
    private Business business;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt= LocalDateTime.now();


    public UserPermissionBusiness(UserPermissionBusinessDto userRoleBusinessDto) {
        this.id = userRoleBusinessDto.getId();
        this.user = new UserSystem(userRoleBusinessDto.getUser());
        this.permission = new Permission(userRoleBusinessDto.getPermission());
        this.business = new Business(userRoleBusinessDto.getBusiness());
    }

    public UserPermissionBusinessDto toAggregate () {
        return new UserPermissionBusinessDto(id, user.toAggregate(), permission.toAggregate(), business.toAggregate());
    }
}
