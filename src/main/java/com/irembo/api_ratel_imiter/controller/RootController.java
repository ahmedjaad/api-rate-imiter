package com.irembo.api_ratel_imiter.controller;

import com.irembo.api_ratel_imiter.service.RateLimitAlgorithm;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    private final RateLimitAlgorithm rateLimitAlgorithm;
    public RootController(RateLimitAlgorithm rateLimitAlgorithm) {
        this.rateLimitAlgorithm = rateLimitAlgorithm;
    }

    @GetMapping(path = "/hello")
    public ResponseEntity<String> helloWorld(@Param("tokens") int tokens){
        if (rateLimitAlgorithm.tryConsume(tokens)){
          return this.forwardToServer();
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many request");
    }

    private ResponseEntity<String> forwardToServer() {
        return ResponseEntity.ok().body("Hello World");
    }

}
