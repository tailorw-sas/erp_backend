package com.kynsof.share.core.domain.kafka.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReplicateManageAgencyKafka implements Serializable {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
    @JsonProperty("client")
    private UUID client;
    @JsonProperty("bookingCouponFormat")
    private String bookingCouponFormat;
    @JsonProperty("status")
    private String status;

    @JsonProperty("generationType")
    private String generationType;
    @JsonProperty("agencyType")
    private UUID agencyType;
    @JsonProperty("cif")
    private String cif;
    @JsonProperty("address")
    private String address;
    @JsonProperty("sentB2BPartner")
    private UUID sentB2BPartner;
    @JsonProperty("cityState")
    private UUID cityState;
    @JsonProperty("country")
    private UUID country;
    private String mailingAddress;
    private String zipCode;
    private String city;
    private Integer creditDay;
    private Boolean autoReconcile;
    private Boolean validateCheckout;
    private String agencyAlias;
}
