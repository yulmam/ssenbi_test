package com.haneolenae.bobi.domain.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvironmentController {

    @Value("${server.env}")
    private String env;

    @GetMapping("/env")
    public ResponseEntity<String> getEnvironment() {
        return ResponseEntity.ok(env);
    }
}
