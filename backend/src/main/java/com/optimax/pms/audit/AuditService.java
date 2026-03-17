package com.optimax.pms.audit;

import com.optimax.pms.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuditService {

    private final AuditEventRepository repository;

    public AuditService(AuditEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void record(String action, String detailsJson) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            userId = principal.getId();
        }

        AuditEvent event = AuditEvent.builder()
                .occurredAt(Instant.now())
                .userId(userId != null ? userId : -1L)
                .action(action)
                .detailsJson(detailsJson)
                .build();
        repository.save(event);
    }
}

