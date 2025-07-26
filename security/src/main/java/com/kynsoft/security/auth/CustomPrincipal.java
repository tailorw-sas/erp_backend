package com.kynsoft.security.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class CustomPrincipal implements Serializable {
    private final String username;
    private final String email;
    private final List<String> hotelAccess;
    private final List<String> agencyAccess;
    private final List<String> reportAccess;
    private final Map<String, Object> attributes;
}