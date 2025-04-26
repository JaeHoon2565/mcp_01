package com.mcp.server.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ✅ ModelRateLimiter
 * 특정 모델에 대한 하루 호출 횟수를 제한하는 서비스.
 * 초과 시 IllegalStateException 발생시킴.
 */
@Service
public class ModelRateLimiter {

    // 모델별 호출 카운트 저장 (날짜 기준으로 초기화됨)
    private final Map<String, Integer> usageMap = new ConcurrentHashMap<>();
    private LocalDate today;

    // 모델별 하루 최대 호출 수 설정 (예: groq은 1000, gpt는 1500 등)
    private final Map<String, Integer> limitMap = Map.of(
            "gpt", 1500,
            "groq", 1000,
            "claude", 500
    );

    @PostConstruct
    public void init() {
        today = LocalDate.now();
    }

    public synchronized void checkQuota(String model) {
        LocalDate now = LocalDate.now();
        if (!now.equals(today)) {
            // 날짜 바뀌면 초기화
            usageMap.clear();
            today = now;
        }

        int used = usageMap.getOrDefault(model, 0);
        int limit = limitMap.getOrDefault(model, 1000); // 기본 1000

        if (used >= limit) {
            throw new IllegalStateException("💥 모델 '" + model + "' 호출 제한 초과 (" + used + "/" + limit + ")");
        }

        usageMap.put(model, used + 1);
    }
}
