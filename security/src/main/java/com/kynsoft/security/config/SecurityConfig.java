package com.kynsoft.security.config;

import com.kynsoft.security.converter.JwtAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsProperties.class)
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String jwkSetUri;

    @Value("${security.auth-whitelist:/api/**}")
    private String[] authWhitelist;

    @Value("${security.additional-public-paths:}")
    private String[] additionalPublicPaths;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        corsProperties.logConfiguration();
        List<String> whitelistPaths = new ArrayList<>(Arrays.asList(authWhitelist));
        if (additionalPublicPaths != null && additionalPublicPaths.length > 0) {
            whitelistPaths.addAll(Arrays.asList(additionalPublicPaths));
        }

        log.info("Configuring Security with Public Routes: {}", whitelistPaths);
        log.info("CORS Enabled: {}", corsProperties.isCorsEnabled());

        return httpSecurity
                .cors(cors -> {
                    if (corsProperties.isCorsEnabled()) {
                        log.info("Applying CORS configuration...");
                        cors.configurationSource(corsConfigurationSource());
                    }
                    else {
                        log.warn("CORS is disabled!");
                    }
                })
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/health", "/actuator/health", "/actuator/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/*").permitAll()
                        .pathMatchers(HttpMethod.GET,
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs.yaml",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "webjars/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/identity/api/auth/authenticate").permitAll()
                        .pathMatchers(whitelistPaths.toArray(new String[0])).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        log.info("Configuring ReactiveJwtDecoder with issuer: {}", jwkSetUri);
        return ReactiveJwtDecoders.fromIssuerLocation(jwkSetUri);
    }

    private org.springframework.web.cors.reactive.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedMethods(corsProperties.getAllowedMethods());
        config.setAllowedHeaders(corsProperties.getAllowedHeaders());
        config.setAllowCredentials(corsProperties.getAllowCredentials());
        config.setMaxAge(corsProperties.getMaxAge());

        log.info("CORS Configuration Applied:");
        log.info("  Origin Patterns: {}", corsProperties.getAllowedOrigins());
        log.info("  Methods: {}", corsProperties.getAllowedMethods());
        log.info("  Headers: {}", corsProperties.getAllowedHeaders());

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}