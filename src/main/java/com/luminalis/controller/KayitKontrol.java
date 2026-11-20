package com.luminalis.controller;

import com.luminalis.service.KullaniciServis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KayitKontrol {

    private final KullaniciServis kullaniciServis;

    public KayitKontrol(KullaniciServis kullaniciServis) {
        this.kullaniciServis = kullaniciServis;
    }

    @GetMapping("/kayit")
    public String kayitFormu() {
        return "kayit";
    }

    @PostMapping("/kayit")
    public String kayitOl(
            @RequestParam String email,
            @RequestParam String adSoyad,
            @RequestParam String sifre,
            Model model
    ) {
        try {
            kullaniciServis.kaydet(email, adSoyad, sifre);
            return "redirect:/login?kayit=ok";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("hata", ex.getMessage());
            return "kayit";
        }
    }
}
