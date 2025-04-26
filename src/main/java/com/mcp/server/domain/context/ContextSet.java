package com.mcp.server.domain.context;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "context_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 세트 이름 (간단 설명)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String persona; // 전문가의 정체성, 경력 등

    @Column(nullable = false, length = 100)
    private String role; // 직무/역할

    @Column(nullable = false, columnDefinition = "TEXT")
    private String situation; // 어떤 상황인지

    @Column(nullable = false, columnDefinition = "TEXT")
    private String goal; // 목적/달성 목표

    @Column(nullable = false, length = 100)
    private String tone; // 톤

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 추가: context JSON 자동 구성용 헬퍼 메서드
    public String toContextJson() {
        return String.format("{\n  \"persona\": \"%s\",\n  \"role\": \"%s\",\n  \"situation\": \"%s\",\n  \"goal\": \"%s\",\n  \"tone\": \"%s\"\n}",
                persona, role, situation, goal, tone);
    }
}
