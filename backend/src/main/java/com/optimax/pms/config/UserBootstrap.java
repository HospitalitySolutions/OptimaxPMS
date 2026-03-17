package com.optimax.pms.config;

import com.optimax.pms.user.User;
import com.optimax.pms.user.UserRepository;
import com.optimax.pms.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@Configuration
public class UserBootstrap {

    private static final Logger log = LoggerFactory.getLogger(UserBootstrap.class);

    @Bean
    CommandLineRunner createInitialAdmin(UserRepository userRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@optimax.com";
            if (userRepository.existsByEmail(adminEmail)) {
                log.info("Admin user {} already exists, skipping bootstrap.", adminEmail);
                return;
            }

            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.SUPER_ADMIN)
                    .isActive(true)
                    .createdAt(Instant.now())
                    .build();

            userRepository.save(admin);
            log.info("Bootstrapped initial admin user with email {}", adminEmail);
        };
    }
}

