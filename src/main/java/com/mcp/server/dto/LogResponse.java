package com.mcp.server.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * ✅ LogResponse
 * - 추론 로그 응답용 DTO
 * - 클라이언트에 보여줄 필드만 선택적으로 담은 구조
 */
@Getter
@Builder
class LogResponse {

    /**
     * 로그 ID (기본 키)
     */
    private Long id;


    /**
     *  제공자 (OPENAI, GEMINI , Groq)
     */

    private String provider;

    /**
     * 해당 로그가 속한 프로젝트명
     */
    private String project;

    /**
     * 사용된 AI 모델명 (예: gpt-4, claude 등)
     */
    private String model;

    /**
     * 사용자가 전달한 쿼리(질문)
     */
    private String query;

    /**
     * AI가 응답한 결과 텍스트
     */
    private String result;

    /**
     * 응답까지 걸린 시간 (ms 단위 문자열)
     */
    private String elapsed;

    /**
     * 로그 생성 시각 (서버 기준)
     */
    private LocalDateTime createdAt;
}