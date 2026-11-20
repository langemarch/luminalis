package com.luminalis.service;

import com.luminalis.entity.Kullanici;
import com.luminalis.repository.KullaniciRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KullaniciServis {

    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;

    public KullaniciServis(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Kullanici kaydet(String email, String adSoyad, String hamSifre) {
        String normEmail = email.toLowerCase().strip();
        if (kullaniciRepository.existsByEmail(normEmail)) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayitli");
        }
        String encodedPw = passwordEncoder.encode(hamSifre);
        Kullanici k = new Kullanici();
        k.setEmail(normEmail);
        k.setAdSoyad(adSoyad != null ? adSoyad.strip() : "");
        k.setSifre(encodedPw);
        k.setRol("USER");
        return kullaniciRepository.save(k);
    }

    public Kullanici findByEmail(String email) {
        return kullaniciRepository.findByEmail(email.toLowerCase().strip())
                .orElseThrow(() -> new RuntimeException("Kullanici bulunamadi: " + email));
    }

    @Transactional
    public Kullanici updateBirthInfo(String email, String birthInfoJson) {
        Kullanici k = findByEmail(email);
        k.setDogumBilgisiJson(birthInfoJson);
        return kullaniciRepository.save(k);
    }
}
