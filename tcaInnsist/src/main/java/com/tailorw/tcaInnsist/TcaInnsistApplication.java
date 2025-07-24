package com.tailorw.tcaInnsist;

import com.kynsoft.security.config.SecurityConfig;
import com.kynsoft.security.converter.JwtAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling
public class TcaInnsistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TcaInnsistApplication.class, args);
	}

}
