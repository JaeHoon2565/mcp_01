package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ApiUsageStats {
    private LocalDate date;
    private String model;
    private long count;
    private long totalTokens;
    private double averageElapsed;
}
