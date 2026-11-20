package com.luminalis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class YapayZekaServis {

    private final RestClient restClient;

    public YapayZekaServis(@Value("${luminalis.ai.base-url}") String aiBaseUrl) {
        this.restClient = RestClient.create(aiBaseUrl);
    }

    public Map<String, Object> callGenerate(Map<String, Object> body) {
        try {
            return restClient.post()
                    .uri("/v1/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            return Map.of(
                    "text", "Gökyüzü enerjiniz bugün odaklanma ve netlik vurgusu taşıyor. (Yerel Spring Boot yedek yorum motoru)",
                    "source", "spring-fallback"
            );
        }
    }

    public Map<String, Object> callCoffeeVision(Map<String, Object> body) {
        try {
            return restClient.post()
                    .uri("/v1/vision/coffee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            return Map.of(
                    "symbols", java.util.List.of("Yol", "Kuş", "Anahtar"),
                    "text", "Fincanınızda açık bir yol, haber getiren kuş ve çözüm simgesi anahtar öne çıkıyor.",
                    "source", "spring-fallback"
            );
        }
    }
}
