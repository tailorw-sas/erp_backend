package com.kynsoft.security.converter;

import com.kynsoft.security.auth.CustomPrincipal;
import com.kynsoft.security.extractor.AccessClaimExtractor;
import com.kynsoft.security.extractor.PermissionExtractor;
import com.kynsoft.security.extractor.ResourceRoleExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationConverter.class);

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute:preferred_username}")
    private String principleAttribute;

    @Value("${jwt.auth.converter.resource-id:quipux-gateway}")
    private String resourceId;

    // Constructor sin argumentos (necesario para @Component)
    public JwtAuthenticationConverter() {
    }

    @NonNull
    public Mono<AbstractAuthenticationToken> convert(@NonNull Jwt jwt) {
        return Mono.fromSupplier(() -> {
            try {
                Collection<? extends GrantedAuthority> authorities = collectAuthorities(jwt);

                CustomPrincipal principal = new CustomPrincipal(
                        getPrincipleClaimName(jwt),
                        jwt.getClaimAsString("email"),
                        AccessClaimExtractor.extractList(jwt, "hotel_access"),
                        AccessClaimExtractor.extractList(jwt, "agency_access"),
                        AccessClaimExtractor.extractList(jwt, "report_access"),
                        jwt.getClaims()
                );

                log.debug("JWT Authorities: {}", authorities);
                log.debug("CustomPrincipal created: {}", principal);

                return new JwtAuthenticationToken(
                        jwt,
                        authorities,
                        principal.getUsername()
                );
            } catch (Exception e) {
                log.error("Error converting JWT: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to convert JWT", e);
            }
        });
    }

    private String getPrincipleClaimName(Jwt jwt) {
        if (principleAttribute != null && jwt.hasClaim(principleAttribute)) {
            return jwt.getClaim(principleAttribute);
        }
        return jwt.getSubject(); // fallback seguro
    }

    private Collection<? extends GrantedAuthority> collectAuthorities(Jwt jwt) {
        return Stream.of(
                jwtGrantedAuthoritiesConverter.convert(jwt),
                ResourceRoleExtractor.extract(jwt, resourceId),
                PermissionExtractor.extract(jwt)
        ).flatMap(Collection::stream).collect(Collectors.toList());
    }
}