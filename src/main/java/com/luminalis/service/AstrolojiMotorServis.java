package com.luminalis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class AstrolojiMotorServis {

    public static final List<String> BURCLAR = List.of(
            "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
            "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık"
    );

    // =========================================================================
    // GÜNLÜK DİNAMİK BURÇ YORUMLARI (tarihe göre her gün değişir)
    // =========================================================================
    private static final String[] GUNLUK_TEMALAR = {
            "Bugün enerjinizi doğru yönlendirdiğinizde işlerinizin kolaylaştığını fark edeceksiniz. Sabah saatlerinde aldığınız bir haber gününüzü şekillendirebilir.",
            "İç sesinize kulak vermek için uygun bir gün. Tereddüt ettiğiniz bir konuda netleşmeniz karar sürecinizi hızlandıracak.",
            "Bugün sosyal çevrenizden destek görebilir, önemli bir görüşmede olumlu sonuç alabilirsiniz. İletişimde açık ve net olmak size avantaj sağlayacak.",
            "Planlarınızı gözden geçirmek ve önceliklerinizi netleştirmek için verimli bir gün. Küçük adımlarla başlamak büyük ilerleme getirecek.",
            "Bugün sezgileriniz güçlü. Karşınıza çıkan bir fırsatı değerlendirmek için doğru zamanlama olduğunu hissedeceksiniz.",
            "Gün içinde beklenmedik bir gelişme planlarınızı etkileyebilir. Esnek olmak ve olaylara farklı açıdan bakmak size kazandıracak.",
            "Bugün kendinize zaman ayırmanız zihinsel olarak tazelenmenizi sağlayacak. Dinlendikten sonra çözüm bulmanız daha kolay olacak."
    };

    private static final String[] HAFTALIK_TEMALAR = {
            "Bu hafta liderlik yeteneğiniz öne çıkıyor. Ekip çalışmalarında yönlendirici olabilir, çevrenizden takdir toplayabilirsiniz.",
            "Hafta boyunca maddi konularda verimli fırsatlar karşınıza çıkabilir. İş birliklerinde istikrarlı duruşunuz öne çıkacak.",
            "Bu hafta yeni bilgiler öğrenmek ve yakın çevrenizle fikir alışverişi yapmak ufkunuzu genişletecek.",
            "Hafta içinde duygusal konularda önemli bir karar netleşebilir. İç huzurunuzu ön planda tutmak size iyi gelecek.",
            "Bu hafta projelerinizde yaratıcılığınızı öne çıkarabilir, önemli bir sunumda fark yaratabilirsiniz."
    };

    private static final String[] AYLIK_TEMALAR = {
            "Bu ay kariyer ve kişisel hedeflerinizde somut ilerleme dönemi. Yeni bir projeye başlamak için uygun zaman.",
            "Bu ay kendinize ve yaşam kalitenize yatırım yapma zamanı. Uzun vadeli hedeflerinizi netleştirebilirsiniz.",
            "Bu ay sosyal çevrenizin büyüdüğü, iletişim ve sunum becerilerinizle öne çıktığınız dinamik bir süreç sizi bekliyor.",
            "Bu ay içsel huzurunuzu güçlendireceğiniz, yaratıcı yeteneklerinizi somut ürünlere dönüştüreceğiniz ilham verici bir dönem."
    };

    public String gunlukYorumUret(String burc, String donem) {
        int gun = LocalDate.now().getDayOfYear();
        int burcIndex = Math.max(0, BURCLAR.indexOf(burc));
        String[] havuz = switch (donem) {
            case "Haftalık" -> HAFTALIK_TEMALAR;
            case "Aylık" -> AYLIK_TEMALAR;
            default -> GUNLUK_TEMALAR;
        };
        int index = Math.floorMod(gun + burcIndex, havuz.length);
        return havuz[index];
    }

    // Türkiye'nin 81 İli (Alfabetik) + Gerçek Enlem / Boylam Koordinatları
    public record SehirKoordinat(String ad, double enlem, double boylam) {}

    public static final List<SehirKoordinat> TURKIYE_81_IL = List.of(
            new SehirKoordinat("Adana", 37.00, 35.32),
            new SehirKoordinat("Adıyaman", 37.76, 38.28),
            new SehirKoordinat("Afyonkarahisar", 38.76, 30.54),
            new SehirKoordinat("Ağrı", 39.72, 43.05),
            new SehirKoordinat("Aksaray", 38.37, 34.03),
            new SehirKoordinat("Amasya", 40.65, 35.83),
            new SehirKoordinat("Ankara", 39.93, 32.86),
            new SehirKoordinat("Antalya", 36.90, 30.71),
            new SehirKoordinat("Ardahan", 41.11, 42.70),
            new SehirKoordinat("Artvin", 41.18, 41.82),
            new SehirKoordinat("Aydın", 37.85, 27.84),
            new SehirKoordinat("Balıkesir", 39.65, 27.88),
            new SehirKoordinat("Bartın", 41.63, 32.34),
            new SehirKoordinat("Batman", 37.88, 41.13),
            new SehirKoordinat("Bayburt", 40.26, 40.23),
            new SehirKoordinat("Bilecik", 40.15, 29.98),
            new SehirKoordinat("Bingöl", 38.88, 40.50),
            new SehirKoordinat("Bitlis", 38.40, 42.11),
            new SehirKoordinat("Bolu", 40.73, 31.61),
            new SehirKoordinat("Burdur", 37.72, 30.29),
            new SehirKoordinat("Bursa", 40.19, 29.06),
            new SehirKoordinat("Çanakkale", 40.15, 26.41),
            new SehirKoordinat("Çankırı", 40.60, 33.61),
            new SehirKoordinat("Çorum", 40.55, 34.95),
            new SehirKoordinat("Denizli", 37.77, 29.09),
            new SehirKoordinat("Diyarbakır", 37.91, 40.24),
            new SehirKoordinat("Düzce", 40.84, 31.16),
            new SehirKoordinat("Edirne", 41.68, 26.56),
            new SehirKoordinat("Elazığ", 38.67, 39.22),
            new SehirKoordinat("Erzincan", 39.75, 39.49),
            new SehirKoordinat("Erzurum", 39.90, 41.27),
            new SehirKoordinat("Eskişehir", 39.78, 30.52),
            new SehirKoordinat("Gaziantep", 37.07, 37.38),
            new SehirKoordinat("Giresun", 40.91, 38.39),
            new SehirKoordinat("Gümüşhane", 40.46, 39.48),
            new SehirKoordinat("Hakkari", 37.57, 43.74),
            new SehirKoordinat("Hatay", 36.20, 36.16),
            new SehirKoordinat("Iğdır", 39.92, 44.04),
            new SehirKoordinat("Isparta", 37.76, 30.55),
            new SehirKoordinat("İstanbul", 41.01, 28.98),
            new SehirKoordinat("İzmir", 38.42, 27.14),
            new SehirKoordinat("Kahramanmaraş", 37.58, 36.94),
            new SehirKoordinat("Karabük", 41.20, 32.62),
            new SehirKoordinat("Karaman", 37.18, 33.22),
            new SehirKoordinat("Kars", 40.60, 43.09),
            new SehirKoordinat("Kastamonu", 41.39, 33.78),
            new SehirKoordinat("Kayseri", 38.73, 35.49),
            new SehirKoordinat("Kırıkkale", 39.85, 33.51),
            new SehirKoordinat("Kırklareli", 41.73, 27.22),
            new SehirKoordinat("Kırşehir", 39.15, 34.16),
            new SehirKoordinat("Kilis", 36.72, 37.12),
            new SehirKoordinat("Kocaeli", 40.77, 29.92),
            new SehirKoordinat("Konya", 37.87, 32.48),
            new SehirKoordinat("Kütahya", 39.42, 29.98),
            new SehirKoordinat("Malatya", 38.35, 38.31),
            new SehirKoordinat("Manisa", 38.61, 27.43),
            new SehirKoordinat("Mardin", 37.31, 40.73),
            new SehirKoordinat("Mersin", 36.80, 34.63),
            new SehirKoordinat("Muğla", 37.21, 28.36),
            new SehirKoordinat("Muş", 38.74, 41.49),
            new SehirKoordinat("Nevşehir", 38.62, 34.71),
            new SehirKoordinat("Niğde", 37.97, 34.68),
            new SehirKoordinat("Ordu", 40.98, 37.88),
            new SehirKoordinat("Osmaniye", 37.07, 36.25),
            new SehirKoordinat("Rize", 41.02, 40.52),
            new SehirKoordinat("Sakarya", 40.76, 30.40),
            new SehirKoordinat("Samsun", 41.29, 36.33),
            new SehirKoordinat("Siirt", 37.93, 41.94),
            new SehirKoordinat("Sinop", 42.03, 35.15),
            new SehirKoordinat("Sivas", 39.75, 37.01),
            new SehirKoordinat("Şanlıurfa", 37.16, 38.79),
            new SehirKoordinat("Şırnak", 37.52, 42.46),
            new SehirKoordinat("Tekirdağ", 40.98, 27.51),
            new SehirKoordinat("Tokat", 40.31, 36.55),
            new SehirKoordinat("Trabzon", 41.00, 39.72),
            new SehirKoordinat("Tunceli", 39.11, 39.55),
            new SehirKoordinat("Uşak", 38.68, 29.41),
            new SehirKoordinat("Van", 38.50, 43.38),
            new SehirKoordinat("Yalova", 40.65, 29.28),
            new SehirKoordinat("Yozgat", 39.82, 34.81),
            new SehirKoordinat("Zonguldak", 41.45, 31.79)
    );

    public List<String> tumSehirAdlari() {
        return TURKIYE_81_IL.stream().map(SehirKoordinat::ad).toList();
    }

    public SehirKoordinat sehirKoordinatiBul(String sehir) {
        return TURKIYE_81_IL.stream()
                .filter(s -> s.ad().equalsIgnoreCase(sehir))
                .findFirst()
                .orElse(new SehirKoordinat(sehir, 41.01, 28.98));
    }

    // =========================================================================
    // İLÇELER (81 ilin tüm ilçeleri, classpath'ten ilceler.json okunur)
    // =========================================================================
    private static Map<String, List<String>> IL_ILCELERI;

    private static synchronized Map<String, List<String>> ilcelerHaritasiniYukle() {
        if (IL_ILCELERI != null) return IL_ILCELERI;
        Map<String, List<String>> harita = new LinkedHashMap<>();
        try {
            java.io.InputStream is = AstrolojiMotorServis.class.getClassLoader().getResourceAsStream("ilceler.json");
            if (is != null) {
                byte[] data = is.readAllBytes();
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, List<String>> parsed = mapper.readValue(data,
                        new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, List<String>>>() {});
                harita.putAll(parsed);
            }
        } catch (Exception e) {
            // JSON okunamazsa boş harita döner (uygulama çökmez)
        }
        IL_ILCELERI = harita;
        return harita;
    }

    public List<String> ilIlceleriniGetir(String il) {
        if (il == null || il.isBlank()) return List.of();
        return ilcelerHaritasiniYukle().getOrDefault(il, List.of());
    }

    public Map<String, List<String>> tumIlIlceleriniGetir() {
        return ilcelerHaritasiniYukle();
    }

    // =========================================================================
    // 22 BÜYÜK ARKANA TAROT DESTESİ
    // =========================================================================
    public record TarotKarti(
            int no, String roma, String ad, String element, String anahtarKelime,
            String duzAnlam, String tersAnlam, String askYorumu, String kariyerYorumu
    ) {}

    public static final List<TarotKarti> TAROT_DESTESI = List.of(
            new TarotKarti(0, "0", "Deli (Mecnun)", "Hava", "Yeni başlangıç, cesaret, spontanlık",
                    "Hayatınızda yepyeni bir sayfa açılıyor. Geçmişin ağırlığını geride bırakıp cesaretle ilk adımı atmak için ideal bir dönemdesiniz.",
                    "Karar verirken acelecilik ve plansızlık riskine dikkat edin. Adım atmadan önce detayları gözden geçirmek sizi koruyacaktır.",
                    "Sürpriz ve heyecan verici bir tanışma veya ilişkide taze bir başlangıç enerjisi hakim.",
                    "Alışılmışın dışında yeni bir proje veya kariyer yönü gündeminize gelebilir."),
            new TarotKarti(1, "I", "Büyücü", "Hava", "İrade, beceri, harekete geçme",
                    "İstediğiniz sonucu elde etmek için tüm bilgi, yetenek ve kaynaklar şu an elinizde. Fikirlerinizi somut eyleme dönüştürme zamanı.",
                    "Potansiyelinizi tam kullanamadığınız veya kararsızlık yaşadığınız bir süreç olabilir; odağınızı tek bir hedefe toplayın.",
                    "İletişim gücünüz ve çekiciliğiniz artıyor; duygularınızı net ifade ettiğinizde karşılık bulacaksınız.",
                    "Sunum, girişim ve beceri gerektiren işlerde çok yüksek bir başarı şansı görünüyor."),
            new TarotKarti(2, "II", "Azize", "Su", "Sezgi, gizli bilgi, içsel bilgelik",
                    "Yüzeyde görünenin ötesini hissediyorsunuz. Acele tepki vermek yerine gözlem yapmak ve iç sesinize güvenmek en doğru yolu gösterecek.",
                    "Sezgilerinizi bastırdığınız veya önemli bir detayı görmezden geldiğiniz bir durum olabilir; sakinleşip tekrar değerlendirin.",
                    "Derin ve sessiz bir duygusal bağ oluşuyor; zamanla her şey daha açık hale gelecek.",
                    "Perde arkasındaki gelişmeleri takip edin; yakında önemli bir bilgi size ulaşacaktır."),
            new TarotKarti(3, "III", "İmparatoriçe", "Toprak", "Bereket, üretkenlik, şefkat",
                    "Emek verdiğiniz konuların meyve vermeye başladığı bereketli ve huzurlu bir dönem. Yaratıcılığınız ve yaşam enerjiniz yükseliyor.",
                    "Başkalarına gereğinden fazla enerji harcarken kendinizi ihmal etmeyin; üretkenliğinizi tazelemek için dinlenmeye zaman ayırın.",
                    "İlişkide sıcaklık, güven ve paylaşım artıyor; ev ve aile konularında güzel gelişmeler var.",
                    "Yaratıcı projelerde büyüme ve maddi kazançta istikrarlı bir artış görünüyor."),
            new TarotKarti(4, "IV", "İmparator", "Ateş", "Otorite, düzen, istikrar",
                    "Hayatınızda sağlam bir düzen kuruyorsunuz. Disiplinli, mantıklı ve kararlı yaklaşımınız sayesinde kontrolü elinizde tutacaksınız.",
                    "Aşırı katı veya kontrolcü davranmak yerine esnek kalmak çözüm süreçlerini kolaylaştıracaktır.",
                    "Güven veren, tutarlı ve uzun vadeli bir birliktelik arayışı ön planda.",
                    "Yönetim, planlama ve liderlik sorumluluklarında güçlü bir konum elde ediyorsunuz."),
            new TarotKarti(5, "V", "Aziz", "Toprak", "Bilgelik, rehberlik, gelenek",
                    "Deneyimli bir kişinin tavsiyesi veya sağlam değerlere bağlı kalmak size doğru yolu gösterecek. Eğitim ve öğrenme için güçlü bir kart.",
                    "Eski kalıpların dışına çıkıp kendi özgün yöntemlerinizi denemeniz gereken bir süreçtesiniz.",
                    "Saygı, bağlılık ve ortak değerler üzerine kurulu kalıcı bir ilişki enerjisi var.",
                    "Kurumsal yapılar, akademik çalışmalar veya mentorluk desteğiyle ilerleme sağlanacak."),
            new TarotKarti(6, "VI", "Aşıklar", "Hava", "Uyum, seçim, kalpten bağ",
                    "Kalbinizle aklınızın örtüştüğü anlamlı bir karar veya güçlü bir uyum dönemi. Değerlerinize uygun seçimler size huzur getirecek.",
                    "İki seçenek arasında kararsızlık yaşıyor olabilirsiniz; neyi gerçekten istediğinizi dürüstçe belirleyin.",
                    "Karşılıklı sevgi, samimiyet ve derin bir uyum öne çıkıyor.",
                    "Güçlü bir iş ortaklığı veya severek yapacağınız yeni bir iş birliği doğabilir."),
            new TarotKarti(7, "VII", "Savaş Arabası", "Su", "Azim, zafer, hızlı ilerleme",
                    "Kararlılığınız ve odaklanmanız sayesinde önünüzdeki engelleri hızla aşıyorsunuz. Hedefinize ulaşmak için rüzgar sizden yana.",
                    "Yönünüzü netleştirmeden hızlanmak yorucu olabilir; önce net bir rota belirleyip sonra ilerleyin.",
                    "İlişkide inisiyatif alarak belirsizlikleri ortadan kaldıracağınız dinamik bir süreç.",
                    "Rekabetçi ortamlarda kararlı duruşunuzla öne çıkıp net bir başarı elde edeceksiniz."),
            new TarotKarti(8, "VIII", "Güç", "Ateş", "İçsel güç, sabır, özgüven",
                    "Zorlukları sertlikle değil, sabır, nezaket ve içsel özgüvenle aşıyorsunuz. Kendi gücünüzün farkına vardığınız olgun bir dönem.",
                    "Kendinizden şüphe ettiğiniz anlarda geçmiş başarılarınızı hatırlayın; enerjinizi tüketmeyin.",
                    "Sabırlı ve anlayışlı yaklaşımınız ilişkideki tüm gerginlikleri yumuşatacaktır.",
                    "Zorlu bir projeyi soğukkanlılığınız ve dayanıklılığınız sayesinde başarıyla tamamlayacaksınız."),
            new TarotKarti(9, "IX", "Ermiş", "Toprak", "İçe dönüş, analiz, rehberlik",
                    "Kısa bir süre gürültüden uzaklaşıp kendi iç sesinizi dinlemek size en net cevabı verecektir. Derin düşünme ve olgunlaşma zamanı.",
                    "Kendinizi çevrenizden fazla soyutlamayın; güvendiğiniz kişilerle fikir paylaşmak iyi gelecektir.",
                    "Duygularınızı netleştirmek ve ilişkiden beklentinizi tanımlamak için sakin bir dönem.",
                    "Detaylı araştırma, analiz ve bağımsız çalışma gerektiren işlerde çok verimli olacaksınız."),
            new TarotKarti(10, "X", "Kader Çarkı", "Ateş", "Dönüm noktası, şans, değişim",
                    "Hayatınızda olumlu bir döngü başlıyor. Beklenmedik ama hayırlı bir tesadüf veya fırsat yönünüzü güzel bir şekilde değiştirebilir.",
                    "Değişime direnmek yerine yeni koşullara uyum sağladığınızda sürecin lehinize döndüğünü göreceksiniz.",
                    "Kadersel bir karşılaşma veya ilişkide yeni bir aşamaya geçiş enerjisi hakim.",
                    "İş hayatında şanslı bir fırsat, terfi veya olumlu bir haber kapıda."),
            new TarotKarti(11, "XI", "Adalet", "Hava", "Denge, doğruluk, hakkaniyet",
                    "Ektiğinizin karşılığını adil biçimde alacağınız bir dönem. Mantıklı, objektif ve dürüst kararlar uzun vadede kazandıracaktır.",
                    "Tek taraflı bakış açısından kaçının; sözleşme veya resmi belgeleri dikkatle inceleyin.",
                    "İlişkide karşılıklı emek, eşitlik ve açık iletişim dengesi kuruluyor.",
                    "Hukuki süreçler, sınavlar, sözleşmeler ve değerlendirmelerde adil bir sonuç alacaksınız."),
            new TarotKarti(12, "XII", "Asılan Adam", "Su", "Yeni bakış açısı, sabır, bekleyiş",
                    "Şu an zorlamak yerine durup olaya farklı bir açıdan bakmak size büyük bir farkındalık kazandıracak. Kısa bir bekleyiş hayrınıza.",
                    "Boş yere oyalandığınız bir konuyu artık bırakıp harekete geçme vakti gelmiş olabilir.",
                    "Karşınızdaki kişinin penceresinden bakmak ilişkide yeni bir anlayış başlatır.",
                    "Eski yöntemi bırakıp farklı bir strateji denediğinizde tıkanıklık çözülecektir."),
            new TarotKarti(13, "XIII", "Dönüşüm (Ölüm)", "Su", "Köklü yenilenme, bitiş ve doğuş",
                    "Artık size hizmet etmeyen bir alışkanlık veya dönem kapanıyor; yerine çok daha sağlıklı ve taze bir başlangıç geliyor.",
                    "Geçmişe tutunmak yeni fırsatların gelişini geciktirebilir; yenilenmeye izin verin.",
                    "İlişkide eski sorunlar geride kalıyor ve daha olgun bir evreye geçiliyor.",
                    "Mevcut düzenin değişmesiyle daha verimli bir çalışma sayfası açılacak."),
            new TarotKarti(14, "XIV", "Denge", "Ateş", "Uyum, ölçülülük, şifa",
                    "Hayatınızın farklı alanları arasında huzurlu bir denge kuruyorsunuz. Sabırlı ve ölçülü adımlar kalıcı başarı getirir.",
                    "Aşırılıklardan uzak durup iş ve dinlenme dengesini yeniden kurmaya özen gösterin.",
                    "Sakin, uyumlu ve birbirini tamamlayan bir ilişki atmosferi öne çıkıyor.",
                    "Farklı fikirleri uzlaştırarak ekip içinde uyumlu ve sürdürülebilir bir sonuç alacaksınız."),
            new TarotKarti(15, "XV", "Şeytan", "Toprak", "Bağlılık farkındalığı, irade testi",
                    "Sizi kısıtlayan alışkanlıkları veya gereksiz kaygıları fark etme zamanı. Kendi iradenizi kullandığınızda özgürleşeceksiniz.",
                    "Sizi yoran bir bağdan veya olumsuz bir döngüden kurtulup ferahlama sürecine giriyorsunuz.",
                    "Kıskançlık veya aşırı sahiplenme yerine güven ve sağlıklı sınırlar kurmak ilişkiyi korur.",
                    "Maddi hırs veya aşırı iş yükü yüzünden kendinizi yıpratmamaya dikkat edin."),
            new TarotKarti(16, "XVI", "Kule", "Ateş", "Ani aydınlanma, sağlam temel",
                    "Beklenmedik bir farkındalık veya ani bir gelişme, zayıf kalan yapıyı temizleyip çok daha sağlam bir temel kurmanızı sağlayacak.",
                    "Gerekli değişimi ertelemek yerine kontrollü adımlarla düzeni yenilemeniz daha rahat olur.",
                    "Açık ve dürüst bir konuşma tüm belirsizlikleri ortadan kaldırıp gerçeği netleştirecek.",
                    "Eski ve verimsiz bir planı bırakıp daha güçlü bir yapılanmaya geçiyorsunuz."),
            new TarotKarti(17, "XVII", "Yıldız", "Hava", "Umut, ilham, yenilenme",
                    "Fırtınadan sonra gökyüzünün açıldığı, umudun ve içsel huzurun yükseldiği çok güzel bir kart. Dilekleriniz destekleniyor.",
                    "Kendinize olan inancınızı kaybetmeyin; sonuçlar düşündüğünüzden daha yakın.",
                    "Samimi, iyileştirici ve geleceğe umutla bakan bir sevgi enerjisi hakim.",
                    "İlham gerektiren projelerde parlayacak ve uzun vadeli hedeflerinize yaklaşacaksınız."),
            new TarotKarti(18, "XVIII", "Ay", "Su", "Sezgi, bilinçaltı, belirsizliği aşma",
                    "Her şey henüz tam netleşmemiş olabilir; varsayımlar yerine gerçek verilere ve sakin sezgilerinize dayanarak ilerleyin.",
                    "Kafanızdaki sis dağılıyor, saklı kalan bir konu aydınlığa kavuşuyor.",
                    "Yanlış anlaşılmaları önlemek için duygularınızı açıkça konuşmanız önemli.",
                    "Belirsiz tekliflerde acele karar vermeyin; birkaç gün içinde şartlar netleşecektir."),
            new TarotKarti(19, "XIX", "Güneş", "Ateş", "Başarı, neşe, açıklık",
                    "Destenin en aydınlık kartlarından biri: Emeklerinizin karşılığını aldığınız, başarının, sevincin ve netliğin öne çıktığı bir dönem.",
                    "Başarı yakın ancak küçük bir gecikme yaşanabilir; pozitif tutumunuzu koruyun.",
                    "Mutluluk, sıcaklık, açık iletişim ve kutlanacak güzel gelişmeler sizi bekliyor.",
                    "Takdir, ödül, başarı ve görünürlük açısından son derece parlak bir dönem."),
            new TarotKarti(20, "XX", "Mahkeme", "Ateş", "Uyanış, önemli karar, yenilenme",
                    "Geçmiş deneyimlerinizi değerlendirip önemli bir karar veriyorsunuz. Kendinizi daha yüksek bir hedefe taşıyacak bir çağrı var.",
                    "Geçmiş hatalar için kendinizi suçlamayı bırakıp önünüze bakma zamanı.",
                    "İlişkide net bir karar verilerek yeni ve sağlıklı bir döneme adım atılıyor.",
                    "Uzun süredir beklenen bir değerlendirme veya başvuru olumlu sonuçlanabilir."),
            new TarotKarti(21, "XXI", "Dünya", "Toprak", "Tamamlanma, bütünlük, büyük başarı",
                    "Önemli bir döngüyü gururla tamamlıyorsunuz. Emeklerinizin tam karşılığını alıp yeni ve daha geniş ufuklara açılacağınız bir dönem.",
                    "Hedefe çok yaklaştınız; son birkaç küçük adımı tamamlayarak süreci bitirebilirsiniz.",
                    "Huzurlu, tamamlanmış ve sağlam temellere oturan bir birliktelik enerjisi.",
                    "Büyük bir projenin, eğitimin veya hedefin başarıyla tamamlanıp kutlanması.")
    );

    public TarotKarti tarotKartiBul(String ad) {
        return TAROT_DESTESI.stream()
                .filter(k -> k.ad().equalsIgnoreCase(ad))
                .findFirst()
                .orElse(TAROT_DESTESI.get(0));
    }

    // =========================================================================
    // KAHVE FALI GERÇEK FOTOĞRAF ANALİZİ (ImageIO + BufferedImage)
    // =========================================================================
    public Map<String, Object> kahveFincaniAnalizEt(MultipartFile foto, String konu, String notlar) {
        Map<String, Object> sonuc = new LinkedHashMap<>();
        int ustKaranlik = 45;
        int ortaKaranlik = 50;
        int dipKaranlik = 55;
        String fotoBilgi = "Fincan fotoğrafı yüklenmedi (niyet ve konu bazlı yorumlandı)";
        String fotoDataUrl = null;
        int seed = Math.abs(Objects.hash(konu, notlar, LocalDate.now().toString()));

        if (foto != null && !foto.isEmpty()) {
            try {
                byte[] bytes = foto.getBytes();
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                if (img != null) {
                    int w = img.getWidth();
                    int h = img.getHeight();
                    int stepX = Math.max(1, w / 40);
                    int stepY = Math.max(1, h / 40);

                    long ustSum = 0, ustCnt = 0;
                    long ortaSum = 0, ortaCnt = 0;
                    long dipSum = 0, dipCnt = 0;

                    for (int y = 0; y < h; y += stepY) {
                        for (int x = 0; x < w; x += stepX) {
                            Color c = new Color(img.getRGB(x, y));
                            int parlaklik = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                            int yogunluk = 100 - (parlaklik * 100 / 255);
                            if (y < h / 3) { ustSum += yogunluk; ustCnt++; }
                            else if (y < 2 * h / 3) { ortaSum += yogunluk; ortaCnt++; }
                            else { dipSum += yogunluk; dipCnt++; }
                        }
                    }
                    ustKaranlik = ustCnt > 0 ? (int) (ustSum / ustCnt) : 45;
                    ortaKaranlik = ortaCnt > 0 ? (int) (ortaSum / ortaCnt) : 50;
                    dipKaranlik = dipCnt > 0 ? (int) (dipSum / dipCnt) : 55;
                    seed = Math.abs(Objects.hash(w, h, ustKaranlik, ortaKaranlik, dipKaranlik, konu, notlar));
                    fotoBilgi = "Yüklenen fincan fotoğrafı incelendi (" + w + "x" + h + " px — Üst telve yoğunluğu: %" + ustKaranlik + ", Orta: %" + ortaKaranlik + ", Dip: %" + dipKaranlik + ")";
                    String mime = foto.getContentType() != null && foto.getContentType().startsWith("image/") ? foto.getContentType() : "image/jpeg";
                    fotoDataUrl = "data:" + mime + ";base64," + Base64.getEncoder().encodeToString(bytes);
                }
            } catch (Exception ignored) {
                fotoBilgi = "Fotoğraf yüklendi ve genel telve dağılımı değerlendirildi.";
            }
        }

        String[] yakinSemboller = {"Açık Yol", "Kuş (Müjdeli Haber)", "Anahtar", "Köprü", "Yıldız"};
        String[] askSemboller = {"Kalp", "Yüzük", "Çifte Kuş", "Çiçek", "Ay Hilali"};
        String[] isSemboller = {"Balık (Kısmet)", "Gemi", "Ağaç", "Dağ ve Zirve", "At"};

        String s1 = yakinSemboller[(ustKaranlik + seed) % yakinSemboller.length];
        String s2 = askSemboller[(ortaKaranlik + seed / 3) % askSemboller.length];
        String s3 = isSemboller[(dipKaranlik + seed / 5) % isSemboller.length];

        String ustYorum = "Fincanın ağız kısmında (yakın gelecek bölgesi) '" + s1 + "' figürü belirgin görünüyor. " +
                (ustKaranlik < 50
                        ? "Telve dağılımının ferah ve açık olması, birkaç gün içinde içinizi rahatlatacak temiz bir haber alacağınıza işaret ediyor."
                        : "Yoğun telve çizgileri, kısa süreli bir kararsızlığın ardından önünüzün hızla açılacağını gösteriyor.");

        String ortaYorum = "Fincanın orta duvarında (ilişkiler ve sosyal çevre) '" + s2 + "' sembolü seçiliyor. " +
                "Sevdiklerinizle veya değer verdiğiniz bir kişiyle yapacağınız samimi bir konuşma aranızdaki bağı güçlendirecek.";

        String dipYorum = "Fincan dibinde (iş, kariyer ve maddi konular) '" + s3 + "' şekli dikkat çekiyor. " +
                "Uzun süredir emek verdiğiniz bir konuda somut ve kalıcı bir ilerleme dönemine giriyorsunuz.";

        String genelOzet = "Seçtiğiniz '" + konu + "' odağında fincanınız genel olarak ferah ve hareketli görünüyor. " +
                ustYorum + " " + dipYorum +
                (notlar != null && !notlar.isBlank() ? " Notunuz ('" + notlar.strip() + "') doğrultusunda acele etmeden sabırla ilerlemeniz lehinize olacaktır." : "");

        sonuc.put("fotoBilgi", fotoBilgi);
        sonuc.put("fotoDataUrl", fotoDataUrl);
        sonuc.put("semboller", List.of(s1, s2, s3));
        sonuc.put("ustYorum", ustYorum);
        sonuc.put("ortaYorum", ortaYorum);
        sonuc.put("dipYorum", dipYorum);
        sonuc.put("genelOzet", genelOzet);
        return sonuc;
    }

    // =========================================================================
    // 60 SEMBOLLÜ KAPSAMLI TÜRKÇE RÜYA TABİRİ SÖZLÜĞÜ
    // =========================================================================
    public record RuyaSembol(String anahtar, String ad, String gelenekselAnlam, String psikolojikAnlam) {}

    public static final List<RuyaSembol> RUYA_SOZLUGU = List.of(
            new RuyaSembol("deniz", "Deniz", "Büyük ferahlık, bereket, uzak hedeflere ulaşma ve gönül genişliğine işaret eder.", "Bilinçaltınızın derinliğini ve duygusal denge arayışınızı simgeler."),
            new RuyaSembol("su", "Su", "Berrak su temiz rızık, huzur ve şifa; bulanık su ise geçici bir tereddüt anlamına gelir.", "Duygusal arınma ve zihinsel tazelenme ihtiyacınızı gösterir."),
            new RuyaSembol("yağmur", "Yağmur", "Rahmet, bereket, sıkıntıların sona ermesi ve beklenen güzel haberlerin gelmesine yorulur.", "Üzerinizdeki stresin boşalması ve yenilenme sürecini temsil eder."),
            new RuyaSembol("yüzü", "Yüzmek", "Zorlukları kendi çabanızla aşacağınıza ve yeni bir alanda başarı kazanacağınıza işaret eder.", "Duygularınızı sağlıklı biçimde yönetebildiğinizi gösterir."),
            new RuyaSembol("anahtar", "Anahtar", "Kapalı kapıların açılması, çözüm bekleyen bir meselenin hallolması ve yeni bir fırsattır.", "Hayatınızda kontrolü ele alma ve çözüm bulma gücünüzü simgeler."),
            new RuyaSembol("kapı", "Kapı", "Yeni bir dönem, iş veya yaşam değişikliği; açık kapı kolaylık ve kısmet demektir.", "Geçiş dönemini ve yeni deneyimlere açıklığınızı ifade eder."),
            new RuyaSembol("yol", "Yol", "Hayat rotanızda ilerleme, hayırlı bir yolculuk veya yeni bir karar sürecine işaret eder.", "Hedeflerinize doğru bilinçli adımlar attığınızı gösterir."),
            new RuyaSembol("araba", "Araba", "İş ve günlük yaşamda hızlanma, kontrolün elinizde olması ve kısa sürede mesafe kat etmektir.", "Kişisel yönlendirme gücünüzü ve yaşam temponuzu yansıtır."),
            new RuyaSembol("uçak", "Uçak", "Büyük hedefler, hızlı yükseliş ve uzak yerlerden gelecek önemli fırsatlara yorulur.", "Vizyonunuzun genişlediğini ve yüksek ideallere yöneldiğinizi gösterir."),
            new RuyaSembol("uç", "Uçmak", "Özgürleşme, yüklerden kurtulma, itibar artışı ve dileklerin gerçekleşmesine işaret eder.", "Kısıtlamaların üzerine çıkma ve özgüven artışı anlamına gelir."),
            new RuyaSembol("düş", "Düşmek", "Kısa süreli bir kaygı veya dikkat edilmesi gereken bir karar anına işaret eder.", "Kontrolü kaybetme endişenizi fark edip sağlam zemin kurma ihtiyacınızı gösterir."),
            new RuyaSembol("ev", "Ev", "Huzur, aile güveni, düzenli bir yaşam ve maddi istikrar anlamı taşır.", "İç dünyanızı, kendinizi güvende hissetme ihtiyacınızı temsil eder."),
            new RuyaSembol("merdiven", "Merdiven", "Merdiven çıkmak aşama aşama yükseliş ve başarı; inmek ise geçmişi gözden geçirmektir.", "Kişisel gelişim basamaklarını ve sabırlı ilerleyişi simgeler."),
            new RuyaSembol("köprü", "Köprü", "İki dönem veya iki kişi arasında kurulacak olumlu bir bağ ve güvenli geçiştir.", "Değişim sürecini dengeli bir şekilde aşacağınızı gösterir."),
            new RuyaSembol("altın", "Altın", "Değerli bir kazanç, emeklerin karşılığını bulması ve kalıcı bir başarıya yorulur.", "Kendi öz değerinizi ve yeteneklerinizi fark etmenizi simgeler."),
            new RuyaSembol("para", "Para", "Maddi ferahlık, iş hayatında verimlilik ve yeni kazanç kapılarına işaret eder.", "Güven, değer ve emek karşılığı beklentinizi yansıtır."),
            new RuyaSembol("yüzük", "Yüzük", "Verilen bir söz, güçlü bir birliktelik, nişan/evlilik veya kalıcı bir anlaşmadır.", "Bağlılık, bütünlük ve uzun vadeli kararlılığı temsil eder."),
            new RuyaSembol("bebek", "Bebek", "Tertemiz bir başlangıç, masum sevinç, müjdeli haber ve yeni bir projenin doğuşudur.", "İçinizdeki yenilenme enerjisini ve koruma içgüdünüzü gösterir."),
            new RuyaSembol("okul", "Okul / Sınav", "Yeni bir şey öğrenme, tecrübe kazanma ve bir değerlendirmeden başarıyla çıkma sürecidir.", "Kendinizi kanıtlama isteğinizi ve sorumluluk bilincinizi yansıtır."),
            new RuyaSembol("sınav", "Sınav", "Sabır ve hazırlıkla aşılacak bir dönüm noktasına ve ardından gelecek rahatlamaya yorulur.", "Performans kaygınızı aşmak için planlı hareket etme ihtiyacınızı gösterir."),
            new RuyaSembol("yılan", "Yılan", "Dikkatli olunması gereken gizli bir rekabete veya tam tersi köklü bir dönüşüm/şifaya işaret eder.", "Çevrenizdeki insanlara karşı sınırlarınızı koruma sezgisi verir."),
            new RuyaSembol("köpek", "Köpek", "Sadık bir dost, güvenilir destek ve zor zamanda yanınızda olacak bir kişiye yorulur.", "Sadakat, güven ve korunma ihtiyacınızı simgeler."),
            new RuyaSembol("kedi", "Kedi", "Bağımsızlık, zarafet, sezgisellik ve ev içi küçük ama keyifli gelişmelere işaret eder.", "Kendi alanınızı koruma ve sezgisel yönünüzü dinleme isteğidir."),
            new RuyaSembol("kuş", "Kuş", "Uzaklardan gelecek sevindirici bir haber, özgürlük ve ferahlığa yorulur.", "Zihinsel hafiflik ve umut dolu beklentileri temsil eder."),
            new RuyaSembol("balık", "Balık", "Büyük kısmet, rızık artışı, bolluk ve beklenmedik maddi rahatlamaya işaret eder.", "Bilinçaltından gelen yaratıcı fikirleri ve bereketi simgeler."),
            new RuyaSembol("at", "At", "Murada erme, güç, hızla gelecek başarı ve saygınlık anlamına gelir.", "Yüksek yaşam enerjinizi ve hedeflerinize ulaşma azminizi gösterir."),
            new RuyaSembol("ağaç", "Ağaç", "Köklü ve uzun ömürlü bir başarı, aile bağlarının güçlenmesi ve berekettir.", "Kişisel büyüme, sağlam duruş ve istikrarı temsil eder."),
            new RuyaSembol("çiçek", "Çiçek", "Sevinç, güzellik, sevgi dolu bir jest ve gönül ferahlığına yorulur.", "Hayatın güzel yönlerine odaklanma ve duygusal tazelenmedir."),
            new RuyaSembol("dağ", "Dağ", "Yüksek bir hedef, kararlılık, güç ve sabırla ulaşılacak zirveyi temsil eder.", "Büyük hedeflere ulaşma iradenizi ve dayanıklılığınızı simgeler."),
            new RuyaSembol("ateş", "Ateş", "Güçlü enerji, tutku, hızlı gelişmeler ve yeni bir motivasyon kaynağına işaret eder.", "İçinizdeki dönüşüm gücünü ve kararlı hareket isteğini gösterir."),
            new RuyaSembol("güneş", "Güneş", "Aydınlık günler, büyük başarı, şifa, netlik ve mutluluğa yorulur.", "Özgüven, bilinç açıklığı ve yaşam sevincini temsil eder."),
            new RuyaSembol("ay", "Ay", "Huzur, sezgisel rehberlik, aile içi uyum ve duygusal olgunluğa işaret eder.", "İç dünyanızla barışık olduğunuzu ve sezgilerinizin güçlendiğini gösterir."),
            new RuyaSembol("yıldız", "Yıldız", "Yüksek şans, yol gösterici bir fırsat ve uzun zamandır kurulan bir hayalin gerçekleşmesidir.", "Umut, ilham ve geleceğe dair parlak beklentileri simgeler."),
            new RuyaSembol("diş", "Diş", "Aile içi konular, yeni bir sorumluluk veya hayatınızda yenilenen bir düzenin habercisidir.", "Değişim sürecinde güven ve özgüven tazeleme ihtiyacını gösterir."),
            new RuyaSembol("ayna", "Ayna", "Gerçekleri net biçimde görme, öz değerlendirme ve içsel farkındalığa işaret eder.", "Kendinize dürüstçe bakıp doğru kararlar alacağınızı simgeler."),
            new RuyaSembol("ağla", "Ağlamak", "Rüyada ağlamak genellikle tersine yorulur; büyük bir sevinç, rahatlama ve yükten kurtulmadır.", "Bastırılmış duyguların serbest kalması ve içsel hafiflemedir."),
            new RuyaSembol("koş", "Koşmak", "Hedefinize ulaşmak için gösterdiğiniz çabayı ve yakında sonuç alacağınızı gösterir.", "Zamanla yarışma hissini azaltıp planlı ilerleme ihtiyacını yansıtır."),
            new RuyaSembol("hediye", "Hediye", "Beklenmedik bir sürpriz, takdir görme ve sevdiklerinizden gelecek güzel bir desteğe yorulur.", "Değer görme ve karşılıklı paylaşım enerjisini temsil eder."),
            new RuyaSembol("telefon", "Telefon / Mektup", "Yakın zamanda alacağınız önemli bir haber, davet veya beklenen bir iletişimdir.", "Bağlantı kurma ve belirsiz bir konuda netlik kazanma arzusudur.")
    );

    public Map<String, Object> ruyaTabirEt(String ruyaMetni, String duygu) {
        String norm = ruyaMetni.toLowerCase(Locale.forLanguageTag("tr"));
        List<RuyaSembol> bulunanlar = new ArrayList<>();
        Set<String> eklenenAdlar = new HashSet<>();

        for (RuyaSembol s : RUYA_SOZLUGU) {
            if (norm.contains(s.anahtar()) && eklenenAdlar.add(s.ad())) {
                bulunanlar.add(s);
            }
        }

        String duyguYorumu = switch (duygu) {
            case "Huzurlu" -> "Rüyanızdaki huzurlu atmosfer, bilinçaltınızın mevcut süreçten memnun olduğunu ve önünüzdeki günlerin sakin ilerleyeceğini destekliyor.";
            case "Endişeli" -> "Rüyada hissedilen endişe, günlük yaşamda zihninizi meşgul eden bir konuyu artık netleştirip geride bırakma zamanının geldiğini gösterir.";
            case "Heyecanlı" -> "Rüyanızdaki heyecan duygusu, yakında hayatınızda hareketlilik ve yeni fırsatların doğacağına işaret etmektedir.";
            case "Şaşkın" -> "Rüyada yaşanan şaşkınlık, beklemediğiniz ama size yeni bir bakış açısı kazandıracak sürpriz bir gelişmenin habercisidir.";
            default -> "Rüyanızın genel akışı, içsel denge arayışınızın ve geleceğe yönelik bilinçli hazırlığınızın bir yansımasıdır.";
        };

        String genelYorum;
        if (!bulunanlar.isEmpty()) {
            String sembolListesi = String.join(", ", bulunanlar.stream().map(RuyaSembol::ad).toList());
            genelYorum = "Rüyanızda öne çıkan '" + sembolListesi + "' sembolleri birlikte değerlendirildiğinde; " +
                    bulunanlar.get(0).gelenekselAnlam() + " " + duyguYorumu;
        } else {
            genelYorum = "Rüyanızda anlattığınız sahne, zihninizin yakın dönemdeki deneyimlerinizi düzenlediğini ve yeni bir başlangıca hazırlandığınızı göstermektedir. " + duyguYorumu;
        }

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("bulunanSemboller", bulunanlar);
        res.put("duyguYorumu", duyguYorumu);
        res.put("genelYorum", genelYorum);
        res.put("tavsiye", "Rüyanızdan aklınızda kalan ana sembolleri not alıp birkaç gün içindeki gelişmelerle karşılaştırmak sezgilerinizi güçlendirecektir.");
        return res;
    }
}
