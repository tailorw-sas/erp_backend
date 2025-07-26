package com.kynsoft.security.extractor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionExtractor {

    @SuppressWarnings("unchecked")
    public static Collection<? extends GrantedAuthority> extract(Jwt jwt) {
        return extract(jwt, "permissions");
    }

    @SuppressWarnings("unchecked")
    public static Collection<? extends GrantedAuthority> extract(Jwt jwt, String permissionsClaim) {
        if (!jwt.hasClaim(permissionsClaim)) {
            return Set.of();
        }

        try {
            Collection<String> permissions = jwt.getClaim(permissionsClaim);
            if (permissions == null) {
                return Set.of();
            }

            return permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } catch (ClassCastException e) {
            // Log warning y retornar conjunto vacío si el claim no es una colección
            return Set.of();
        }
    }
}