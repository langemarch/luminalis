package com.luminalis.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "astro_modulu")
public class AstroModul {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    @Column(nullable = false)
    private String baslik;

    private String altBaslik = "";

    @Column(nullable = false)
    private String tur; // natal, tarot, coffee, dream, synastry

    private String modelAdi = "LuminalisNeuralTextModel";
    private Integer krediMaliyeti = 1;

    public AstroModul() {}

    public AstroModul(Kategori kategori, String baslik, String altBaslik, String tur, String modelAdi, Integer krediMaliyeti) {
        this.kategori = kategori;
        this.baslik = baslik;
        this.altBaslik = altBaslik;
        this.tur = tur;
        this.modelAdi = modelAdi;
        this.krediMaliyeti = krediMaliyeti;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }
    public String getBaslik() { return baslik; }
    public void setBaslik(String baslik) { this.baslik = baslik; }
    public String getAltBaslik() { return altBaslik; }
    public void setAltBaslik(String altBaslik) { this.altBaslik = altBaslik; }
    public String getTur() { return tur; }
    public void setTur(String tur) { this.tur = tur; }
    public String getModelAdi() { return modelAdi; }
    public void setModelAdi(String modelAdi) { this.modelAdi = modelAdi; }
    public Integer getKrediMaliyeti() { return krediMaliyeti; }
    public void setKrediMaliyeti(Integer krediMaliyeti) { this.krediMaliyeti = krediMaliyeti; }
}
