package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ApiUsageSummary {
    private LocalDate date;
    private String model;
    private Long count;         // <- Long으로 변경
    private Long totalTokens;   // <- Long으로 변경
    private Double averageElapsed; // <- Double로 변경 (기존과 동일)
}
