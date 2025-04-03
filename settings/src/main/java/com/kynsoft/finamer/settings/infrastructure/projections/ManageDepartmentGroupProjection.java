package com.kynsoft.finamer.settings.infrastructure.projections;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ManageDepartmentGroupProjection {

    @Getter
    private UUID id;

    @Getter
    private String code;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private String status;

    public ManageDepartmentGroupProjection(UUID id, String code, String name, String descripcion, String status){
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = descripcion;
        this.status = status;
    }

}
