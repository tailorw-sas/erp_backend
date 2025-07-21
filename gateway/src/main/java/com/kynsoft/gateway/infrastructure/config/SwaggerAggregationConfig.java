package com.kynsoft.gateway.infrastructure.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAggregationConfig {

    /*
     *
        - 🌉 Cloud Bridges
        - 💳 Credit Card Service
        - 🔐 Identity Service
        - 📈 Innsist Service
        - 🧾 Invoice Service
        - 🏦 Payment Service
        - 📊 Report Service
        - ⏰ Scheduler Service
        - ⚙️ Settings Service
    * */
    @Bean
    public GroupedOpenApi gatewayApi() {
        return GroupedOpenApi.builder()
                .group("1-gateway")
                .displayName("Gateway APIs")
                .pathsToMatch("/gateway/**", "/health", "/actuator/**")
                .build();
    }

    @Bean
    public GroupedOpenApi identityApi() {
        return GroupedOpenApi.builder()
                .group("2-identity")
                .displayName("Identity Service")
                .pathsToMatch("/identity/**")
                .build();
    }

    // Agregar más microservicios aquí
    @Bean
    public GroupedOpenApi reportApi() {
        return GroupedOpenApi.builder()
                .group("3-report")
                .displayName("Report Service")
                .pathsToMatch("/report/**")
                .build();
    }
}
