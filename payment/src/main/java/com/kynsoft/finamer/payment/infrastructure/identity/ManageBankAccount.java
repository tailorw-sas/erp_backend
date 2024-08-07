package com.kynsoft.finamer.payment.infrastructure.identity;

import com.kynsoft.finamer.payment.domain.dto.ManageBankAccountDto;
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
@Table(name = "manage_bank_account")
public class ManageBankAccount implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;
    private String accountNumber;
    private String status;
    private String nameOfBank;

    public ManageBankAccount(ManageBankAccountDto dto) {
        this.id = dto.getId();
        this.accountNumber = dto.getAccountNumber();
        this.status = dto.getStatus();
        this.nameOfBank = dto.getNameOfBank();
    }

    public ManageBankAccountDto toAggregate(){
        return new ManageBankAccountDto(id, accountNumber, status, nameOfBank);
    }
}
