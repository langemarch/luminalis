# Luminalis — Web Portalı

Premium Türkçe astroloji ve kişisel farkındalık web uygulaması. Katmanlı kurumsal mimari ile geliştirilmiştir.

## Teknoloji Yığını

- **Backend:** Spring Boot 3.3.5 (Java 21, Tomcat :8080)
- **ORM:** Spring Data JPA / Hibernate
- **Veritabanı:** H2 (gömülü, varsayılan) veya MySQL (opsiyonel, `DB_URL` ile)
- **Güvenlik:** Spring Security + JWT (jjwt 0.12.6) + BCrypt
- **Şablon:** Thymeleaf (sunucu tarafı HTML)

## Gereksinimler

- JDK 21 (ör. Eclipse Adoptium)
- Maven 3.9+

> Veritabanı olarak **H2 gömülü** kullanılır; hiçbir veritabanı kurulumu gerekmez.
> MySQL kullanmak isterseniz `DB_URL`, `DB_USER`, `DB_PASS` ortam değişkenlerini tanımlayın.

## Çalıştırma

```bash
mvn clean package -DskipTests
java -jar target/luminalis-backend-1.0.0.jar
```

Site: http://localhost:8080/

Sağlık kontrolü: http://localhost:8080/api/health

## Yapılandırma (application.properties)

| Anahtar | Varsayılan | Açıklama |
|---|---|---|
| `PORT` | 8080 | Sunucu portu |
| `DB_URL` | `jdbc:h2:file:./data/luminalis;AUTO_SERVER=TRUE` | Veritabanı bağlantısı |
| `DB_USER` | `sa` | Veritabanı kullanıcısı |
| `DB_PASS` | (boş) | Veritabanı şifresi |
| `JWT_SECRET` | (varsayılan) | JWT imza anahtarı |

## Katmanlı Mimari

```
templates/ (Thymeleaf HTML)  →  controller/ (@Controller)  →  service/ (@Transactional)
    →  repository/ (JpaRepository)  →  entity/ (@Entity)  →  H2/MySQL
```

## Ana Sayfalar

- `/` — Ana sayfa (astro modülleri kataloğu)
- `/burclar` — Burç yorumları
- `/dogum-haritasi` — Doğum haritası
- `/fal` — Tarot & kahve falı
- `/uyum` — İlişki uyumu (sinastri)
- `/gunluk` — Kişisel notlar (giriş zorunlu)
- `/login`, `/kayit` — Giriş / kayıt
- `/h2-console` — H2 veritabanı konsolu
