package com.mcp.server.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/*
// ✅ 14. Template DTO 및 Mapper 추가
// contextJson ↔ Map<String, Object> 변환을 위한 구조
*/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateRequest {
    private String project;
    private String name;
    private String description;
    private Map<String, Object> context;
}

