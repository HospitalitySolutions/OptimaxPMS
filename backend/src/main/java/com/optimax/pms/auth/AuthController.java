package com.optimax.pms.auth;

import com.optimax.pms.security.JwtService;
import com.optimax.pms.security.UserPrincipal;
import com.optimax.pms.user.User;
import com.optimax.pms.user.UserRepository;
import com.optimax.pms.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(
                principal.getUsername(),
                Map.of(
                        "userId", principal.getId(),
                        "role", principal.getRole().name()
                )
        );

        return ResponseEntity.ok(
                new AuthResponse(token, principal.getId(), principal.getUsername(), principal.getRole().name())
        );
    }

    @PostMapping("/register-initial-admin")
    public ResponseEntity<Void> registerInitialAdmin(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User admin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.SUPER_ADMIN)
                .isActive(true)
                .build();

        userRepository.save(admin);
        return ResponseEntity.ok().build();
    }
}

