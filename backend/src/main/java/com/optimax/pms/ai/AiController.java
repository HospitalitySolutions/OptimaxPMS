package com.optimax.pms.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/rate-recommendations")
    public Map<String, Object> rateRecommendations(@RequestParam("propertyId") Long propertyId) {
        return aiService.suggestRates(propertyId);
    }
}

