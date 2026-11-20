package com.luminalis.service;

import com.luminalis.entity.Kullanici;
import com.luminalis.repository.KullaniciRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KullaniciDetayServis implements UserDetailsService {

    private final KullaniciRepository kullaniciRepository;

    public KullaniciDetayServis(KullaniciRepository kullaniciRepository) {
        this.kullaniciRepository = kullaniciRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Kullanici k = kullaniciRepository.findByEmail(email.toLowerCase().strip())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanici bulunamadi: " + email));
        String rol = (k.getRol() != null && !k.getRol().isBlank()) ? k.getRol() : "USER";
        return User.withUsername(k.getEmail())
                .password(k.getSifre())
                .roles(rol)
                .build();
    }
}
