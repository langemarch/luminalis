package com.luminalis.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luminalis.config.JwtUtil;
import com.luminalis.entity.Kullanici;
import com.luminalis.service.KullaniciDetayServis;
import com.luminalis.service.KullaniciServis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthApiController {

    private final KullaniciServis kullaniciServis;
    private final KullaniciDetayServis detayServis;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public AuthApiController(
            KullaniciServis kullaniciServis,
            KullaniciDetayServis detayServis,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            ObjectMapper objectMapper
    ) {
        this.kullaniciServis = kullaniciServis;
        this.detayServis = detayServis;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok", "backend", "spring-boot", "database", "mysql");
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> req) {
        try {
            String email = String.valueOf(req.getOrDefault("email", ""));
            String password = String.valueOf(req.getOrDefault("password", ""));
            String name = String.valueOf(req.getOrDefault("name", req.getOrDefault("adSoyad", "")));
            if (email.isBlank() || password.length() < 4) {
                return ResponseEntity.badRequest().body(Map.of("detail", "Gecerli e-posta ve en az 4 karakterli sifre girin"));
            }
            Kullanici k = kullaniciServis.kaydet(email, name, password);
            String token = jwtUtil.createAccessToken(k.getEmail(), k.getRol());
            return ResponseEntity.ok(Map.of("accessToken", token, "token", token, "user", toUserDto(k)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("detail", ex.getMessage()));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> req) {
        String email = String.valueOf(req.getOrDefault("email", "")).toLowerCase().strip();
        String password = String.valueOf(req.getOrDefault("password", ""));
        try {
            UserDetails u = detayServis.loadUserByUsername(email);
            if (!passwordEncoder.matches(password, u.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Hatali e-posta veya sifre"));
            }
            Kullanici k = kullaniciServis.findByEmail(email);
            String token = jwtUtil.createAccessToken(k.getEmail(), k.getRol());
            return ResponseEntity.ok(Map.of("accessToken", token, "token", token, "user", toUserDto(k)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Hatali e-posta veya sifre"));
        }
    }

    @PutMapping("/auth/birth-info")
    public ResponseEntity<?> updateBirthInfo(@RequestBody Map<String, Object> body, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Oturum gerekli"));
        }
        try {
            String json = objectMapper.writeValueAsString(body);
            Kullanici k = kullaniciServis.updateBirthInfo(principal.getName(), json);
            return ResponseEntity.ok(toUserDto(k));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("detail", e.getMessage()));
        }
    }

    private Map<String, Object> toUserDto(Kullanici k) {
        Map<String, Object> birth = Map.of();
        try {
            if (k.getDogumBilgisiJson() != null && !k.getDogumBilgisiJson().isBlank()) {
                birth = objectMapper.readValue(k.getDogumBilgisiJson(), new TypeReference<>() {});
            }
        } catch (Exception ignored) {}
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", k.getId());
        dto.put("email", k.getEmail());
        dto.put("name", k.getAdSoyad());
        dto.put("role", k.getRol());
        dto.put("isPremium", Boolean.TRUE.equals(k.getPremium()));
        dto.put("birthInfo", birth);
        return dto;
    }
}
