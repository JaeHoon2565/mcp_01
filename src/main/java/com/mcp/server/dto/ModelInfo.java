package com.mcp.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelInfo {
    private String name;       // 내부적으로 사용하는 키 (예: "groq", "gpt")
    private String provider;    // 제공자 이름 (예: "Groq", "OpenAI")
    private String description; // 설명 (예: "초고속 AI 모델 (Groq)")
}
