package com.luminalis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "arsiv_urunu")
public class ArsivUrunu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arsiv_id", nullable = false)
    private KozmikArsiv arsiv;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modul_id", nullable = false)
    private AstroModul modul;

    private Integer adet = 1;
    private LocalDateTime eklenmeZamani = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public KozmikArsiv getArsiv() { return arsiv; }
    public void setArsiv(KozmikArsiv arsiv) { this.arsiv = arsiv; }
    public AstroModul getModul() { return modul; }
    public void setModul(AstroModul modul) { this.modul = modul; }
    public Integer getAdet() { return adet; }
    public void setAdet(Integer adet) { this.adet = adet; }
    public LocalDateTime getEklenmeZamani() { return eklenmeZamani; }
    public void setEklenmeZamani(LocalDateTime eklenmeZamani) { this.eklenmeZamani = eklenmeZamani; }
}
