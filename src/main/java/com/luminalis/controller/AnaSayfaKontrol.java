package com.luminalis.controller;

import com.luminalis.entity.AstroModul;
import com.luminalis.service.AnalizRaporuServis;
import com.luminalis.service.AstroModulServis;
import com.luminalis.service.AstrolojiMotorServis;
import com.luminalis.service.KozmikArsivServis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class AnaSayfaKontrol {

    private final AstroModulServis modulServis;
    private final KozmikArsivServis arsivServis;
    private final AnalizRaporuServis raporServis;
    private final AstrolojiMotorServis astrolojiMotor;

    public AnaSayfaKontrol(
            AstroModulServis modulServis,
            KozmikArsivServis arsivServis,
            AnalizRaporuServis raporServis,
            AstrolojiMotorServis astrolojiMotor
    ) {
        this.modulServis = modulServis;
        this.arsivServis = arsivServis;
        this.raporServis = raporServis;
        this.astrolojiMotor = astrolojiMotor;
    }

    @GetMapping({"/", "/anasayfa"})
    public String anasayfa(
            @RequestParam(defaultValue = "Tümü") String element,
            Model model,
            Principal principal
    ) {
        model.addAttribute("aktifMenu", "anasayfa");
        model.addAttribute("seciliElement", element);
        model.addAttribute("sehirler", astrolojiMotor.tumSehirAdlari());

        List<AstroModul> tumBurclar = modulServis.tumModulleriGetir();
        if (!"Tümü".equalsIgnoreCase(element)) {
            tumBurclar = tumBurclar.stream()
                    .filter(m -> m.getKategori() != null && m.getKategori().getAd().toLowerCase().contains(element.toLowerCase()))
                    .toList();
        }
        model.addAttribute("moduller", tumBurclar);

        if (principal != null) {
            String email = principal.getName();
            model.addAttribute("kullaniciEmail", email);
            model.addAttribute("arsivUrunleri", arsivServis.arsivUrunleriniGetir(email));
            model.addAttribute("toplam", arsivServis.arsivToplaminiHesapla(email));
            model.addAttribute("raporlar", raporServis.kullaniciRaporlariniGetir(email));
        }
        return "anasayfa";
    }

    @GetMapping("/login")
    public String loginFormu() {
        return "login";
    }
}
