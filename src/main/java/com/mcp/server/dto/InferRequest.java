package com.mcp.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * ✅ InferRequest DTO
 * - 사용자가 AI 추론을 요청할 때 전달하는 요청 데이터 구조
 *
 * [필드 설명]
 * - project: 요청이 속한 프로젝트 이름
 * - provider: 모델 제공자 (예: Groq, Together, OpenAI 등) ← 명시적 구분 가능
 * - model: 사용할 AI 모델 이름 (예: gpt-4, llama-3-8b 등)
 * - context: AI에게 제공할 배경 정보 (JSON 형식)
 * - query: 사용자가 AI에게 묻는 질문
 */
@Data
public class InferRequest {

    @NotBlank(message = "project 필드는 비워둘 수 없습니다.")
    private String project;

    @NotBlank(message = "provider 필드는 비워둘 수 없습니다.")
    private String provider;

    @NotBlank(message = "model 필드는 비워둘 수 없습니다.")
    private String model;

    @NotNull(message = "contextSetId는 null일 수 없습니다.")
    private Long contextSetId; // ✅ ContextSet 기반 추론을 위해 필드 변경

    @NotBlank(message = "query 필드는 비워둘 수 없습니다.")
    private String query;
}