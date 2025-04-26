package com.mcp.server.domain.usage;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ✅ ApiUsageLog
 * 일자별, 모델별, IP별 API 사용량(토큰 수, 소요 시간 등)을 기록하기 위한 엔티티.
 */
@Getter
@NoArgsConstructor
@Entity
public class ApiUsageLog {

    @Id
    @GeneratedValue
    private Long id;

    private String model;
    private int tokensUsed;
    private long elapsedTimeMs;

    private LocalDate date;
    private String ipAddress;

    @CreatedDate
    private LocalDateTime createdAt;

    public ApiUsageLog(String model, int tokensUsed, long elapsedTimeMs, String ipAddress) {
        this.model = model;
        this.tokensUsed = tokensUsed;
        this.elapsedTimeMs = elapsedTimeMs;
        this.ipAddress = ipAddress;
        this.date = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }
}
