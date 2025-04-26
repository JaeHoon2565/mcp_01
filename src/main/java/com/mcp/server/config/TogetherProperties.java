package com.mcp.server.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ Together AI API 설정 클래스
 * - application.yml에서 together 관련 값 읽어옴
 */
@Getter
@Configuration
@ConfigurationProperties(prefix = "together")
public class TogetherProperties {

    private String apiKey;
    private String model;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setModel(String model) {
        this.model = model;
    }
}