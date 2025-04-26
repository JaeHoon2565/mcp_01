package com.mcp.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.server.domain.template.Template;
import com.mcp.server.dto.TemplateRequest;
import com.mcp.server.dto.TemplateResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
// ✅ TemplateMapper
// Template 엔티티와 Request/Response DTO 간의 변환을 처리하는 유틸리티 클래스
// 내부적으로 ObjectMapper를 이용해 JSON ↔ Map 변환을 수행함
*/
public class TemplateMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // TemplateRequest → Template (Entity)
    public static Template toEntity(TemplateRequest request) {
        try {
            String json = objectMapper.writeValueAsString(request.getContext());
            return new Template(request.getProject(), request.getName(), request.getDescription(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Context JSON 변환 실패", e);
        }
    }

    // Template (Entity) → TemplateResponse
    public static TemplateResponse toResponse(Template template) {
        try {
            Map<String, Object> contextMap = objectMapper.readValue(template.getContextJson(), Map.class);
            return TemplateResponse.builder()
                    .id(template.getId())
                    .project(template.getProject())
                    .name(template.getName())
                    .description(template.getDescription())
                    .context(contextMap)
                    .createdAt(template.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Context 파싱 실패", e);
        }
    }

    // List<Template> → List<TemplateResponse>
    public static List<TemplateResponse> toResponseList(List<Template> templates) {
        return templates.stream()
                .map(TemplateMapper::toResponse)
                .collect(Collectors.toList());
    }
}
