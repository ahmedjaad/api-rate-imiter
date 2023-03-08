package com.irembo.api_rate_limiter.api_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// http://localhost:8082/api_service/api/v1/hello
@RestController
@RequestMapping("/api_service/api/v1")
public class ApiServiceHelloWorld {
    @GetMapping("hello")
    ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok().body("Hello World");
    }
}
