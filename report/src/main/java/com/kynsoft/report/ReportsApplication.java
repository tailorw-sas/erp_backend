package com.kynsoft.report;

import com.kynsoft.security.config.SecurityConfig;
import com.kynsoft.security.converter.JwtAuthenticationConverter;
import jakarta.annotation.PostConstruct;
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
public class ReportsApplication {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(ReportsApplication.class, args);
    }

    @PostConstruct
    public void loadJDBCDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            log.info("The Postgres SQL JDBC Driver was loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The driver for Postgresql JDBC Driver could not be loaded correctly.", e);
        }
    }
}