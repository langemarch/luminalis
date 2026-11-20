package com.luminalis.config;

import com.luminalis.entity.AstroModul;
import com.luminalis.entity.Kategori;
import com.luminalis.repository.AstroModulRepository;
import com.luminalis.repository.KategoriRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

    private final KategoriRepository kategoriRepository;
    private final AstroModulRepository modulRepository;

    public DataSeeder(KategoriRepository kategoriRepository, AstroModulRepository modulRepository) {
        this.kategoriRepository = kategoriRepository;
        this.modulRepository = modulRepository;
    }

    private Kategori kategoriBulVeyaOlustur(String ad, String aciklama) {
        return kategoriRepository.findAll().stream()
                .filter(k -> ad.equalsIgnoreCase(k.getAd()))
                .findFirst()
                .orElseGet(() -> kategoriRepository.save(new Kategori(ad, aciklama)));
    }

    @Override
    public void run(String... args) {
        List<Kategori> mevcutKategoriler = kategoriRepository.findAll();
        if (mevcutKategoriler.size() >= 3) {
            mevcutKategoriler.get(0).setAd("Ateş Grubu");
            mevcutKategoriler.get(0).setAciklama("Koç, Aslan ve Yay — Enerji, cesaret ve girişimcilik");
            mevcutKategoriler.get(1).setAd("Toprak Grubu");
            mevcutKategoriler.get(1).setAciklama("Boğa, Başak ve Oğlak — İstikrar, güven ve üretkenlik");
            mevcutKategoriler.get(2).setAd("Hava Grubu");
            mevcutKategoriler.get(2).setAciklama("İkizler, Terazi ve Kova — İletişim, zeka ve sosyal denge");
            kategoriRepository.saveAll(mevcutKategoriler);
        }

        Kategori kAtes = kategoriBulVeyaOlustur("Ateş Grubu", "Koç, Aslan ve Yay — Enerji, cesaret ve girişimcilik");
        Kategori kToprak = kategoriBulVeyaOlustur("Toprak Grubu", "Boğa, Başak ve Oğlak — İstikrar, güven ve üretkenlik");
        Kategori kHava = kategoriBulVeyaOlustur("Hava Grubu", "İkizler, Terazi ve Kova — İletişim, zeka ve sosyal denge");
        Kategori kSu = kategoriBulVeyaOlustur("Su Grubu", "Yengeç, Akrep ve Balık — Sezgi, empati ve duygusal derinlik");

        record BurcSeed(String kod, Kategori kat, String baslik, String ozet, String gezegen) {}

        List<BurcSeed> seeds = List.of(
                new BurcSeed("Koç", kAtes, "Koç Burcu (21 Mart - 20 Nisan)", "Yönetici Gezegen: Mars — İş ve eğitim hayatınızda cesur adımlar atacağınız, enerjinizin yüksek olduğu bir gün.", "Mars"),
                new BurcSeed("Boğa", kToprak, "Boğa Burcu (21 Nisan - 20 Mayıs)", "Yönetici Gezegen: Venüs — Maddi konular, birikimler ve uzun vadeli planlarınızda güven verici gelişmeler var.", "Venüs"),
                new BurcSeed("İkizler", kHava, "İkizler Burcu (21 Mayıs - 21 Haziran)", "Yönetici Gezegen: Merkür — İletişim trafiğiniz hızlanıyor; yakın çevrenizden beklediğiniz önemli bir haber gelebilir.", "Merkür"),
                new BurcSeed("Yengeç", kSu, "Yengeç Burcu (22 Haziran - 22 Temmuz)", "Yönetici Gezegen: Ay — Sezgilerinizin güçlendiği, ev ve aile içi konularda huzurlu adımlar atacağınız bir gün.", "Ay"),
                new BurcSeed("Aslan", kAtes, "Aslan Burcu (23 Temmuz - 22 Ağustos)", "Yönetici Gezegen: Güneş — Sosyal çevrenizde ve projelerinizde liderliğinizle öne çıkacağınız verimli bir gün.", "Güneş"),
                new BurcSeed("Başak", kToprak, "Başak Burcu (23 Ağustos - 22 Eylül)", "Yönetici Gezegen: Merkür — Detay gerektiren işlerde başarı sağlayacağınız, plan ve düzen kurmak için ideal bir gün.", "Merkür"),
                new BurcSeed("Terazi", kHava, "Terazi Burcu (23 Eylül - 23 Ekim)", "Yönetici Gezegen: Venüs — İkili ilişkiler ve ortaklıklarda denge kuracağınız, uzlaşmacı bir gün sizi bekliyor.", "Venüs"),
                new BurcSeed("Akrep", kSu, "Akrep Burcu (24 Ekim - 22 Kasım)", "Yönetici Gezegen: Plüton — Derin analiz gücünüz sayesinde gizli kalan detayları fark edeceğiniz stratejik bir gün.", "Plüton"),
                new BurcSeed("Yay", kAtes, "Yay Burcu (23 Kasım - 21 Aralık)", "Yönetici Gezegen: Jüpiter — Yeni öğrenme fırsatları, seyahat planları ve ufkunuzu genişletecek görüşmeler gündemde.", "Jüpiter"),
                new BurcSeed("Oğlak", kToprak, "Oğlak Burcu (22 Aralık - 20 Ocak)", "Yönetici Gezegen: Satürn — Kariyer hedeflerinizde disiplinli çalışmanızın karşılığını almaya başlayacağınız bir gün.", "Satürn"),
                new BurcSeed("Kova", kHava, "Kova Burcu (21 Ocak - 18 Şubat)", "Yönetici Gezegen: Uranüs — Özgün fikirleriniz ve yenilikçi bakış açınızla çevrenizde fark yaratacaksınız.", "Uranüs"),
                new BurcSeed("Balık", kSu, "Balık Burcu (19 Şubat - 20 Mart)", "Yönetici Gezegen: Neptün — Yaratıcılığınızın ve empati yeteneğinizin yükseldiği, sanatsal konulara açık bir gün.", "Neptün")
        );

        List<AstroModul> mevcutlar = modulRepository.findAll();
        if (mevcutlar.isEmpty()) {
            for (BurcSeed s : seeds) {
                modulRepository.save(new AstroModul(s.kat(), s.baslik(), s.ozet(), s.kod(), s.gezegen(), 1));
            }
        } else {
            // İlk 5 mevcut kaydı koruyarak güncelle (FK bütünlüğü için) ve eksik 7 burcu ekle
            for (int i = 0; i < Math.min(mevcutlar.size(), seeds.size()); i++) {
                AstroModul m = mevcutlar.get(i);
                BurcSeed s = seeds.get(i);
                m.setKategori(s.kat());
                m.setBaslik(s.baslik());
                m.setAltBaslik(s.ozet());
                m.setTur(s.kod());
                m.setModelAdi(s.gezegen());
            }
            modulRepository.saveAll(mevcutlar);

            Map<String, AstroModul> mevcutByTur = mevcutlar.stream()
                    .collect(Collectors.toMap(AstroModul::getTur, m -> m, (a, b) -> a));
            for (BurcSeed s : seeds) {
                if (!mevcutByTur.containsKey(s.kod())) {
                    modulRepository.save(new AstroModul(s.kat(), s.baslik(), s.ozet(), s.kod(), s.gezegen(), 1));
                }
            }
        }
    }
}
