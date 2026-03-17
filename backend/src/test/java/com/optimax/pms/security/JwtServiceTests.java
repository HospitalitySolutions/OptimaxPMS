package com.optimax.pms.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtServiceTests {

    @Autowired
    private JwtService jwtService;

    @Test
    void generatesAndParsesToken() {
        String token = jwtService.generateToken("user@example.com", Map.of("k", "v"));
        String subject = jwtService.extractSubject(token);
        assertThat(subject).isEqualTo("user@example.com");
    }
}

