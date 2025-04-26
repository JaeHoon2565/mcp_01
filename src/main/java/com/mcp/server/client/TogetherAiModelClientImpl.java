package com.mcp.server.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.server.config.TogetherProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * ✅ Together AI 모델 클라이언트 구현체
 * - 모델 목록 불러오기 및 메시지 호출 기능 구현
 * - API 키는 application.yml -> TogetherProperties 통해 주입
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TogetherAiModelClientImpl implements AiModelClient {

    private final RestTemplate restTemplate;
    private final TogetherProperties togetherProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String API_URL = "https://api.together.xyz/v1/chat/completions";
    private static final String MODEL_LIST_URL = "https://api.together.xyz/v1/models";

    private Set<String> supportedModels = new HashSet<>();

    /**
     * ✅ 애플리케이션 시작 시 사용 가능한 모델 목록 불러오기
     * - 응답 구조가 배열인 경우에도 대응하도록 수정
     */
    @PostConstruct
    public void loadSupportedModels() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(togetherProperties.getApiKey());

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    MODEL_LIST_URL,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class
            );

            JsonNode root = response.getBody();
            log.debug("📦 Together 모델 목록 응답: {}", root);

            if (root == null) {
                throw new IllegalStateException("Together 모델 응답이 null입니다.");
            }

            // 응답이 배열 형태일 경우 처리
            if (root.isArray()) {
                supportedModels = StreamSupport.stream(
                                Spliterators.spliteratorUnknownSize(root.elements(), Spliterator.ORDERED),
                                false
                        ).map(node -> node.get("id").asText())
                        .collect(Collectors.toSet());
            } else {
                throw new IllegalStateException("Together 응답이 배열이 아님: " + root);
            }

            log.info("✅ [Together] 사용 가능 모델 목록 불러오기 완료: {}", supportedModels);
        } catch (Exception e) {
            log.error("❌ [Together] 모델 목록 불러오기 실패", e);
        }
    }

    /**
     * ✅ 모델 호출
     * @param model 모델명
     * @param prompt 사용자 프롬프트
     * @return 응답 메시지 내용
     */
    @Override
    public String call(String model, String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(togetherProperties.getApiKey());

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
            log.error("❌ [Together] 모델 호출 실패", e);
            return "[ERROR] Together 호출 실패: " + e.getMessage();
        }
    }

    /**
     * ✅ 지원 여부 확인
     * @param modelName 모델명
     * @return 지원 여부
     */
    @Override
    public boolean supports(String modelName) {
        return supportedModels.contains(modelName);
    }

    /**
     * ✅ 지원 모델 반환
     */
    public Set<String> getSupportedModels() {
        return supportedModels;
    }


}
