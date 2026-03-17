package com.optimax.pms.auth;

import com.optimax.pms.security.UserPrincipal;
import com.optimax.pms.user.UserConsent;
import com.optimax.pms.user.UserConsentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/gdpr")
public class GdprController {

    private final UserConsentRepository consentRepository;

    public GdprController(UserConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    @PostMapping("/accept-terms")
    public ResponseEntity<Void> acceptTerms(@AuthenticationPrincipal UserPrincipal principal) {
        UserConsent consent = consentRepository.findByUserId(principal.getId())
                .orElseGet(() -> UserConsent.builder().user(principal.toUserEntityRef()).build());
        consent.setTermsAccepted(true);
        consent.setTermsAcceptedAt(Instant.now());
        consentRepository.save(consent);
        return ResponseEntity.ok().build();
    }
}

