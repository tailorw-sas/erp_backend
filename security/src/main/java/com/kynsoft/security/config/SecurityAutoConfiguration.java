package com.kynsoft.security.config;

import com.kynsoft.security.converter.JwtAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Slf4j
@AutoConfiguration
@ConditionalOnClass({ServerHttpSecurity.class, ReactiveJwtDecoder.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(CorsProperties.class)
@ComponentScan(basePackages = "com.kynsoft.security")
public class SecurityAutoConfiguration {

    public SecurityAutoConfiguration() {
        log.info("🔐 Kynsoft Security Auto-Configuration activada");
    }

    /**
     * Bean condicional del converter JWT
     * Solo se crea si no existe uno personalizado
     */
    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        log.debug("Creando JwtAuthenticationConverter por defecto");
        return new JwtAuthenticationConverter();
    }
}