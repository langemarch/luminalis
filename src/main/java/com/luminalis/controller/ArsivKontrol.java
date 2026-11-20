package com.luminalis.controller;

import com.luminalis.service.AnalizRaporuServis;
import com.luminalis.service.KozmikArsivServis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ArsivKontrol {

    private final KozmikArsivServis arsivServis;
    private final AnalizRaporuServis raporServis;

    public ArsivKontrol(KozmikArsivServis arsivServis, AnalizRaporuServis raporServis) {
        this.arsivServis = arsivServis;
        this.raporServis = raporServis;
    }

    @PostMapping({"/takip/ekle", "/arsiv/ekle"})
    public String takipListesineEkle(@RequestParam Long modulId, Principal principal) {
        arsivServis.arsiveEkle(principal.getName(), modulId);
        return "redirect:/";
    }

    @PostMapping({"/takip/azalt", "/arsiv/azalt"})
    public String takipOnceliginiAzalt(@RequestParam Long urunId, Principal principal) {
        arsivServis.arsivdenAzalt(principal.getName(), urunId);
        return "redirect:/";
    }

    @PostMapping({"/takip/sil", "/arsiv/sil"})
    public String takipListesindenCikar(@RequestParam Long urunId, Principal principal) {
        arsivServis.arsivdenSil(principal.getName(), urunId);
        return "redirect:/";
    }

    @PostMapping({"/ozet/olustur", "/rapor/olustur"})
    public String gokyuzuOzetiOlustur(Principal principal) {
        raporServis.raporOlustur(principal.getName());
        return "redirect:/";
    }
}
