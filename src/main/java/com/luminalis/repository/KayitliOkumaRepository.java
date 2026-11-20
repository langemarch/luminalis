package com.luminalis.repository;

import com.luminalis.entity.KayitliOkuma;
import com.luminalis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface KayitliOkumaRepository extends JpaRepository<KayitliOkuma, Long> {
    List<KayitliOkuma> findByKullaniciOrderByCreatedAtDesc(Kullanici kullanici);
}
