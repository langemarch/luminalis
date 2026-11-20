package com.luminalis.service;

import com.luminalis.entity.*;
import com.luminalis.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnalizRaporuServis {

    private final KullaniciRepository kullaniciRepository;
    private final KozmikArsivRepository arsivRepository;
    private final ArsivUrunuRepository arsivUrunuRepository;
    private final AnalizRaporuRepository raporRepository;
    private final RaporUrunuRepository raporUrunuRepository;

    public AnalizRaporuServis(
            KullaniciRepository kullaniciRepository,
            KozmikArsivRepository arsivRepository,
            ArsivUrunuRepository arsivUrunuRepository,
            AnalizRaporuRepository raporRepository,
            RaporUrunuRepository raporUrunuRepository
    ) {
        this.kullaniciRepository = kullaniciRepository;
        this.arsivRepository = arsivRepository;
        this.arsivUrunuRepository = arsivUrunuRepository;
        this.raporRepository = raporRepository;
        this.raporUrunuRepository = raporUrunuRepository;
    }

    @Transactional
    public AnalizRaporu raporOlustur(String email) {
        Kullanici k = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadi"));
        KozmikArsiv arsiv = arsivRepository.findByKullanici(k)
                .orElseThrow(() -> new RuntimeException("Takip listeniz bos"));
        List<ArsivUrunu> kalemler = arsivUrunuRepository.findByArsiv(arsiv);
        if (kalemler.isEmpty()) {
            throw new RuntimeException("Ozet olusturmak icin once takip listenize burc ekleyin");
        }

        AnalizRaporu rapor = new AnalizRaporu();
        rapor.setKullanici(k);
        rapor.setDurum("HAZIRLANDI");
        int toplam = 0;
        for (ArsivUrunu u : kalemler) {
            toplam += (u.getAdet() != null ? u.getAdet() : 1);
        }
        rapor.setToplamAdet(toplam);
        rapor = raporRepository.save(rapor);

        for (ArsivUrunu u : kalemler) {
            RaporUrunu ru = new RaporUrunu();
            ru.setRapor(rapor);
            ru.setModul(u.getModul());
            ru.setAdet(u.getAdet());
            ru.setYorumOzeti(u.getModul().getBaslik() + " (Oncelik: " + u.getAdet() + ")");
            raporUrunuRepository.save(ru);
        }

        arsivUrunuRepository.deleteAll(kalemler);
        return raporRepository.findById(rapor.getId()).orElse(rapor);
    }

    @Transactional(readOnly = true)
    public List<AnalizRaporu> kullaniciRaporlariniGetir(String email) {
        Kullanici k = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadi"));
        return raporRepository.findByKullaniciOrderByOlusturmaZamaniDesc(k);
    }
}
