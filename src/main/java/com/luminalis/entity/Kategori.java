package com.luminalis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kategori")
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ad;

    private String aciklama = "";

    @JsonIgnore
    @OneToMany(mappedBy = "kategori", cascade = CascadeType.ALL)
    private List<AstroModul> moduller = new ArrayList<>();

    public Kategori() {}

    public Kategori(String ad, String aciklama) {
        this.ad = ad;
        this.aciklama = aciklama;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAd() { return ad; }
    public void setAd(String ad) { this.ad = ad; }
    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }
    public List<AstroModul> getModuller() { return moduller; }
    public void setModuller(List<AstroModul> moduller) { this.moduller = moduller; }
}
