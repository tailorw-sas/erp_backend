package com.kynsoft.finamer.settings;

import com.kynsoft.security.config.SecurityConfig;
import com.kynsoft.security.converter.JwtAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication
        (exclude = {
                ReactiveSecurityAutoConfiguration.class,
                ReactiveOAuth2ResourceServerAutoConfiguration.class
        })
@Import({
        SecurityConfig.class,
        JwtAuthenticationConverter.class
})
public class SettingsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SettingsApplication.class, args);
    }
}
