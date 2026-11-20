package com.luminalis.repository;

import com.luminalis.entity.AnalizRaporu;
import com.luminalis.entity.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalizRaporuRepository extends JpaRepository<AnalizRaporu, Long> {
    List<AnalizRaporu> findByKullaniciOrderByOlusturmaZamaniDesc(Kullanici kullanici);
}
