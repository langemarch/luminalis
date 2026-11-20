package com.luminalis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "analiz_raporu")
public class AnalizRaporu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kullanici_id", nullable = false)
    private Kullanici kullanici;

    private String durum = "ONAYLANDI";
    private Integer toplamAdet = 0;
    private LocalDateTime olusturmaZamani = LocalDateTime.now();

    @OneToMany(mappedBy = "rapor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RaporUrunu> ogeler = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Kullanici getKullanici() { return kullanici; }
    public void setKullanici(Kullanici kullanici) { this.kullanici = kullanici; }
    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }
    public Integer getToplamAdet() { return toplamAdet; }
    public void setToplamAdet(Integer toplamAdet) { this.toplamAdet = toplamAdet; }
    public LocalDateTime getOlusturmaZamani() { return olusturmaZamani; }
    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) { this.olusturmaZamani = olusturmaZamani; }
    public List<RaporUrunu> getOgeler() { return ogeler; }
    public void setOgeler(List<RaporUrunu> ogeler) { this.ogeler = ogeler; }
}
