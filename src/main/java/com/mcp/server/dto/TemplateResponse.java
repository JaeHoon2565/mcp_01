package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
/*
// ✅ TemplateResponse
// 템플릿 조회 시 반환되는 응답 객체
// context는 JSON 문자열을 파싱한 Map 형태로 응답됨
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateResponse {
    private Long id;
    private String project;
    private String name;
    private String description;
    private Map<String, Object> context;
    private String createdAt;
}

