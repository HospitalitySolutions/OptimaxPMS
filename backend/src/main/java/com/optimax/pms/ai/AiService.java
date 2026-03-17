package com.optimax.pms.ai;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {

    public Map<String, Object> suggestRates(Long propertyId) {
        // Stubbed AI suggestion – real implementation can call external ML service.
        return Map.of(
                "propertyId", propertyId,
                "message", "AI rate recommendations not yet implemented"
        );
    }
}

