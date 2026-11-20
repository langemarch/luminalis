package com.luminalis.repository;

import com.luminalis.entity.KozmikArsiv;
import com.luminalis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KozmikArsivRepository extends JpaRepository<KozmikArsiv, Long> {
    Optional<KozmikArsiv> findByKullanici(Kullanici kullanici);
}
