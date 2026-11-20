package com.luminalis.service;

import com.luminalis.entity.ArsivUrunu;
import com.luminalis.entity.AstroModul;
import com.luminalis.entity.KozmikArsiv;
import com.luminalis.entity.Kullanici;
import com.luminalis.repository.ArsivUrunuRepository;
import com.luminalis.repository.AstroModulRepository;
import com.luminalis.repository.KozmikArsivRepository;
import com.luminalis.repository.KullaniciRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KozmikArsivServis {

    private final KullaniciRepository kullaniciRepository;
    private final KozmikArsivRepository arsivRepository;
    private final ArsivUrunuRepository arsivUrunuRepository;
    private final AstroModulRepository modulRepository;

    public KozmikArsivServis(
            KullaniciRepository kullaniciRepository,
            KozmikArsivRepository arsivRepository,
            ArsivUrunuRepository arsivUrunuRepository,
            AstroModulRepository modulRepository
    ) {
        this.kullaniciRepository = kullaniciRepository;
        this.arsivRepository = arsivRepository;
        this.arsivUrunuRepository = arsivUrunuRepository;
        this.modulRepository = modulRepository;
    }

    @Transactional(readOnly = true)
    public List<ArsivUrunu> arsivUrunleriniGetir(String email) {
        Kullanici k = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Bulunamadi: " + email));
        KozmikArsiv arsiv = arsivRepository.findByKullanici(k).orElse(null);
        if (arsiv == null) {
            return List.of();
        }
        return arsivUrunuRepository.findByArsiv(arsiv);
    }

    @Transactional(readOnly = true)
    public int arsivToplaminiHesapla(String email) {
        int toplam = 0;
        for (ArsivUrunu u : arsivUrunleriniGetir(email)) {
            toplam += (u.getAdet() != null ? u.getAdet() : 1);
        }
        return toplam;
    }

    @Transactional
    public ArsivUrunu arsiveEkle(String email, Long modulId) {
        Kullanici k = kullaniciRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Bulunamadi: " + email));
        KozmikArsiv arsiv = arsivRepository.findByKullanici(k).orElse(null);
        if (arsiv == null) {
            arsiv = new KozmikArsiv();
            arsiv.setKullanici(k);
            arsiv = arsivRepository.save(arsiv);
        }
        AstroModul modul = modulRepository.findById(modulId)
                .orElseThrow(() -> new RuntimeException("Analiz modulu bulunamadi: " + modulId));
        ArsivUrunu urun = arsivUrunuRepository.findByArsivAndModul(arsiv, modul).orElse(null);
        if (urun == null) {
            urun = new ArsivUrunu();
            urun.setArsiv(arsiv);
            urun.setModul(modul);
            urun.setAdet(1);
        } else {
            urun.setAdet(urun.getAdet() + 1);
        }
        return arsivUrunuRepository.save(urun);
    }

    @Transactional
    public void arsivdenAzalt(String email, Long urunId) {
        ArsivUrunu urun = arsivUrunuRepository.findById(urunId)
                .orElseThrow(() -> new RuntimeException("Arsiv kalemi bulunamadi"));
        if (!urun.getArsiv().getKullanici().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Yetkisiz islem");
        }
        if (urun.getAdet() > 1) {
            urun.setAdet(urun.getAdet() - 1);
            arsivUrunuRepository.save(urun);
        } else {
            arsivUrunuRepository.delete(urun);
        }
    }

    @Transactional
    public void arsivdenSil(String email, Long urunId) {
        ArsivUrunu urun = arsivUrunuRepository.findById(urunId)
                .orElseThrow(() -> new RuntimeException("Arsiv kalemi bulunamadi"));
        if (!urun.getArsiv().getKullanici().getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Yetkisiz islem");
        }
        arsivUrunuRepository.delete(urun);
    }
}
