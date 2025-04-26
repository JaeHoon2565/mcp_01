package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ✅ UsageStatResponse
 * 모델별 사용량 통계 응답용 DTO
 */
@Getter
@AllArgsConstructor
public class UsageStatResponse {
    private String model;
    private int calls;
    private int totalTokens;
    private long avgElapsedMs;
}