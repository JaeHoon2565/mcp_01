package com.mcp.server.domain.log;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// ✅ 7. Log 엔티티 (추론 요청 & 응답 기록)
// 추론 요청과 응답 정보를 DB에 저장하기 위한 JPA 엔티티

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String project;
    private String provider;
    private String model;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String query;

    @Column(columnDefinition = "TEXT")
    private String result;

    private String elapsed;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Log(String project, String provider, String model, String prompt, String query, String result, String elapsed) {
        this.project = project;
        this.provider = provider;
        this.model = model;
        this.prompt = prompt;
        this.query = query;
        this.result = result;
        this.elapsed = elapsed;
    }
}