package com.kynsof.identity.infrastructure.dataSeeder;

import com.kynsof.identity.domain.dto.BusinessDto;
import com.kynsof.identity.domain.dto.UserStatus;
import com.kynsof.identity.domain.dto.UserSystemDto;
import com.kynsof.identity.domain.dto.enumType.EBusinessStatus;
import com.kynsof.identity.infrastructure.identity.Business;
import com.kynsof.identity.infrastructure.identity.UserSystem;
import com.kynsof.identity.infrastructure.repository.command.BusinessWriteDataJPARepository;
import com.kynsof.identity.infrastructure.repository.command.UserSystemsWriteDataJPARepository;
import com.kynsof.identity.infrastructure.repository.query.BusinessReadDataJPARepository;
import com.kynsof.identity.infrastructure.repository.query.UserSystemReadDataJPARepository;
import com.kynsof.share.core.domain.EUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserSystemReadDataJPARepository readRepository;
    private final UserSystemsWriteDataJPARepository writeRepository;
    private final BusinessReadDataJPARepository businessReadDataJPARepository;
    private final BusinessWriteDataJPARepository businessWriteDataJPARepository;

    @Autowired
    public DataSeeder(UserSystemReadDataJPARepository readRepository, UserSystemsWriteDataJPARepository writeRepository, BusinessReadDataJPARepository businessReadDataJPARepository, BusinessWriteDataJPARepository businessWriteDataJPARepository) {
        this.readRepository = readRepository;
        this.writeRepository = writeRepository;
        this.businessReadDataJPARepository = businessReadDataJPARepository;
        this.businessWriteDataJPARepository = businessWriteDataJPARepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ID del usuario que deseas verificar y crear si no existe
        UUID userId = UUID.fromString("1a570163-5761-434c-9072-fb3f76bfe501");

        if (!readRepository.existsById(userId)) {
            // Si no existe, crear el usuario
            UserSystemDto userDto = new UserSystemDto();
            userDto.setId(userId);
            userDto.setUserName("admin-user@gmail.com");
            userDto.setEmail("admin-user@gmail.com");
            userDto.setName("ADMIN");
            userDto.setLastName("ADMIN");
            userDto.setStatus(UserStatus.ACTIVE);
            userDto.setUserType(EUserType.SYSTEM);
            writeRepository.save(new UserSystem(userDto));

            System.out.println("Seeder: Usuario creado con éxito.");
        } else {
            System.out.println("Seeder: El usuario con ID " + userId + " ya existe.");
        }
       UUID businessId = UUID.fromString("41833c25-5ca2-41cd-b22f-8488723097da");
        if (!businessReadDataJPARepository.existsById(businessId)) {
            // Si no existe, crear el usuario
            BusinessDto create = new BusinessDto(
                    businessId,
                    "FINAMER",
                    "",
                    "",
                    "FINAMER",
                    "https://static.kynsoft.net/bizum_2024-05-13_12-35-00.png",
                    "111111111",
                    EBusinessStatus.ACTIVE,
                    null,
                    "ADDRESS"
            );
            businessWriteDataJPARepository.save(new Business(create));

            System.out.println("Seeder: Usuario creado con éxito.");
        } else {
            System.out.println("Seeder: El usuario con ID " + userId + " ya existe.");
        }
    }
}
