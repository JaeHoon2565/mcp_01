package com.mcp.server.client;

import java.util.Set;

/**
 * ✅ AI 모델 호출용 인터페이스
 * - MCP에서 추론 요청 시, 다양한 AI 모델 구현체(Groq 등)를 교체 가능하게 함
 */
public interface AiModelClient {

    /**
     * AI 모델에게 프롬프트 전달하고 응답 받기
     *
     * @param model 사용할 모델명 (예: mixtral-8x7b-32768)
     * @param prompt 사용자가 만든 최종 메시지
     * @return AI의 응답 텍스트
     */
    String call(String model, String prompt);

    /**
     * ✅ 해당 모델을 지원하는지 확인
     */
    boolean supports(String modelName);

    /**
     * ✅ 현재 클라이언트가 지원하는 모델 ID 목록 반환
     */
    Set<String> getSupportedModels();
}