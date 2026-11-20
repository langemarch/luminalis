package com.luminalis.repository;

import com.luminalis.entity.GunlukKaydi;
import com.luminalis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GunlukRepository extends JpaRepository<GunlukKaydi, Long> {
    List<GunlukKaydi> findByKullaniciOrderByCreatedAtDesc(Kullanici kullanici);
}
