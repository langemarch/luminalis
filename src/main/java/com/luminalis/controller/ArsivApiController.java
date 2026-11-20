package com.luminalis.controller;

import com.luminalis.entity.AnalizRaporu;
import com.luminalis.entity.ArsivUrunu;
import com.luminalis.entity.AstroModul;
import com.luminalis.service.AnalizRaporuServis;
import com.luminalis.service.AstroModulServis;
import com.luminalis.service.KozmikArsivServis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArsivApiController {

    private final AstroModulServis modulServis;
    private final KozmikArsivServis arsivServis;
    private final AnalizRaporuServis raporServis;

    public ArsivApiController(
            AstroModulServis modulServis,
            KozmikArsivServis arsivServis,
            AnalizRaporuServis raporServis
    ) {
        this.modulServis = modulServis;
        this.arsivServis = arsivServis;
        this.raporServis = raporServis;
    }

    @GetMapping("/arsiv/durum")
    public Map<String, Object> durum(Principal principal) {
        return buildDurum(principal);
    }

    @PostMapping("/arsiv/ekle")
    public ResponseEntity<?> ekle(@RequestBody Map<String, Object> req, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Giris gerekli"));
        }
        Long modulId = Long.valueOf(String.valueOf(req.get("modulId")));
        arsivServis.arsiveEkle(principal.getName(), modulId);
        return ResponseEntity.ok(buildDurum(principal));
    }

    @PostMapping("/arsiv/azalt")
    public ResponseEntity<?> azalt(@RequestBody Map<String, Object> req, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Giris gerekli"));
        }
        Long urunId = Long.valueOf(String.valueOf(req.get("urunId")));
        arsivServis.arsivdenAzalt(principal.getName(), urunId);
        return ResponseEntity.ok(buildDurum(principal));
    }

    @PostMapping("/arsiv/sil")
    public ResponseEntity<?> sil(@RequestBody Map<String, Object> req, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Giris gerekli"));
        }
        Long urunId = Long.valueOf(String.valueOf(req.get("urunId")));
        arsivServis.arsivdenSil(principal.getName(), urunId);
        return ResponseEntity.ok(buildDurum(principal));
    }

    @PostMapping("/rapor/olustur")
    public ResponseEntity<?> raporOlustur(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("detail", "Giris gerekli"));
        }
        try {
            AnalizRaporu r = raporServis.raporOlustur(principal.getName());
            Map<String, Object> res = buildDurum(principal);
            res.put("raporId", r.getId());
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("detail", ex.getMessage()));
        }
    }

    private Map<String, Object> buildDurum(Principal principal) {
        List<AstroModul> moduller = modulServis.tumModulleriGetir();
        Map<String, Object> out = new HashMap<>();
        out.put("moduller", moduller);
        if (principal != null) {
            String email = principal.getName();
            List<ArsivUrunu> urunler = arsivServis.arsivUrunleriniGetir(email);
            out.put("arsivUrunleri", urunler);
            out.put("toplam", arsivServis.arsivToplaminiHesapla(email));
            out.put("raporlar", raporServis.kullaniciRaporlariniGetir(email));
        } else {
            out.put("arsivUrunleri", List.of());
            out.put("toplam", 0);
            out.put("raporlar", List.of());
        }
        return out;
    }
}
