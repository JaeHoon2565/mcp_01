package com.mcp.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.server.config.GroqProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ✅ AiModelClient의 실제 구현체 (Groq API 연동)
 *
 * - OpenAI 호환 방식으로 Groq AI 모델에게 프롬프트를 전달하고, 응답을 받아온다.
 * - 모델 종류는 Groq API 키를 통해 자동으로 조회하여 목록화한다.
 *   예: mixtral-8x7b-32768, llama3-8b-8192 등
 *
 * 💡 MCP 서버에서 AI 추론을 수행할 때 사용하는 실제 HTTP 통신 모듈이다.
 *    prompt를 받아서 POST 요청을 보내고, 결과 텍스트만 파싱해서 반환한다.
 *
 * ✅ 연동 대상 API:
 * POST https://api.groq.com/openai/v1/chat/completions
 * GET  https://api.groq.com/openai/v1/models
 */
@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class GroqAiModelClientImpl implements AiModelClient {

    private final RestTemplate restTemplate;
    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL_LIST_URL = "https://api.groq.com/openai/v1/models";

    private Set<String> supportedModels = new HashSet<>();

    /**
     * ✅ 서버 시작 시 지원 가능한 모델 목록 자동 조회
     * - Groq API를 통해 키 기반 사용 가능 모델 목록을 불러옴
     */
    @PostConstruct
    public void loadSupportedModels() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(groqProperties.getApiKey());

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    MODEL_LIST_URL,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            supportedModels = StreamSupport.stream(
                            Spliterators.spliteratorUnknownSize(response.getBody().get("data").elements(), Spliterator.ORDERED),
                            false
                    ).map(node -> node.get("id").asText())
                    .collect(Collectors.toSet());

            log.info("✅ [Groq] 사용 가능 모델 목록 불러오기 완료: {}", supportedModels);
        } catch (Exception e) {
            log.error("❌ [Groq] 모델 목록 불러오기 실패", e);
        }
    }

    /**
     * ✅ 프롬프트를 Groq 모델에 전달하여 결과 응답을 받는다
     *
     * @param model 사용할 모델명 (예: llama3-8b-8192)
     * @param prompt 사용자가 만든 최종 메시지
     * @return AI 응답 텍스트
     */
    @Override
    public String call(String model, String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqProperties.getApiKey());

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            log.error("❌ [Groq] 모델 호출 실패", e);
            return "[ERROR] Groq 호출 실패: " + e.getMessage();
        }
    }

    /**
     * ✅ 현재 이 구현체가 특정 모델명을 지원하는지 확인
     *
     * @param modelName 모델 ID (예: llama3-8b-8192)
     * @return 지원 여부
     */
    @Override
    public boolean supports(String modelName) {
        return supportedModels.contains(modelName);
    }

    /**
     * ✅ 외부 노출용: 현재 Groq 키로 사용할 수 있는 모델 목록 반환
     */
    public Set<String> getSupportedModels() {
        return supportedModels;
    }
}
