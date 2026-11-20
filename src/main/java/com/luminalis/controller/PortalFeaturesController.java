package com.luminalis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luminalis.entity.GunlukKaydi;
import com.luminalis.entity.KayitliOkuma;
import com.luminalis.entity.Kullanici;
import com.luminalis.entity.SohbetMesaji;
import com.luminalis.repository.GunlukRepository;
import com.luminalis.repository.KayitliOkumaRepository;
import com.luminalis.repository.KullaniciRepository;
import com.luminalis.repository.SohbetMesajiRepository;
import com.luminalis.service.YapayZekaServis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PortalFeaturesController {

    private final KullaniciRepository kullaniciRepository;
    private final KayitliOkumaRepository okumaRepository;
    private final GunlukRepository gunlukRepository;
    private final SohbetMesajiRepository sohbetRepository;
    private final YapayZekaServis yapayZekaServis;
    private final ObjectMapper objectMapper;

    public PortalFeaturesController(
            KullaniciRepository kullaniciRepository,
            KayitliOkumaRepository okumaRepository,
            GunlukRepository gunlukRepository,
            SohbetMesajiRepository sohbetRepository,
            YapayZekaServis yapayZekaServis,
            ObjectMapper objectMapper
    ) {
        this.kullaniciRepository = kullaniciRepository;
        this.okumaRepository = okumaRepository;
        this.gunlukRepository = gunlukRepository;
        this.sohbetRepository = sohbetRepository;
        this.yapayZekaServis = yapayZekaServis;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/readings")
    public ResponseEntity<?> listReadings(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        List<Map<String, Object>> out = new ArrayList<>();
        for (KayitliOkuma r : okumaRepository.findByKullaniciOrderByCreatedAtDesc(k)) {
            out.add(Map.of(
                    "id", r.getId(),
                    "type", r.getType(),
                    "title", r.getTitle(),
                    "content", r.getSummary(),
                    "createdAt", r.getCreatedAt().toString()
            ));
        }
        return ResponseEntity.ok(out);
    }

    @PostMapping("/readings")
    public ResponseEntity<?> createReading(@RequestBody Map<String, Object> req, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        KayitliOkuma r = new KayitliOkuma();
        r.setKullanici(k);
        r.setType(String.valueOf(req.getOrDefault("type", "natal")));
        r.setTitle(String.valueOf(req.getOrDefault("title", "Kozmik Analiz")));
        r.setSummary(String.valueOf(req.getOrDefault("content", "")));
        try {
            r.setDataJson(objectMapper.writeValueAsString(req.getOrDefault("data", Map.of())));
        } catch (Exception ignored) {}
        r = okumaRepository.save(r);
        return ResponseEntity.ok(Map.of("id", r.getId(), "ok", true));
    }

    @GetMapping("/journal")
    public ResponseEntity<?> listJournal(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        List<Map<String, Object>> out = new ArrayList<>();
        for (GunlukKaydi g : gunlukRepository.findByKullaniciOrderByCreatedAtDesc(k)) {
            out.add(Map.of(
                    "id", g.getId(),
                    "date", g.getTitle(),
                    "text", g.getContent()
            ));
        }
        return ResponseEntity.ok(out);
    }

    @PostMapping("/journal")
    public ResponseEntity<?> createJournal(@RequestBody Map<String, Object> req, Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        GunlukKaydi g = new GunlukKaydi();
        g.setKullanici(k);
        g.setTitle(String.valueOf(req.getOrDefault("date", LocalDate.now().toString())));
        g.setContent(String.valueOf(req.getOrDefault("text", "")));
        g = gunlukRepository.save(g);
        return ResponseEntity.ok(Map.of("id", g.getId(), "date", g.getTitle(), "text", g.getContent()));
    }

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody Map<String, Object> req, Principal principal) {
        String sessionId = String.valueOf(req.getOrDefault("session_id", "web-default"));
        String message = String.valueOf(req.getOrDefault("message", ""));
        Kullanici k = (principal != null)
                ? kullaniciRepository.findByEmail(principal.getName()).orElse(null)
                : null;

        SohbetMesaji userMsg = new SohbetMesaji();
        userMsg.setKullanici(k);
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setMessage(message);
        sohbetRepository.save(userMsg);

        Map<String, Object> aiRes = yapayZekaServis.callGenerate(Map.of("domain", "chat", "prompt", message));
        String reply = String.valueOf(aiRes.getOrDefault("text", "Gökyüzü enerjiniz bugün denge ve netlik gösteriyor."));

        SohbetMesaji botMsg = new SohbetMesaji();
        botMsg.setKullanici(k);
        botMsg.setSessionId(sessionId);
        botMsg.setRole("assistant");
        botMsg.setMessage(reply);
        sohbetRepository.save(botMsg);

        return Map.of("reply", reply);
    }
}
