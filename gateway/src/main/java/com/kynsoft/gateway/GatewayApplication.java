package com.kynsoft.gateway;

import com.kynsoft.security.config.SecurityConfig;
import com.kynsoft.security.converter.JwtAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootApplication(exclude = {
        ReactiveSecurityAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class
})
@Import({
        SecurityConfig.class,
        JwtAuthenticationConverter.class
})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @EventListener
//    public void onApplicationReady(ApplicationReadyEvent event) {
//        log.info("✅ Gateway started correctly");
//
//        var context = event.getApplicationContext();
//
//        // Verificar por TIPO (más confiable)
//        var securityConfigs = context.getBeansOfType(SecurityConfig.class);
//        var jwtConverters = context.getBeansOfType(JwtAuthenticationConverter.class);
//        var corsProperties = context.getBeansOfType(com.kynsoft.security.config.CorsProperties.class);
//
//        log.info("🔐 Security (Manual Import):");
//        log.info("  📦 SecurityConfig: {}", !securityConfigs.isEmpty() ? "✅ LOADED" : "❌ FAIL");
//        log.info("  📦 JwtConverter: {}", !jwtConverters.isEmpty() ? "✅ LOADED" : "❌ FAIL");
//        log.info("  📦 CorsProperties: {}", !corsProperties.isEmpty() ? "✅ LOADED" : "❌ FAIL");
//
//        boolean hasOurFilterChain = context.containsBean("securityWebFilterChain");
//        log.info("  🔒 SecurityWebFilterChain: {}", hasOurFilterChain ? "✅ OUR" : "❌ SPRING BOOT");
//
//        boolean allGood = !securityConfigs.isEmpty() && !jwtConverters.isEmpty() &&
//                !corsProperties.isEmpty() && hasOurFilterChain;
//
//        if (allGood) {
//            log.info("🎉 ¡Security working correctly!");
//            log.info("💡 STABLE AND FUNCTIONAL MANUAL CONFIGURATION");
//        } else {
//            log.warn("⚠️ Security has problems");
//        }
//    }
}