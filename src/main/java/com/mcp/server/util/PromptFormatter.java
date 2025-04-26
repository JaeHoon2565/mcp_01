package com.mcp.server.util;

import java.util.Map;

/**
 * ✅ PromptFormatter
 * - context + query를 기반으로 AI 모델에게 전달할 프롬프트 문자열을 구성하는 유틸리티 클래스
 * - 지원 메서드:
 *   1. Map<String, Object> 기반 컨텍스트 조합
 *   2. contextJson(String) 형태의 세트 기반 조합 (ContextSet 등)
 */
public class PromptFormatter {

    /**
     * ✅ [기존] Map 기반 프롬프트 구성
     * - 주로 사용자 직접 입력 기반 context를 사용
     *
     * @param project 프로젝트 이름
     * @param context 사용자 입력 context (currentStep, schemas 등)
     * @param query   유저 질문
     * @return 프롬프트 문자열
     */
    public static String formatPrompt(String project, Map<String, Object> context, String query) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Project] ").append(project).append("\n");

        if (context != null && !context.isEmpty()) {
            Object currentStep = context.get("currentStep");
            if (currentStep != null) {
                sb.append("[Step] ").append(currentStep).append("\n");
            }

            Object schemas = context.get("schemas");
            if (schemas instanceof Map<?, ?> schemaMap) {
                sb.append("[Schemas]\n");
                for (Map.Entry<?, ?> entry : schemaMap.entrySet()) {
                    sb.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
            }
        } else {
            sb.append("[Context 없음] 아래 질문을 기반으로 적절히 이해하고 응답해주세요.\n");
        }

        sb.append("\n[User Query]\n").append(query);
        return sb.toString();
    }

    /**
     * ✅ [추가] contextJson 기반 프롬프트 구성
     * - ContextSetService에서 불러온 contextJson(String)을 바로 활용
     *
     * @param project     프로젝트 이름
     * @param contextJson JSON 문자열 (persona, role, situation, goal, tone 등 포함)
     * @param query       유저 질문
     * @return 프롬프트 문자열
     */
    public static String formatPrompt(String project, String contextJson, String query) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Project] ").append(project).append("\n\n");
        sb.append("[Context]\n").append(contextJson).append("\n\n");
        sb.append("[User Query]\n").append(query);
        return sb.toString();
    }
}
