package com.luminalis.repository;

import com.luminalis.entity.SohbetMesaji;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SohbetMesajiRepository extends JpaRepository<SohbetMesaji, Long> {
    List<SohbetMesaji> findBySessionIdOrderByCreatedAtAsc(String sessionId);
}
