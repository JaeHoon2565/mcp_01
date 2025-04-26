package com.mcp.server.service;

import com.mcp.server.client.AiModelClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiModelRouter {

    private final List<AiModelClient> clients;

    public String route(String modelName, String prompt) {
        return clients.stream()
                .filter(c -> c.supports(modelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 모델: " + modelName))
                .call(modelName, prompt);
    }
}