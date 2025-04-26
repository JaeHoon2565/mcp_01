package com.mcp.server.service;

import com.mcp.server.domain.embedding.LogEmbeddingMetadata;
import com.mcp.server.domain.embedding.LogEmbeddingMetadataRepository;
import com.mcp.server.domain.log.Log;
import com.mcp.server.domain.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * ✅ LogService
 * - 추론 요청이 발생한 후, 해당 요청 및 응답 데이터를 DB에 저장하는 서비스 계층
 * - 로그 저장 + 벡터화를 위한 메타데이터(LogEmbeddingMetadata) 분리 저장
 */
@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final LogEmbeddingMetadataRepository logEmbeddingMetadataRepository;

    /**
     * ✅ 로그 저장 + 메타데이터(PROMPT, QUERY, RESULT) 생성
     *
     * @param project  프로젝트 이름
     * @param provider 모델 제공자
     * @param model    모델 ID
     * @param prompt   사용된 프롬프트 문자열
     * @param query    사용자 질문
     * @param result   AI 응답 결과
     * @param elapsed  처리 시간(ms)
     */
    public void saveLog(String project, String provider, String model, String prompt, String query, String result, String elapsed) {
        // 1. 로그 저장
        Log log = new Log(project, provider, model, prompt, query, result, elapsed);
        logRepository.save(log);

        Long logId = log.getId();

        // 2. LogEmbeddingMetadata 저장 (세분화된 contentType으로 각각 저장)
        logEmbeddingMetadataRepository.save(buildMetadata(project, logId, "PROMPT", prompt));
        logEmbeddingMetadataRepository.save(buildMetadata(project, logId, "QUERY", query));
        logEmbeddingMetadataRepository.save(buildMetadata(project, logId, "RESULT", result));
    }

    /**
     * ✅ LogEmbeddingMetadata 생성 유틸
     *
     * @param project      프로젝트명
     * @param logId        로그 ID
     * @param contentType  PROMPT / QUERY / RESULT
     * @param content      실제 텍스트
     * @return             LogEmbeddingMetadata 객체
     */
    private LogEmbeddingMetadata buildMetadata(String project, Long logId, String contentType, String content) {
        return LogEmbeddingMetadata.builder()
                .project(project)
                .logId(logId)
                .contentType(contentType)
                .content(content)
                .embedded(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * ✅ 모델명 기반 제공자 추정 로직 (예비 용도)
     */
    private String resolveProvider(String modelId) {
        String id = modelId.toLowerCase();
        if (id.contains("groq")) return "Groq";
        if (id.contains("together") || id.startsWith("meta-llama") || id.startsWith("qwen/")) return "Together";
        if (id.contains("openai") || id.startsWith("gpt-")) return "OpenAI";
        if (id.contains("claude") || id.contains("anthropic")) return "Anthropic";
        if (id.contains("gemma") || id.contains("google")) return "Google";
        return "Unknown";
    }
}
