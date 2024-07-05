package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ManageEmployeeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "manage_employee")
public class ManageEmployee implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;

    public ManageEmployee(ManageEmployeeDto dto) {
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
    }

    public ManageEmployeeDto toAggregate() {
        return new ManageEmployeeDto(
                id, 
                firstName, 
                lastName, 
                email
        );
    }

}