/**
 * ✅ LogEmbeddingBackfillService
 * - 누락된 LogEmbeddingMetadata를 주기적으로 생성해주는 백필(batch) 처리 전용 서비스
 * - Log 데이터는 존재하지만 메타데이터가 생성되지 않은 항목을 찾아 자동 생성
 *

 * 대상: infer_log 테이블 (Log)
 * 목적: 사용자의 입력/출력 관련 텍스트 (prompt, query, result) 임베딩용 메타데이터 생성 *
 * 용도: 대화 이력, 추론 흐름에 대한 의미 벡터화
 * contentType: PROMPT, QUERY, RESULT

 *
 */
package com.mcp.server.service;

import com.mcp.server.domain.embedding.LogEmbeddingMetadata;
import com.mcp.server.domain.embedding.LogEmbeddingMetadataRepository;
import com.mcp.server.domain.log.Log;
import com.mcp.server.domain.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogEmbeddingBackfillService {

    private static final Logger log = LoggerFactory.getLogger(LogEmbeddingBackfillService.class);

    private final LogRepository logRepository;
    private final LogEmbeddingMetadataRepository metadataRepository;

    /**
     * ✅ 전체 로그 중 메타데이터가 누락된 항목을 찾아 보충하는 작업
     * - 매 실행 시, 로그를 순회하며 누락된 contentType 별 LogEmbeddingMetadata 생성
     */
    public void backfillMissingMetadata() {
        List<Log> allLogs = logRepository.findAll();
        int addedCount = 0;

        for (Log log : allLogs) {
            Long logId = log.getId();

            // 해당 로그에 대해 이미 생성된 메타데이터 목록 조회
            List<LogEmbeddingMetadata> existing = metadataRepository.findByLogId(logId);

            // 각 contentType의 존재 여부 확인
            boolean hasPrompt = existing.stream().anyMatch(m -> m.getContentType().equals("PROMPT"));
            boolean hasQuery  = existing.stream().anyMatch(m -> m.getContentType().equals("QUERY"));
            boolean hasResult = existing.stream().anyMatch(m -> m.getContentType().equals("RESULT"));

            // 누락된 contentType이 있으면 메타데이터 생성
            if (!hasPrompt) {
                metadataRepository.save(buildMetadata(log, "PROMPT", log.getPrompt()));
                addedCount++;
            }
            if (!hasQuery) {
                metadataRepository.save(buildMetadata(log, "QUERY", log.getQuery()));
                addedCount++;
            }
            if (!hasResult) {
                metadataRepository.save(buildMetadata(log, "RESULT", log.getResult()));
                addedCount++;
            }
        }

        log.info("✅ Backfill 완료: {}건의 메타데이터 생성됨", addedCount);
    }

    /**
     * ✅ LogEmbeddingMetadata 객체 생성 빌더
     *
     * @param log         원본 로그 객체
     * @param contentType 메타데이터 타입 (PROMPT, QUERY, RESULT)
     * @param content     저장할 텍스트 내용
     * @return            생성된 LogEmbeddingMetadata 객체
     */
    private LogEmbeddingMetadata buildMetadata(Log log, String contentType, String content) {
        return LogEmbeddingMetadata.builder()
                .project(log.getProject())
                .logId(log.getId())
                .contentType(contentType)
                .content(content)
                .embedded(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
