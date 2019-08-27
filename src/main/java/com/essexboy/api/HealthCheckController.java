package com.essexboy.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HealthCheckController {
    
    @RequestMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<String>("it works!", HttpStatus.OK);
    }
    
}

