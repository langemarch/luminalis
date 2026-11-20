package com.luminalis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "rapor_urunu")
public class RaporUrunu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rapor_id", nullable = false)
    private AnalizRaporu rapor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modul_id", nullable = false)
    private AstroModul modul;

    private Integer adet = 1;
    private String yorumOzeti = "";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AnalizRaporu getRapor() { return rapor; }
    public void setRapor(AnalizRaporu rapor) { this.rapor = rapor; }
    public AstroModul getModul() { return modul; }
    public void setModul(AstroModul modul) { this.modul = modul; }
    public Integer getAdet() { return adet; }
    public void setAdet(Integer adet) { this.adet = adet; }
    public String getYorumOzeti() { return yorumOzeti; }
    public void setYorumOzeti(String yorumOzeti) { this.yorumOzeti = yorumOzeti; }
}
