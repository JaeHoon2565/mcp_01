package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
// ✅ 2. InferResponse DTO
// AI 응답을 클라이언트에 전달하기 위한 DTO
// - result: AI가 응답한 본문
// - prompt: 내부적으로 생성된 전체 프롬프트 (디버깅/로깅용)
// - model: 어떤 모델로 호출했는지
// - elapsed: 걸린 시간 (ms)
// - tokensUsed: 사용된 토큰 수
*/
@Data
@Builder
@AllArgsConstructor
public class InferResponse {
    private String result;
    private String prompt;
    private String model;
    private String elapsed;
    private int tokensUsed;
}
