package com.mcp.server.util;


/**
 * ✅ TokenCounter
 * 프롬프트 문자열의 길이를 기준으로 간단한 토큰 수를 추정하는 유틸리티 클래스.
 * (현재는 1 token ≈ 4 characters 방식 사용)
 */
public class TokenCounter {

    public static int estimateTokenCount(String text) {
        // 아주 간단한 방식 (1 token ≈ 4 characters)
        return text.length() / 4;
    }

    public static int countForPrompt(String prompt) {
        return estimateTokenCount(prompt);
    }
}