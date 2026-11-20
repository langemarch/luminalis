package com.luminalis.controller;

import com.luminalis.service.YapayZekaServis;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/astrology")
public class AstrologyController {

    private static final List<String> SIGNS = List.of(
            "Koç", "Boğa", "İkizler", "Yengeç", "Aslan", "Başak",
            "Terazi", "Akrep", "Yay", "Oğlak", "Kova", "Balık"
    );

    private final YapayZekaServis yapayZekaServis;

    public AstrologyController(YapayZekaServis yapayZekaServis) {
        this.yapayZekaServis = yapayZekaServis;
    }

    @GetMapping("/daily-transit")
    public Map<String, Object> dailyTransit() {
        return Map.of(
                "date", LocalDate.now().toString(),
                "moonPhase", "Büyüyen Ay",
                "summary", "Ay ve Venüs arasındaki uyumlu açı iletişim ve içsel dengeyi destekliyor.",
                "dominantElement", "Ateş & Hava"
        );
    }

    @GetMapping("/planetary-positions")
    public List<Map<String, Object>> planetaryPositions() {
        return List.of(
                Map.of("id", "sun", "name", "Güneş", "signName", "Akrep", "degree", 25, "retrograde", false),
                Map.of("id", "moon", "name", "Ay", "signName", "İkizler", "degree", 14, "retrograde", false),
                Map.of("id", "mercury", "name", "Merkür", "signName", "Yay", "degree", 9, "retrograde", false),
                Map.of("id", "venus", "name", "Venüs", "signName", "Oğlak", "degree", 6, "retrograde", false),
                Map.of("id", "mars", "name", "Mars", "signName", "Aslan", "degree", 4, "retrograde", false),
                Map.of("id", "jupiter", "name", "Jüpiter", "signName", "İkizler", "degree", 18, "retrograde", true),
                Map.of("id", "saturn", "name", "Satürn", "signName", "Balık", "degree", 12, "retrograde", false)
        );
    }

    @GetMapping("/calendar")
    public List<Map<String, Object>> calendar() {
        LocalDate d = LocalDate.now();
        return List.of(
                Map.of("date", d.plusDays(2).toString(), "title", "Merkür - Jüpiter Karşıtlığı", "impact", "Orta"),
                Map.of("date", d.plusDays(6).toString(), "title", "Dolunay Evresi", "impact", "Yüksek"),
                Map.of("date", d.plusDays(12).toString(), "title", "Venüs Üçgen Açısı", "impact", "Olumlu")
        );
    }

    @GetMapping("/horoscope")
    public Map<String, Object> horoscope(
            @RequestParam(defaultValue = "leo") String sign,
            @RequestParam(defaultValue = "daily") String period
    ) {
        String label = switch (period) {
            case "weekly" -> "HAFTALIK REHBERLİK";
            case "monthly" -> "AYLIK REHBERLİK";
            default -> "GÜNLÜK REHBERLİK";
        };
        int seed = Math.abs(Objects.hash(sign, period, LocalDate.now().toString()));
        return Map.of(
                "sign", sign,
                "period", period,
                "rangeLabel", label,
                "general", "Bugün sezgileriniz ve mantığınız dengeli ilerliyor. Yarım kalan işlerinizi toparlamak için uygun bir gökyüzü var.",
                "focus", "İletişim, planlama ve net kararlar",
                "advice", "Acele kararlar yerine adımlarınızı sırayla planlayın.",
                "mood", "Dengeli & Sezgisel",
                "luckyNumber", (seed % 9) + 1,
                "luckyColor", "Altın Sarısı",
                "categories", List.of(
                        Map.of("label", "Aşk & İlişkiler", "score", 4, "text", "Açık iletişim uyumu artırıyor."),
                        Map.of("label", "Kariyer & Odak", "score", 5, "text", "Üretkenlik ve dikkat seviyeniz yüksek."),
                        Map.of("label", "Enerji & Sağlık", "score", 4, "text", "Kısa yürüyüşler zihni tazeler.")
                )
        );
    }

    @PostMapping("/natal-chart")
    public Map<String, Object> natalChart(@RequestBody Map<String, Object> req) {
        String date = String.valueOf(req.getOrDefault("date", "2001-08-15"));
        String time = String.valueOf(req.getOrDefault("time", "14:30"));
        String place = String.valueOf(req.getOrDefault("place", "İstanbul"));

        int seed = Math.abs(Objects.hash(date, time, place));
        String[] planetIds = {"sun", "moon", "mercury", "venus", "mars", "jupiter", "saturn", "uranus", "neptune", "pluto"};
        String[] planetNames = {"Güneş", "Ay", "Merkür", "Venüs", "Mars", "Jüpiter", "Satürn", "Uranüs", "Neptün", "Plüton"};

        List<Map<String, Object>> planets = new ArrayList<>();
        for (int i = 0; i < planetIds.length; i++) {
            double lon = ((seed + i * 37) % 360) + ((i * 3) % 10) * 0.1;
            int signIdx = (int) (lon / 30) % 12;
            int deg = (int) (lon % 30);
            int house = ((i + (seed % 12)) % 12) + 1;
            String signName = SIGNS.get(signIdx);
            planets.add(Map.of(
                    "id", planetIds[i],
                    "name", planetNames[i],
                    "longitude", lon,
                    "signName", signName,
                    "degree", deg,
                    "house", house,
                    "formatted", signName + " " + deg + "°"
            ));
        }

        List<Map<String, Object>> houses = new ArrayList<>();
        for (int h = 1; h <= 12; h++) {
            double cusp = ((seed % 30) + (h - 1) * 30.0) % 360.0;
            String signName = SIGNS.get((int) (cusp / 30) % 12);
            houses.add(Map.of("house", h, "longitude", cusp, "signName", signName, "degree", (int) (cusp % 30)));
        }

        List<Map<String, Object>> aspects = List.of(
                Map.of("planet1", "Güneş", "planet2", "Ay", "type", "Üçgen (120°)", "orb", 1.8, "harmony", "Uyumlu"),
                Map.of("planet1", "Venüs", "planet2", "Mars", "type", "Altmışlık (60°)", "orb", 2.1, "harmony", "Destekleyici"),
                Map.of("planet1", "Merkür", "planet2", "Satürn", "type", "Kavuşum (0°)", "orb", 0.9, "harmony", "Odaklayıcı")
        );

        String ascSign = SIGNS.get(seed % 12);
        String mcSign = SIGNS.get((seed + 9) % 12);
        return Map.of(
                "date", date,
                "time", time,
                "place", place,
                "ascendant", Map.of("signName", ascSign, "degree", seed % 30),
                "midheaven", Map.of("signName", mcSign, "degree", (seed + 11) % 30),
                "planets", planets,
                "houses", houses,
                "aspects", aspects
        );
    }

    @PostMapping("/synastry")
    public Map<String, Object> synastry(@RequestBody Map<String, Object> req) {
        String myName = String.valueOf(req.getOrDefault("my_name", "Siz"));
        String partnerName = String.valueOf(req.getOrDefault("partner_name", "Partner"));
        String myDate = String.valueOf(req.getOrDefault("my_date", "2001-08-15"));
        String partnerDate = String.valueOf(req.getOrDefault("partner_date", "2002-11-08"));

        int h = Math.abs(Objects.hash(myName, partnerName, myDate, partnerDate));
        int overall = 75 + (h % 20);
        int love = 76 + ((h / 3) % 21);
        int passion = 72 + ((h / 5) % 24);
        int comm = 74 + ((h / 7) % 22);

        List<Map<String, Object>> crossAspects = List.of(
                Map.of("p1", myName + " Venüs", "p2", partnerName + " Ay", "aspect", "Üçgen (120°)", "tightness", 92),
                Map.of("p1", myName + " Güneş", "p2", partnerName + " Merkür", "aspect", "Altmışlık (60°)", "tightness", 86),
                Map.of("p1", myName + " Mars", "p2", partnerName + " Venüs", "aspect", "Kavuşum (0°)", "tightness", 89)
        );

        Map<String, Object> aiRes = yapayZekaServis.callGenerate(Map.of(
                "domain", "compatibility",
                "prompt", myName + " (" + myDate + ") ile " + partnerName + " (" + partnerDate + ") sinastri uyumu."
        ));

        return Map.of(
                "synastry", Map.of(
                        "scores", Map.of("overall", overall, "love", love, "passion", passion, "communication", comm),
                        "crossAspects", crossAspects
                ),
                "aiText", aiRes.getOrDefault("text", myName + " ve " + partnerName + " arasında güçlü bir iletişim ve duygusal uyum görülüyor.")
        );
    }
}
