package com.luminalis.controller;

import com.luminalis.entity.AiIcerikRaporu;
import com.luminalis.repository.AiIcerikRaporuRepository;
import com.luminalis.service.YapayZekaServis;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiApiController {

    private final YapayZekaServis yapayZekaServis;
    private final AiIcerikRaporuRepository reportRepository;

    public AiApiController(YapayZekaServis yapayZekaServis, AiIcerikRaporuRepository reportRepository) {
        this.yapayZekaServis = yapayZekaServis;
        this.reportRepository = reportRepository;
    }

    @PostMapping("/natal")
    public Map<String, Object> natal(@RequestBody Map<String, Object> req) {
        req.put("domain", "natal");
        return yapayZekaServis.callGenerate(req);
    }

    @PostMapping("/tarot")
    public Map<String, Object> tarot(@RequestBody Map<String, Object> req) {
        req.put("domain", "tarot");
        return yapayZekaServis.callGenerate(req);
    }

    @PostMapping("/coffee")
    public Map<String, Object> coffee(@RequestBody Map<String, Object> req) {
        req.put("domain", "coffee");
        return yapayZekaServis.callGenerate(req);
    }

    @PostMapping("/coffee/photo")
    public Map<String, Object> coffeePhoto(@RequestBody Map<String, Object> req) {
        return yapayZekaServis.callCoffeeVision(req);
    }

    @PostMapping("/dream")
    public Map<String, Object> dream(@RequestBody Map<String, Object> req) {
        req.put("domain", "dream");
        return yapayZekaServis.callGenerate(req);
    }

    @PostMapping("/report")
    public Map<String, Object> report(@RequestBody Map<String, Object> req, Principal principal) {
        String email = principal != null ? principal.getName() : null;
        String feature = String.valueOf(req.getOrDefault("feature", "genel"));
        String reason = String.valueOf(req.getOrDefault("reason", ""));
        reportRepository.save(new AiIcerikRaporu(email, feature, reason));
        return Map.of("ok", true);
    }
}
