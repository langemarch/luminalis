package com.luminalis.controller;

import com.luminalis.entity.GunlukKaydi;
import com.luminalis.entity.KayitliOkuma;
import com.luminalis.entity.Kullanici;
import com.luminalis.repository.GunlukRepository;
import com.luminalis.repository.KayitliOkumaRepository;
import com.luminalis.repository.KullaniciRepository;
import com.luminalis.service.AstrolojiMotorServis;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class WebSayfaKontrol {

    private static final List<String> BURCLAR = AstrolojiMotorServis.BURCLAR;

    private record BurcProfili(
            String ad, String tarih, String element, String nitelik, String gezegen,
            String uyumluBurclar, String ugurluRenk, String odak, String tavsiye,
            String gunluk, String haftalik, String aylik,
            String ask, String kariyer, String saglik
    ) {}

    private static final Map<String, BurcProfili> BURC_PROFILLERI = new LinkedHashMap<>();

    static {
        List<BurcProfili> profiller = List.of(
                new BurcProfili("Koç", "21 Mart - 20 Nisan", "Ateş", "Öncü", "Mars",
                        "Aslan, Yay, Kova", "Kırmızı", "Girişimcilik, hızlı kararlar ve yeni başlangıçlar",
                        "Enerjinizi tek bir hedefe odakladığınızda engelleri kolayca aşacaksınız.",
                        "Bugün iş ve eğitim hayatınızda inisiyatif almak için güçlü bir gökyüzü desteği altındasınız. Uzun süredir ertelediğiniz bir görüşmeyi başlatmak size yeni kapılar açabilir.",
                        "Bu hafta liderlik yönünüz ön plana çıkıyor. Hafta ortasına doğru ekip çalışmalarında yönlendirici rol üstlenebilir, hafta sonunda ise sosyal planlarla enerji toplayabilirsiniz.",
                        "Bu ay kariyer ve kişisel hedeflerinizde somut ilerleme dönemi. Mars'ın desteğiyle yeni bir projeye başlamak ve finansal düzenlemeler yapmak için uygun zamanlar var.",
                        "İlişkilerde açık ve dürüst iletişim sizi yakınlaştırıyor. Bekar Koçlar için sosyal ortamlarda sürpriz tanışmalar gündeme gelebilir.",
                        "Hızlı çözüm üretme beceriniz yöneticilerin ve çalışma arkadaşlarınızın dikkatini çekiyor.",
                        "Yüksek fiziksel enerjinizi düzenli yürüyüş veya sporla dengelemek zihninizi de rahatlatacaktır."),
                new BurcProfili("Boğa", "21 Nisan - 20 Mayıs", "Toprak", "Sabit", "Venüs",
                        "Başak, Oğlak, Yengeç", "Zümrüt Yeşili", "Maddi güven, sabır ve kalıcı değerler",
                        "Acele kararlar yerine adım adım ve planlı ilerlemek size kazandıracaktır.",
                        "Bugün finansal konular, bütçe planlaması ve ev düzeni önceliğinizde. Sabırlı yaklaşımınız sayesinde beklediğiniz bir konuda güven verici bir sonuç alabilirsiniz.",
                        "Hafta boyunca maddi konularda verimli fırsatlar karşınıza çıkabilir. Özellikle iş birliklerinde istikrarlı duruşunuz takdir toplayacak.",
                        "Bu ay kendinize ve yaşam kalitenize yatırım yapma zamanı. Uzun vadeli hedeflerinizi netleştirip sağlam temeller atabileceğiniz bir dönemdesiniz.",
                        "Sevgi ve sadakat arayışınız artıyor. Partnerinizle sakin ve keyifli vakit geçirmek ilişkinizi güçlendirecektir.",
                        "Detaylı ve istikrarlı çalışmanız sayesinde sorumluluk alanınızda kalıcı başarılar elde edebilirsiniz.",
                        "Beslenme düzeninize ve uyku kalitenize özen göstermek günlük performansınızı doğrudan artırır."),
                new BurcProfili("İkizler", "21 Mayıs - 21 Haziran", "Hava", "Değişken", "Merkür",
                        "Terazi, Kova, Koç", "Sarı", "İletişim, eğitim ve çok yönlü projeler",
                        "Aynı anda pek çok işe dağılmak yerine en önemli iki konuya öncelik verin.",
                        "Bugün telefon, e-posta ve yüz yüze görüşmeler açısından oldukça hareketli bir gündesiniz. Zihinsel kıvraklığınız sayesinde karmaşık bir meseleyi hızla çözüme kavuşturabilirsiniz.",
                        "Hafta içinde yeni eğitimler, kısa yolculuklar ve yakın çevreyle yapılacak fikir alışverişleri ufkunuzu genişletecek.",
                        "Bu ay sosyal çevrenizin büyüdüğü, iletişim ve sunum becerilerinizle öne çıktığınız dinamik bir süreç sizi bekliyor.",
                        "Sohbeti keyifli, entelektüel paylaşımların güçlü olduğu bir dönemdesiniz. Duygularınızı net ifade etmek yanlış anlaşılmaları önler.",
                        "Sunum, yazışma ve ekip koordinasyonu gerektiren işlerde çok verimli sonuçlar alabilirsiniz.",
                        "Zihinsel yorgunluğu azaltmak için akşam saatlerinde ekran süresini sınırlamanız faydalı olur."),
                new BurcProfili("Yengeç", "22 Haziran - 22 Temmuz", "Su", "Öncü", "Ay",
                        "Akrep, Balık, Boğa", "Gümüş Beyazı", "Aile bağları, sezgi ve içsel huzur",
                        "Başkalarının sorumluluklarını üstlenirken kendi ihtiyaçlarınızı ihmal etmeyin.",
                        "Bugün sezgileriniz son derece güçlü çalışıyor. Ev, aile ve sevdiklerinizle ilgili konularda yapacağınız yapıcı konuşmalar ortamı yumuşatacaktır.",
                        "Haftanın ilk yarısında duygusal konularda netlik kazanıyorsunuz. Hafta sonu kendinize ve evinize vakit ayırmak size çok iyi gelecek.",
                        "Bu ay içsel güven duygunuzu pekiştireceğiniz, hem iş hem özel yaşamda köklü ve huzurlu düzenlemeler yapacağınız bir dönem.",
                        "Şefkatli ve anlayışlı tavrınız partneriniz tarafından derinden hissediliyor. Geçmiş kırgınlıkları geride bırakmak için güzel bir gün.",
                        "Sezgisel öngörüleriniz ve koruyucu liderliğiniz ekip içindeki güven ortamını güçlendiriyor.",
                        "Su tüketiminizi artırmak ve sakinleştirici aktivitelerle stresi azaltmak enerjinizi yükseltir."),
                new BurcProfili("Aslan", "23 Temmuz - 22 Ağustos", "Ateş", "Sabit", "Güneş",
                        "Koç, Yay, Terazi", "Altın Sarısı", "Özgüven, yaratıcılık ve liderlik",
                        "Parladığınız anlarda çevrenizdekilerin fikirlerine de alan açmak başarınızı ikiye katlar.",
                        "Bugün kendinizi ifade etme gücünüz zirvede. Katılacağınız toplantılarda veya sosyal ortamlarda karizmanız ve üretken fikirlerinizle kolayca dikkat çekebilirsiniz.",
                        "Bu hafta yaratıcı projeler ve sahne önüne çıkacağınız görevler için harika fırsatlar var. Kendinize olan inancınız başarıyı beraberinde getiriyor.",
                        "Bu ay hedeflerinizi büyütme ve yeteneklerinizi görünür kılma zamanı. Özellikle kariyer ve sosyal statü alanında olumlu gelişmeler yaşanabilir.",
                        "Romantik ve cömert tavrınız ilişkide sıcak bir atmosfer yaratıyor. Özel bir jest karşı tarafı çok mutlu edebilir.",
                        "Proje yönetimi ve sunumlarda özgüvenli duruşunuzla belirleyici bir rol üstleniyorsunuz.",
                        "Kalp ve sırt sağlığınızı destekleyen hafif esneme hareketleri gün boyu dinç kalmanızı sağlar."),
                new BurcProfili("Başak", "23 Ağustos - 22 Eylül", "Toprak", "Değişken", "Merkür",
                        "Boğa, Oğlak, Yengeç", "Lacivert", "Düzen, analiz ve verimlilik",
                        "Mükemmeliyetçilik yüzünden kendinize gereğinden fazla yüklenmeyin; ilerleme kusursuzluktan değerlidir.",
                        "Bugün planlama yapmak, yarım kalan dosyaları toparlamak ve detay gerektiren işleri tamamlamak için en verimli günlerinizden biri.",
                        "Hafta boyunca çalışma düzeninizde yapacağınız küçük iyileştirmeler size ciddi zaman kazandıracak.",
                        "Bu ay üretkenliğinizin arttığı, sağlık ve iş alışkanlıklarınızı daha sistemli hale getireceğiniz verimli bir dönemdesiniz.",
                        "İlişkilerde küçük ama anlamlı destekleriniz güven duygusunu pekiştiriyor. Eleştirel dilden uzak durmak uyumu artırır.",
                        "Analitik bakış açınız ve dikkatli kontrolleriniz sayesinde kritik hataları önceden fark edebilirsiniz.",
                        "Düzenli beslenme ve sindirim sistemini yormayan hafif öğünler gün içi enerjinizi dengeler."),
                new BurcProfili("Terazi", "23 Eylül - 23 Ekim", "Hava", "Öncü", "Venüs",
                        "İkizler, Kova, Aslan", "Pastel Mavi", "Denge, diplomasi ve iş birlikleri",
                        "Herkesi memnun etmeye çalışırken kendi önceliğinizi netleştirmekten çekinmeyin.",
                        "Bugün ikili ilişkilerde ve ortak çalışmalarda uzlaşmacı tavrınızla kilit rol oynuyorsunuz. Estetik ve sanatla ilgili konular size ilham verebilir.",
                        "Bu hafta anlaşmalar, iş birlikleri ve sosyal davetler gündeminizde olacak. Diplomatik diliniz sayesinde pürüzleri kolayca aşacaksınız.",
                        "Bu ay yaşamınızda denge kurma, ilişkileri güçlendirme ve karşılıklı faydaya dayalı ortaklıklar geliştirme ayı.",
                        "Uyumlu ve zarif yaklaşımınız sevgi bağlarınızı derinleştiriyor. Partnerinizle ortak bir plan yapmak için ideal bir gün.",
                        "Müzakere, müşteri ilişkileri ve ekip uyumu gerektiren alanlarda öne çıkıyorsunuz.",
                        "Gün içinde kısa molalar verip zihinsel dengeyi korumak baş ağrısı ve gerginliği önler."),
                new BurcProfili("Akrep", "24 Ekim - 22 Kasım", "Su", "Sabit", "Plüton",
                        "Yengeç, Balık, Başak", "Bordo", "Derin analiz, kararlılık ve dönüşüm",
                        "Kontrol edemediğiniz detayları akışa bırakmak içsel gücünüzü daha da artırır.",
                        "Bugün odaklanma ve araştırma gücünüz çok yüksek. Yüzeyde görünmeyen ayrıntıları sezerek stratejik ve doğru kararlar verebilirsiniz.",
                        "Hafta içinde önemli bir konuda netleşme ve dönüşüm yaşayabilirsiniz. Finansal kaynaklarınızı akıllıca yöneteceğiniz bir süreçtesiniz.",
                        "Bu ay kendinizi yenileme, gereksiz yüklerden kurtulma ve uzun vadeli hedeflerinize kararlılıkla sarılma dönemi.",
                        "Derin ve tutkulu bağlar ön planda. Güven temelli açık konuşmalar ilişkinizi bir üst seviyeye taşıyabilir.",
                        "Kriz yönetimi ve karmaşık problemleri çözme konusundaki başarınız takdir topluyor.",
                        "Uyku düzeninize dikkat etmek ve zihni boşaltan sakin yürüyüşler yapmak size iyi gelecektir."),
                new BurcProfili("Yay", "23 Kasım - 21 Aralık", "Ateş", "Değişken", "Jüpiter",
                        "Koç, Aslan, Kova", "Mor", "Keşif, vizyon ve yeni öğrenmeler",
                        "Büyük resmi görürken günlük küçük sorumlulukları da takvimde ihmal etmeyin.",
                        "Bugün iyimserliğiniz ve öğrenme merakınız çevrenize de pozitif enerji yayıyor. Yeni bir eğitim, kurs veya seyahat fikri gündeminize gelebilir.",
                        "Bu hafta ufkunuzu genişletecek insanlarla tanışabilir, akademik veya uluslararası bağlantılı konularda güzel haberler alabilirsiniz.",
                        "Bu ay hareketli, keşif dolu ve vizyonunuzu büyüten gelişmelerle geçecek. Cesur ama ölçülü adımlar atabilirsiniz.",
                        "Neşeli ve samimi tavrınız ilişkilerde canlılık yaratıyor. Ortak bir gezi veya etkinlik bağlarınızı tazeleyebilir.",
                        "Stratejik düşünme ve yeni fırsatları görme yeteneğiniz iş hayatında avantaj sağlıyor.",
                        "Açık havada vakit geçirmek ve tempolu yürüyüş yapmak enerjinizi tazeleyecektir."),
                new BurcProfili("Oğlak", "22 Aralık - 20 Ocak", "Toprak", "Öncü", "Satürn",
                        "Boğa, Başak, Akrep", "Koyu Gri", "Disiplin, sorumluluk ve uzun vadeli başarı",
                        "Çalışma temponuzun yanında dinlenmeye ve sevdiklerinize de planlı zaman ayırın.",
                        "Bugün kararlı ve disiplinli duruşunuzla önemli görevleri zamanında tamamlıyorsunuz. Uzun süredir emek verdiğiniz bir konuda somut ilerleme görebilirsiniz.",
                        "Hafta boyunca kariyer ve yöneticilerle ilişkiler ön planda. Sorumluluk bilinciniz size yeni yetkiler ve güven kazandırabilir.",
                        "Bu ay hedeflerinize adım adım yaklaştığınız, profesyonel alanda kalıcı ve saygın sonuçlar alacağınız güçlü bir dönem.",
                        "Güvenilir ve tutarlı davranışlarınız ilişkide sağlam bir zemin oluşturuyor. Duygularınızı biraz daha görünür kılmak sıcaklığı artırır.",
                        "Zaman yönetimi ve kurumsal projelerde sistem kurma becerinizle örnek gösteriliyorsunuz.",
                        "Eklem ve duruş sağlığınız için masa başında uzun süre hareketsiz kalmamaya özen gösterin."),
                new BurcProfili("Kova", "21 Ocak - 18 Şubat", "Hava", "Sabit", "Uranüs",
                        "İkizler, Terazi, Yay", "Turkuaz", "Yenilikçilik, teknoloji ve topluluk projeleri",
                        "Özgün fikirlerinizi uygulamaya geçirirken somut bir yol haritası oluşturun.",
                        "Bugün alışılmışın dışında çözümler üreterek çevrenizi şaşırtabilirsiniz. Teknoloji, yazılım ve ekip projeleri için son derece verimli bir gün.",
                        "Bu hafta arkadaş çevreniz ve ortak hedefler paylaştığınız gruplarla verimli iş birlikleri doğabilir.",
                        "Bu ay yeniliklere açık olacağınız, özgün projelerinizi hayata geçireceğiniz ve sosyal çevrenizden destek göreceğiniz bir dönem.",
                        "Arkadaşlık ve anlayış temelli ilişkiler sizi mutlu ediyor. Özgürlük ve bağlılık dengesini rahatça kurabilirsiniz.",
                        "Yenilikçi yaklaşımınız ve modern araçları etkin kullanmanız iş süreçlerini hızlandırıyor.",
                        "Düzensiz uyku saatlerinden kaçınmak ve düzenli esneme yapmak zindeliğinizi korur."),
                new BurcProfili("Balık", "19 Şubat - 20 Mart", "Su", "Değişken", "Neptün",
                        "Yengeç, Akrep, Boğa", "Deniz Yeşili", "Empati, sanat ve sezgisel kavrayış",
                        "Hayal gücünüzü gerçekçi planlarla desteklediğinizde çok güzel sonuçlar elde edersiniz.",
                        "Bugün empati yeteneğiniz ve yaratıcı ilhamınız oldukça yüksek. Sanatsal çalışmalar, tasarım veya yardım odaklı konularda anlamlı adımlar atabilirsiniz.",
                        "Hafta içinde iç sesinize kulak vererek önemli bir kararınızı netleştireceksiniz. Yakın dostlarınızdan manevi destek görebilirsiniz.",
                        "Bu ay içsel huzurunuzu güçlendireceğiniz, yaratıcı yeteneklerinizi somut ürünlere dönüştüreceğiniz ilham verici bir dönem.",
                        "Romantik, incelikli ve anlayışlı tavrınız karşınızdaki kişiyi derinden etkiliyor.",
                        "Yaratıcı çözümler ve insan odaklı çalışmalar gerektiren alanlarda fark yaratıyorsunuz.",
                        "Dinlenmeye zaman ayırmak ve sakin müzik veya yürüyüşle zihni dinlendirmek size iyi gelir.")
        );
        for (BurcProfili p : profiller) {
            BURC_PROFILLERI.put(p.ad(), p);
        }
    }

    private static final Map<String, String> TAROT_ANLAMLARI = Map.ofEntries(
            Map.entry("Büyücü", "yeteneklerinizi ve elinizdeki imkanları harekete geçirerek yeni bir girişim başlatma gücüne sahip olduğunuzu"),
            Map.entry("Azize", "iç sesinize ve sezgilerinize güvenmeniz gerektiğini, zamanla gizli kalan detayların netleşeceğini"),
            Map.entry("İmparator", "disiplin, düzen ve kararlı bir planlama ile kontrolü elinizde tuttuğunuzu"),
            Map.entry("Kader Çarkı", "hayatınızda olumlu bir dönüm noktasına ve yeni fırsatların açıldığı bir döngüye girdiğinizi"),
            Map.entry("Güç", "sabır, özgüven ve içsel dengeyle zorlu bir süreci başarıyla yönettiğinizi"),
            Map.entry("Ermiş", "kısa bir içe dönüş ve bilinçli değerlendirme sonrası doğru yolu net biçimde göreceğinizi"),
            Map.entry("Savaş Arabası", "odaklanma ve azim sayesinde hedeflerinize hızla ilerleyeceğinizi"),
            Map.entry("Yıldız", "umut, yenilenme ve uzun vadeli dileklerinizin desteklendiği ferah bir dönemi"),
            Map.entry("Güneş", "açıklık, başarı, sevinç ve emeklerinizin görünür karşılığını alacağınız aydınlık bir sonucu"),
            Map.entry("Dünya", "önemli bir sürecin başarıyla tamamlanacağını ve yeni bir aşamaya geçeceğinizi"),
            Map.entry("Aşıklar", "uyumlu bir seçim, güçlü bir bağ ve kalbinizle aklınızın örtüştüğü bir kararı"),
            Map.entry("İmparatoriçe", "bereket, üretkenlik ve hem maddi hem manevi anlamda gelişim yaşanacağını")
    );

    private static final Map<String, String> KAHVE_SEMBOLLERI = Map.ofEntries(
            Map.entry("Yol", "yakın zamanda planladığınız bir yolculuğa veya hayatınızda yeni bir yön çizmeye"),
            Map.entry("Kuş", "uzaktan gelecek sevindirici ve beklenen bir habere"),
            Map.entry("Anahtar", "uzun süredir çözüm bekleyen bir konunun kilidinin açılmasına ve yeni bir fırsata"),
            Map.entry("Ağaç", "köklü, kalıcı ve zamanla büyüyen bereketli bir gelişmeye"),
            Map.entry("Balık", "maddi anlamda kısmetin açılmasına ve beklenmedik bir kazanca"),
            Map.entry("Yüzük", "kalıcı bir söz, güçlü bir birliktelik veya resmi bir anlaşmaya"),
            Map.entry("Gemi", "büyük bir hedefin size doğru yaklaşmasına ve ferah bir sürece"),
            Map.entry("Dağ", "sabır ve kararlılıkla aşılacak önemli bir hedefe ve ardından gelecek başarıya"),
            Map.entry("Kalp", "duygusal yakınlaşmaya, samimi bir sevgi paylaşımına ve huzura"),
            Map.entry("At", "hızlı ilerleyen gelişmelere, güç ve murada ermeye"),
            Map.entry("Köprü", "iki dönem veya iki kişi arasında kurulacak olumlu bir bağa"),
            Map.entry("Yıldız", "şansın sizden yana dönmesine ve dileğinizin gerçekleşme yoluna girmesine")
    );

    private final KullaniciRepository kullaniciRepository;
    private final KayitliOkumaRepository okumaRepository;
    private final GunlukRepository gunlukRepository;
    private final AstrolojiMotorServis astrolojiMotor;

    public WebSayfaKontrol(
            KullaniciRepository kullaniciRepository,
            KayitliOkumaRepository okumaRepository,
            GunlukRepository gunlukRepository,
            AstrolojiMotorServis astrolojiMotor
    ) {
        this.kullaniciRepository = kullaniciRepository;
        this.okumaRepository = okumaRepository;
        this.gunlukRepository = gunlukRepository;
        this.astrolojiMotor = astrolojiMotor;
    }

    private void ortakModelDoldur(Model model, Principal principal, String aktifMenu) {
        model.addAttribute("aktifMenu", aktifMenu);
        if (principal != null) {
            model.addAttribute("kullaniciEmail", principal.getName());
        }
    }

    private String tarihIleGunesBurcuBul(String tarihStr) {
        try {
            LocalDate d = LocalDate.parse(tarihStr);
            int m = d.getMonthValue();
            int day = d.getDayOfMonth();
            if ((m == 3 && day >= 21) || (m == 4 && day <= 20)) return "Koç";
            if ((m == 4 && day >= 21) || (m == 5 && day <= 20)) return "Boğa";
            if ((m == 5 && day >= 21) || (m == 6 && day <= 21)) return "İkizler";
            if ((m == 6 && day >= 22) || (m == 7 && day <= 22)) return "Yengeç";
            if ((m == 7 && day >= 23) || (m == 8 && day <= 22)) return "Aslan";
            if ((m == 8 && day >= 23) || (m == 9 && day <= 22)) return "Başak";
            if ((m == 9 && day >= 23) || (m == 10 && day <= 23)) return "Terazi";
            if ((m == 10 && day >= 24) || (m == 11 && day <= 22)) return "Akrep";
            if ((m == 11 && day >= 23) || (m == 12 && day <= 21)) return "Yay";
            if ((m == 12 && day >= 22) || (m == 1 && day <= 20)) return "Oğlak";
            if ((m == 1 && day >= 21) || (m == 2 && day <= 18)) return "Kova";
            return "Balık";
        } catch (Exception e) {
            return "Aslan";
        }
    }

    // -------------------------------------------------------------------------
    // 1. BURÇ YORUMLARI (/burclar)
    // -------------------------------------------------------------------------
    @GetMapping("/burclar")
    public String burclarSayfasi(
            @RequestParam(defaultValue = "Koç") String burc,
            @RequestParam(defaultValue = "Günlük") String donem,
            Model model,
            Principal principal
    ) {
        ortakModelDoldur(model, principal, "burclar");
        BurcProfili profil = BURC_PROFILLERI.getOrDefault(burc, BURC_PROFILLERI.get("Koç"));

        model.addAttribute("burclar", BURCLAR);
        model.addAttribute("seciliBurc", profil.ad());
        model.addAttribute("seciliDonem", donem);
        model.addAttribute("tarihAraligi", profil.tarih());
        model.addAttribute("element", profil.element());
        model.addAttribute("nitelik", profil.nitelik());
        model.addAttribute("gezegen", profil.gezegen());
        model.addAttribute("uyumluBurclar", profil.uyumluBurclar());
        model.addAttribute("ugurluRenk", profil.ugurluRenk());
        model.addAttribute("odakKonusu", profil.odak());
        model.addAttribute("tavsiye", profil.tavsiye());
        model.addAttribute("askYorumu", profil.ask());
        model.addAttribute("kariyerYorumu", profil.kariyer());
        model.addAttribute("saglikYorumu", profil.saglik());

        String genelYorum = astrolojiMotor.gunlukYorumUret(profil.ad(), donem);
        model.addAttribute("genelYorum", genelYorum);

        int seed = Math.abs(Objects.hash(profil.ad(), donem, LocalDate.now().toString()));
        model.addAttribute("ugurluSayi", (seed % 9) + 1);
        model.addAttribute("askPuani", 74 + (seed % 23));
        model.addAttribute("kariyerPuani", 76 + ((seed / 3) % 21));
        model.addAttribute("saglikPuani", 72 + ((seed / 5) % 24));
        return "burclar";
    }

    // -------------------------------------------------------------------------
    // 2. DOĞUM HARİTASI (/dogum-haritasi & /dogum-haritasi/hesapla)
    // -------------------------------------------------------------------------
    @GetMapping("/api/ilceler")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> ilcelerEndpoint(@RequestParam String il) {
        return java.util.Map.of("il", il, "ilceler", astrolojiMotor.ilIlceleriniGetir(il));
    }

    @GetMapping("/dogum-haritasi")
    public String dogumHaritasiFormu(Model model, Principal principal) {
        ortakModelDoldur(model, principal, "harita");
        model.addAttribute("sehirler", astrolojiMotor.tumSehirAdlari());
        model.addAttribute("ilceler", astrolojiMotor.ilIlceleriniGetir("İstanbul"));
        haritaHesaplaVeEkle("1998-05-15", "14:30", "İstanbul", model);
        return "dogum-haritasi";
    }

    @PostMapping({"/dogum-haritasi", "/dogum-haritasi/hesapla"})
    public String dogumHaritasiHesapla(
            @RequestParam String dogumTarihi,
            @RequestParam String dogumSaati,
            @RequestParam String dogumYeri,
            @RequestParam(defaultValue = "") String dogumIlce,
            Model model,
            Principal principal
    ) {
        ortakModelDoldur(model, principal, "harita");
        model.addAttribute("sehirler", astrolojiMotor.tumSehirAdlari());
        model.addAttribute("ilceler", astrolojiMotor.ilIlceleriniGetir(dogumYeri));

        String konum = dogumYeri;
        if (dogumIlce != null && !dogumIlce.isBlank()) {
            konum = dogumIlce + ", " + dogumYeri;
        }
        final String finalKonum = konum;
        haritaHesaplaVeEkle(dogumTarihi, dogumSaati, finalKonum, model);

        if (principal != null) {
            kullaniciRepository.findByEmail(principal.getName()).ifPresent(k -> {
                KayitliOkuma okuma = new KayitliOkuma();
                okuma.setKullanici(k);
                okuma.setType("Doğum Haritası");
                okuma.setTitle("Doğum Haritası (" + finalKonum + " - " + dogumTarihi + " " + dogumSaati + ")");
                okuma.setSummary(String.valueOf(model.getAttribute("haritaOzeti")));
                okumaRepository.save(okuma);
                model.addAttribute("kayitMesaji", "Doğum haritası analizi hesabınıza kaydedildi.");
            });
        }
        return "dogum-haritasi";
    }

    private void haritaHesaplaVeEkle(String tarih, String saat, String sehir, Model model) {
        AstrolojiMotorServis.SehirKoordinat koord = astrolojiMotor.sehirKoordinatiBul(sehir);
        String gunesBurcu = tarihIleGunesBurcuBul(tarih);
        int gunesIdx = BURCLAR.indexOf(gunesBurcu);
        int saatInt = 12;
        int dakikaInt = 0;
        try {
            String[] parca = saat.split(":");
            saatInt = Integer.parseInt(parca[0]);
            if (parca.length > 1) dakikaInt = Integer.parseInt(parca[1]);
        } catch (Exception ignored) {}

        // Gerçek boylam farkı (Türkiye referansı 30° Doğu'ya göre dakika kayması)
        double yerelSaat = saatInt + (dakikaInt / 60.0) + ((koord.boylam() - 30.0) / 15.0);
        int yukselenKayma = (int) Math.floor(((yerelSaat - 6.0 + 24.0) % 24.0) / 2.0);
        int seed = Math.abs(Objects.hash(tarih, saat, sehir));
        int yukselenIdx = (gunesIdx + yukselenKayma) % 12;
        int ayIdx = (gunesIdx + (seed % 11) + 1) % 12;
        int mcIdx = (yukselenIdx + 9) % 12;

        String yukselen = BURCLAR.get(yukselenIdx);
        String ayBurcu = BURCLAR.get(ayIdx);
        String tepeNoktasi = BURCLAR.get(mcIdx);

        String[] gezegenAdlari = {"Güneş", "Ay", "Merkür", "Venüs", "Mars", "Jüpiter", "Satürn", "Uranüs", "Neptün", "Plüton"};
        String[] anlamlar = {
                "Temel kimlik, yaşam amacı ve bilinçli irade",
                "Duygusal tepkiler, içgüdüler ve güven ihtiyacı",
                "İletişim tarzı, öğrenme ve düşünce yapısı",
                "İlişkiler, sevgi dili ve estetik anlayışı",
                "Hareket, cesaret, mücadele ve girişim gücü",
                "Büyüme, şans, bolluk ve öğrenme alanı",
                "Sorumluluk, disiplin, sabır ve yaşam dersleri",
                "Özgünlük, yenilikçilik ve ani değişimler",
                "Hayal gücü, sanat ve sezgisel derinlik",
                "Köklü dönüşüm, kararlılık ve yeniden yapılanma"
        };

        int ates = 0, toprak = 0, hava = 0, su = 0;
        List<Map<String, Object>> gezegenler = new ArrayList<>();
        for (int i = 0; i < gezegenAdlari.length; i++) {
            String burc;
            if (i == 0) burc = gunesBurcu;
            else if (i == 1) burc = ayBurcu;
            else if (i == 2) burc = BURCLAR.get((gunesIdx + (seed % 3) - 1 + 12) % 12);
            else if (i == 3) burc = BURCLAR.get((gunesIdx + ((seed / 2) % 3) - 1 + 12) % 12);
            else burc = BURCLAR.get((seed + i * 5) % 12);

            String el = BURC_PROFILLERI.get(burc).element();
            if ("Ateş".equals(el)) ates++;
            else if ("Toprak".equals(el)) toprak++;
            else if ("Hava".equals(el)) hava++;
            else su++;

            int derece = (seed + i * 11) % 29 + 1;
            int burcIdx = BURCLAR.indexOf(burc);
            int ev = ((burcIdx - yukselenIdx + 12) % 12) + 1;
            gezegenler.add(Map.of(
                    "ad", gezegenAdlari[i],
                    "burc", burc,
                    "element", el,
                    "derece", derece + "°",
                    "ev", ev + ". Ev",
                    "aciklama", anlamlar[i]
            ));
        }

        List<Map<String, Object>> acilar = List.of(
                Map.of("gezegen1", "Güneş (" + gunesBurcu + ")", "gezegen2", "Ay (" + ayBurcu + ")", "aciTuru", "Üçgen (120°)", "orb", "1.6°", "etki", "Bilinçli irade ve duygular arasında doğal bir denge ve uyum"),
                Map.of("gezegen1", "Venüs", "gezegen2", "Mars", "aciTuru", "Altmışlık (60°)", "orb", "2.1°", "etki", "Sosyal ilişkilerde sıcak iletişim ve yaratıcı enerji"),
                Map.of("gezegen1", "Merkür", "gezegen2", "Satürn", "aciTuru", "Kavuşum (0°)", "orb", "0.9°", "etki", "Analitik düşünce, sistemli planlama ve çalışma disiplini")
        );

        BurcProfili gunesProfil = BURC_PROFILLERI.get(gunesBurcu);
        model.addAttribute("dogumTarihi", tarih);
        model.addAttribute("dogumSaati", saat);
        model.addAttribute("dogumYeri", sehir);
        model.addAttribute("koordinatBilgi", String.format(Locale.US, "%.2f° K, %.2f° D", koord.enlem(), koord.boylam()));
        model.addAttribute("gunesBurcu", gunesBurcu);
        model.addAttribute("ayBurcu", ayBurcu);
        model.addAttribute("yukselenBurc", yukselen);
        model.addAttribute("tepeNoktasi", tepeNoktasi);
        model.addAttribute("atesYuzde", ates * 10);
        model.addAttribute("toprakYuzde", toprak * 10);
        model.addAttribute("havaYuzde", hava * 10);
        model.addAttribute("suYuzde", su * 10);
        model.addAttribute("gezegenler", gezegenler);
        model.addAttribute("acilar", acilar);
        model.addAttribute("haritaOzeti",
                sehir + " (" + String.format(Locale.US, "%.2f° K, %.2f° D", koord.enlem(), koord.boylam()) + " — " + tarih + " " + saat +
                ") koordinatlarına göre Güneş burcunuz " + gunesBurcu + " (" + gunesProfil.element() + " Grubu), Ay burcunuz " + ayBurcu +
                " ve Yükselen burcunuz " + yukselen + " olarak hesaplanmıştır. Tepe Noktası (MC) " + tepeNoktasi + " burcundadır.");
    }

    // -------------------------------------------------------------------------
    // 3. TAROT (22 KART), KAHVE FALI (FOTOĞRAF ANALİZİ) VE RÜYA TABİRİ (/fal)
    // -------------------------------------------------------------------------
    private void falOrtakModel(Model model, Principal principal, String aktifSekme) {
        ortakModelDoldur(model, principal, "fal");
        model.addAttribute("aktifSekme", aktifSekme);
        model.addAttribute("tarotDestesi", AstrolojiMotorServis.TAROT_DESTESI);
        model.addAttribute("ruyaSozlugu", AstrolojiMotorServis.RUYA_SOZLUGU);
        model.addAttribute("seciliKonu", "Genel");
        gecmisOkumalariEkle(model, principal);
    }

    @GetMapping("/fal")
    public String falSayfasi(@RequestParam(defaultValue = "tarot") String sekme, Model model, Principal principal) {
        falOrtakModel(model, principal, sekme);
        return "fal";
    }

    @PostMapping("/fal/tarot")
    public String tarotAcilimi(
            @RequestParam(defaultValue = "1") int kart1No,
            @RequestParam(defaultValue = "8") int kart2No,
            @RequestParam(defaultValue = "19") int kart3No,
            @RequestParam(defaultValue = "Genel") String konu,
            @RequestParam(defaultValue = "") String niyet,
            Model model,
            Principal principal
    ) {
        falOrtakModel(model, principal, "tarot");
        int[] nolar = {
                Math.floorMod(kart1No, AstrolojiMotorServis.TAROT_DESTESI.size()),
                Math.floorMod(kart2No, AstrolojiMotorServis.TAROT_DESTESI.size()),
                Math.floorMod(kart3No, AstrolojiMotorServis.TAROT_DESTESI.size())
        };
        String[] pozisyonlar = {"1. Kart — Geçmiş", "2. Kart — Şimdi", "3. Kart — Gelecek"};
        int seed = Math.abs(Objects.hash(nolar[0], nolar[1], nolar[2], konu, niyet, LocalDate.now().toString()));

        List<Map<String, Object>> secilenKartlar = new ArrayList<>();
        List<String> adlar = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AstrolojiMotorServis.TarotKarti k = AstrolojiMotorServis.TAROT_DESTESI.get(nolar[i]);
            boolean ters = ((seed + i * 7) % 5) == 0;
            String anlam = ters ? k.tersAnlam() : k.duzAnlam();
            String konuYorum = "Aşk".equals(konu) ? k.askYorumu() : ("Kariyer".equals(konu) ? k.kariyerYorumu() : anlam);
            adlar.add(k.ad());
            secilenKartlar.add(Map.of(
                    "pozisyon", pozisyonlar[i],
                    "roma", k.roma(),
                    "ad", k.ad(),
                    "element", k.element(),
                    "yon", ters ? "Ters" : "Düz",
                    "anahtar", k.anahtarKelime(),
                    "yorum", anlam,
                    "konuYorum", konuYorum
            ));
        }

        String sentez = "Seçtiğiniz '" + String.join(" -> ", adlar) + "' üçlüsü (" + konu + " odağı): " +
                secilenKartlar.get(0).get("yorum") + " Şu anki aşamada " + secilenKartlar.get(1).get("konuYorum") +
                " Gelecek kartınız ise " + secilenKartlar.get(2).get("yorum");
        if (!niyet.isBlank()) {
            sentez = "Niyetiniz ('" + niyet.strip() + "') için Tarot Sentezi — " + sentez;
        }

        model.addAttribute("tarotKartlari", secilenKartlar);
        model.addAttribute("tarotSentez", sentez);
        model.addAttribute("seciliKonu", konu);
        okumaKaydet(principal, "Tarot", "Tarot (" + konu + "): " + String.join(", ", adlar), sentez);
        gecmisOkumalariEkle(model, principal);
        return "fal";
    }

    @PostMapping("/fal/kahve")
    public String kahveFali(
            @RequestParam(value = "fincanFoto", required = false) MultipartFile fincanFoto,
            @RequestParam(defaultValue = "Genel") String konu,
            @RequestParam(defaultValue = "") String notlar,
            Model model,
            Principal principal
    ) {
        falOrtakModel(model, principal, "kahve");
        Map<String, Object> analiz = astrolojiMotor.kahveFincaniAnalizEt(fincanFoto, konu, notlar);
        model.addAttribute("kahveAnaliz", analiz);
        model.addAttribute("kahveKonu", konu);
        okumaKaydet(principal, "Kahve Falı", "Kahve Falı (" + konu + ")", String.valueOf(analiz.get("genelOzet")));
        gecmisOkumalariEkle(model, principal);
        return "fal";
    }

    @PostMapping("/fal/ruya")
    public String ruyaTabiri(
            @RequestParam String ruyaMetni,
            @RequestParam(defaultValue = "Meraklı") String duygu,
            Model model,
            Principal principal
    ) {
        falOrtakModel(model, principal, "ruya");
        Map<String, Object> analiz = astrolojiMotor.ruyaTabirEt(ruyaMetni, duygu);
        model.addAttribute("ruyaAnaliz", analiz);
        model.addAttribute("ruyaMetniGirdi", ruyaMetni);
        model.addAttribute("seciliDuygu", duygu);
        String kisaBaslik = ruyaMetni.strip().length() > 36 ? ruyaMetni.strip().substring(0, 36) + "..." : ruyaMetni.strip();
        okumaKaydet(principal, "Rüya Tabiri", "Rüya: " + kisaBaslik, String.valueOf(analiz.get("genelYorum")));
        gecmisOkumalariEkle(model, principal);
        return "fal";
    }

    private void okumaKaydet(Principal principal, String tur, String baslik, String ozet) {
        if (principal == null) return;
        kullaniciRepository.findByEmail(principal.getName()).ifPresent(k -> {
            KayitliOkuma okuma = new KayitliOkuma();
            okuma.setKullanici(k);
            okuma.setType(tur);
            okuma.setTitle(baslik);
            okuma.setSummary(ozet);
            okumaRepository.save(okuma);
        });
    }

    private void gecmisOkumalariEkle(Model model, Principal principal) {
        if (principal != null) {
            kullaniciRepository.findByEmail(principal.getName()).ifPresent(k ->
                    model.addAttribute("gecmisOkumalar", okumaRepository.findByKullaniciOrderByCreatedAtDesc(k))
            );
        }
    }

    // -------------------------------------------------------------------------
    // 4. SİNASTRİ İLİŞKİ UYUMU (/uyum)
    // -------------------------------------------------------------------------
    @GetMapping("/uyum")
    public String uyumSayfasi(Model model, Principal principal) {
        ortakModelDoldur(model, principal, "uyum");
        uyumHesaplaVeEkle("Ahmet", "2001-08-15", "Zeynep", "2002-11-08", model);
        return "uyum";
    }

    @PostMapping("/uyum")
    public String uyumHesapla(
            @RequestParam String kisi1Ad,
            @RequestParam String kisi1Tarih,
            @RequestParam String kisi2Ad,
            @RequestParam String kisi2Tarih,
            Model model,
            Principal principal
    ) {
        ortakModelDoldur(model, principal, "uyum");
        uyumHesaplaVeEkle(kisi1Ad, kisi1Tarih, kisi2Ad, kisi2Tarih, model);
        okumaKaydet(principal, "Burç Uyumu", kisi1Ad + " & " + kisi2Ad + " Sinastri Uyumu", String.valueOf(model.getAttribute("uyumOzeti")));
        return "uyum";
    }

    private void uyumHesaplaVeEkle(String ad1, String t1, String ad2, String t2, Model model) {
        String b1 = tarihIleGunesBurcuBul(t1);
        String b2 = tarihIleGunesBurcuBul(t2);
        String e1 = BURC_PROFILLERI.get(b1).element();
        String e2 = BURC_PROFILLERI.get(b2).element();

        int taban = 72;
        if (e1.equals(e2)) taban = 86;
        else if ((e1.equals("Ateş") && e2.equals("Hava")) || (e1.equals("Hava") && e2.equals("Ateş"))) taban = 84;
        else if ((e1.equals("Toprak") && e2.equals("Su")) || (e1.equals("Su") && e2.equals("Toprak"))) taban = 85;
        else taban = 74;

        int h = Math.abs(Objects.hash(ad1, t1, ad2, t2));
        int genel = Math.min(97, taban + (h % 11));
        int sevgi = Math.min(98, taban + ((h / 3) % 12));
        int iletisim = Math.min(96, taban - 2 + ((h / 5) % 14));
        int guven = Math.min(97, taban - 1 + ((h / 7) % 12));

        List<Map<String, Object>> caprazAcilar = List.of(
                Map.of("kisi1", ad1 + " Güneş (" + b1 + ")", "kisi2", ad2 + " Güneş (" + b2 + ")", "aci", e1.equals(e2) ? "Üçgen (120°)" : "Altmışlık (60°)", "yorum", e1 + " ve " + e2 + " elementleri arasında karşılıklı denge ve tamamlayıcı enerji bulunuyor."),
                Map.of("kisi1", ad1 + " Venüs", "kisi2", ad2 + " Mars", "aci", "Altmışlık (60°)", "yorum", "Sosyal uyum, ortak paylaşımlar ve duygusal çekim güçlü."),
                Map.of("kisi1", ad1 + " Merkür", "kisi2", ad2 + " Merkür", "aci", "Kavuşum (0°)", "yorum", "Fikir alışverişi ve günlük iletişim dili birbiriyle örtüşüyor.")
        );

        model.addAttribute("kisi1Ad", ad1);
        model.addAttribute("kisi1Tarih", t1);
        model.addAttribute("kisi1Burc", b1 + " (" + e1 + ")");
        model.addAttribute("kisi2Ad", ad2);
        model.addAttribute("kisi2Tarih", t2);
        model.addAttribute("kisi2Burc", b2 + " (" + e2 + ")");
        model.addAttribute("genelUyum", genel);
        model.addAttribute("sevgiUyumu", sevgi);
        model.addAttribute("iletisimUyumu", iletisim);
        model.addAttribute("guvenUyumu", guven);
        model.addAttribute("caprazAcilar", caprazAcilar);
        model.addAttribute("uyumOzeti",
                ad1 + " (" + b1 + " - " + e1 + ") ve " + ad2 + " (" + b2 + " - " + e2 + ") arasındaki sinastri karşılaştırmasında genel uyum %" + genel +
                " olarak hesaplanmıştır. İlişkide karşılıklı anlayış ve iletişim ön plandadır.");
    }

    // -------------------------------------------------------------------------
    // 5. KİŞİSEL GÜNLÜK & NOTLARIM (/gunluk — Giriş Zorunlu)
    // -------------------------------------------------------------------------
    @GetMapping("/gunluk")
    public String gunlukSayfasi(Model model, Principal principal) {
        ortakModelDoldur(model, principal, "gunluk");
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("notlar", gunlukRepository.findByKullaniciOrderByCreatedAtDesc(k));
        model.addAttribute("okumalar", okumaRepository.findByKullaniciOrderByCreatedAtDesc(k));
        model.addAttribute("bugun", LocalDate.now().toString());
        return "gunluk";
    }

    @PostMapping("/gunluk/ekle")
    public String gunlukEkle(
            @RequestParam String baslik,
            @RequestParam String icerik,
            @RequestParam(defaultValue = "Dengeli") String duyguDurumu,
            Principal principal
    ) {
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        GunlukKaydi g = new GunlukKaydi();
        g.setKullanici(k);
        g.setTitle(baslik.strip());
        g.setContent(icerik.strip());
        g.setMood(duyguDurumu);
        gunlukRepository.save(g);
        return "redirect:/gunluk";
    }

    @PostMapping("/gunluk/sil")
    public String gunlukSil(@RequestParam Long notId, Principal principal) {
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        gunlukRepository.findById(notId).ifPresent(g -> {
            if (g.getKullanici().getId().equals(k.getId())) {
                gunlukRepository.delete(g);
            }
        });
        return "redirect:/gunluk";
    }

    @PostMapping("/okuma/sil")
    public String okumaSil(@RequestParam Long okumaId, Principal principal) {
        Kullanici k = kullaniciRepository.findByEmail(principal.getName()).orElseThrow();
        okumaRepository.findById(okumaId).ifPresent(o -> {
            if (o.getKullanici().getId().equals(k.getId())) {
                okumaRepository.delete(o);
            }
        });
        return "redirect:/gunluk";
    }
}
