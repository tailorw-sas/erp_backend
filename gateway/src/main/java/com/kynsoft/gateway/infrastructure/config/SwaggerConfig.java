package com.kynsoft.gateway.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🏢 ERP Gateway API")
                        .version("1.0.0")
                        .description("""
                                **API Gateway for ERP**

                                Centralizes all ERP microservices:
                                - 🌉 Cloud Bridges
                                - 💳 Credit Card Service
                                - 🔐 Identity Service
                                - 📈 Innsist Service
                                - 🧾 Invoice Service
                                - 🏦 Payment Service
                                - 📊 Report Service
                                - ⏰ Scheduler Service
                                - ⚙️ Settings Service
                                
                                **Para autenticarse:**
                                1. Use the endpoint `/identity/api/auth/authenticate`
                                2. Copy the JWT token from the response
                                3. Click "Authorize" 🔒 above
                                4. Paste the token (without 'Bearer')
                                """)
                        .contact(new Contact()
                                .name("TailorWare Development Team")
                                .email("admin@tailorw.com")
                                .url("https://tailorw.com"))
                        .license(new License()
                                .name("TailorWare License")
                                .url("https://tailorw.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Gateway Local Development"),
                        new Server()
                                .url("https://api-qa.tailorw.com")
                                .description("Gateway QA Environment"),
                        new Server()
                                .url("https://api.tailorw.com")
                                .description("Gateway Production")))
                // SEGURIDAD JWT
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("""
                                                **Autenticación JWT**
                                                
                                                1. Get your token from `/identity/api/auth/authenticate`
                                                2. Copy the value of the `token` field
                                                3. Paste it here (without the 'Bearer' prefix)
                                                
                                                Ejemplo: `eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...`
                                                """)));
    }
}