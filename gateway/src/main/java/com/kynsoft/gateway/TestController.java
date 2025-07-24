package com.kynsoft.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("pong");
    }


    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from Gateway");
    }
}
