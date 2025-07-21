package com.kynsoft.security.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private boolean corsEnabled = true;
    private List<String> allowedOrigins = Arrays.asList("http://localhost:*",
            "http://127.0.0.1:*",
            "http://192.168.*.*:*");
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH");
    private List<String> allowedHeaders = List.of("Authorization", "Content-Type");

    private Boolean allowCredentials = true;
    private Long maxAge = 3600L;

    public void logConfiguration() {
        log.info("CORS Configuration:");
        log.info("  Enabled: {}", corsEnabled);
        log.info("  Allowed Origins: {}", allowedOrigins);
        log.info("  Allowed Methods: {}", allowedMethods);
        log.info("  Allowed Headers: {}", allowedHeaders);
        log.info("  Allow Credentials: {}", allowCredentials);
        log.info("  Max Age: {}", maxAge);
    }
}