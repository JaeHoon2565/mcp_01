package com.mcp.server.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ Groq API 설정 클래스
 * - application.yml에서 groq 관련 값 읽어옴
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "groq")
public class GroqProperties {

    private String apiKey;
    private String model;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setModel(String model) {
        this.model = model;
    }
}