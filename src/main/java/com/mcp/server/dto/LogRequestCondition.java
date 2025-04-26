package com.mcp.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ✅ LogRequestCondition
 * - 추론 로그 조회 시 사용할 필터 조건 DTO
 * - GET /logs 요청의 쿼리 파라미터를 바인딩하는 용도
 */
@Getter
@Setter
public class LogRequestCondition {

    /**
     * 필터링할 프로젝트 이름 (선택)
     * 예: A, B, C
     */
    private String project;

    /**
     * 시작 날짜 (yyyy-MM-dd 형식)
     * 해당 날짜 이후의 로그만 포함됨
     */
    private LocalDate from;

    /**
     * 종료 날짜 (yyyy-MM-dd 형식)
     * 해당 날짜 이전의 로그만 포함됨
     */
    private LocalDate to;

    /**
     * 정렬 순서 (asc | desc)
     * 기본값: desc (최신 순)
     */
    private String sort = "desc";
}