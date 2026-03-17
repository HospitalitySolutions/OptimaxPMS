package com.optimax.pms.integration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integrations")
public class IntegrationController {

    private final IntegrationConfigRepository configRepository;

    public IntegrationController(IntegrationConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @GetMapping
    public List<IntegrationConfig> list(@RequestParam("propertyId") Long propertyId) {
        return configRepository.findByPropertyId(propertyId);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        // Stubbed response for now – integrations can plug real checks later.
        return Map.of("status", "ok");
    }
}

