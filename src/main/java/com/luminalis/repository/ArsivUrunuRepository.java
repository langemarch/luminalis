package com.luminalis.repository;

import com.luminalis.entity.ArsivUrunu;
import com.luminalis.entity.AstroModul;
import com.luminalis.entity.KozmikArsiv;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ArsivUrunuRepository extends JpaRepository<ArsivUrunu, Long> {
    List<ArsivUrunu> findByArsiv(KozmikArsiv arsiv);
    Optional<ArsivUrunu> findByArsivAndModul(KozmikArsiv arsiv, AstroModul modul);
    void deleteByArsiv(KozmikArsiv arsiv);
}
