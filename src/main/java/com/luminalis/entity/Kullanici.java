package com.luminalis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kullanici")
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String sifre;

    private String adSoyad = "";
    private String rol = "USER";

    @Column(columnDefinition = "TEXT")
    private String dogumBilgisiJson = "{}";

    private Boolean premium = false;
    private LocalDateTime olusturmaZamani = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSifre() { return sifre; }
    public void setSifre(String sifre) { this.sifre = sifre; }
    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getDogumBilgisiJson() { return dogumBilgisiJson; }
    public void setDogumBilgisiJson(String dogumBilgisiJson) { this.dogumBilgisiJson = dogumBilgisiJson; }
    public Boolean getPremium() { return premium; }
    public void setPremium(Boolean premium) { this.premium = premium; }
    public LocalDateTime getOlusturmaZamani() { return olusturmaZamani; }
    public void setOlusturmaZamani(LocalDateTime olusturmaZamani) { this.olusturmaZamani = olusturmaZamani; }
}
