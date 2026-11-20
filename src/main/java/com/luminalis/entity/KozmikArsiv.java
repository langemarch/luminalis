package com.luminalis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kozmik_arsiv")
public class KozmikArsiv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "kullanici_id", unique = true, nullable = false)
    private Kullanici kullanici;

    private LocalDateTime olusturmaZamani = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Kullanici getKullanici() { return kullanici; }
    public void setKullanici(Kullanici kullanici) { this.kullanici = kullanici; }
    public LocalDateTime getOlusturmaZamani() { return olusturmaZamani; }
    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) { this.olusturmaZamani = olusturmaZamani; }
}
