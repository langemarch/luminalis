package com.luminalis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_content_reports")
public class AiIcerikRaporu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterEmail;

    @Column(nullable = false)
    private String feature;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    private LocalDateTime createdAt = LocalDateTime.now();

    public AiIcerikRaporu() {}

    public AiIcerikRaporu(String reporterEmail, String feature, String reason) {
        this.reporterEmail = reporterEmail;
        this.feature = feature;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReporterEmail() { return reporterEmail; }
    public void setReporterEmail(String reporterEmail) { this.reporterEmail = reporterEmail; }
    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
