package com.kynsoft.security.extractor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class ResourceRoleExtractor {

    @SuppressWarnings("unchecked")
    public static Collection<? extends GrantedAuthority> extract(Jwt jwt, String resourceId) {
        if (jwt.getClaim("resource_access") == null) {
            return Set.of();
        }

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (!resourceAccess.containsKey(resourceId)) {
            return Set.of();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}