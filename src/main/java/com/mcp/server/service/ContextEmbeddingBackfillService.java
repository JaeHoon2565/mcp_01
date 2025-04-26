/**
 * ✅ ContextEmbeddingBackfillService
 * - 누락된 ContextEmbeddingMetadata를 주기적으로 생성해주는 백필(batch) 처리 전용 서비스
 * - ContextSet 데이터는 존재하지만 메타데이터가 생성되지 않은 항목을 찾아 자동 생성
 *
 *대상: context_sets 테이블
 *목적: 추론에 사용된 컨텍스트 설정값 (페르소나, 역할, 상황 등)을 임베딩하여 LLM에게 맥락 이해 제공
 *용도: 추론 시 상황 정보 기반의 응답 품질 향상, 유사 상황 검색 등
 *contentType: CONTEXT (현재 고정)

 */
package com.mcp.server.service;

import com.mcp.server.domain.context.ContextSet;
import com.mcp.server.domain.context.ContextSetRepository;
import com.mcp.server.domain.embedding.ContextEmbeddingMetadata;
import com.mcp.server.domain.embedding.ContextEmbeddingMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContextEmbeddingBackfillService {

    private static final Logger log = LoggerFactory.getLogger(ContextEmbeddingBackfillService.class);

    private final ContextSetRepository contextSetRepository;
    private final ContextEmbeddingMetadataRepository metadataRepository;

    /**
     * ✅ 전체 ContextSet 중 메타데이터가 누락된 항목을 찾아 보충하는 작업
     * - 매 실행 시, 각 ContextSet에 대해 CONTEXT 유형 메타데이터가 없는 경우 생성
     */
    public void backfillMissingMetadata() {
        List<ContextSet> allContexts = contextSetRepository.findAll();
        int addedCount = 0;

        for (ContextSet context : allContexts) {
            Long contextSetId = context.getId();

            // 해당 ContextSet에 대해 이미 생성된 메타데이터 목록 조회
            List<ContextEmbeddingMetadata> existing = metadataRepository.findByContextSetId(contextSetId);

            boolean hasContext = !existing.isEmpty();

            // 메타데이터가 존재하지 않으면 생성
            if (!hasContext) {
                metadataRepository.save(buildMetadata(context));
                addedCount++;
            }
        }

        log.info("✅ Context 백필 완료: {}건의 메타데이터 생성됨", addedCount);
    }

    /**
     * ✅ ContextEmbeddingMetadata 생성 빌더
     */
    private ContextEmbeddingMetadata buildMetadata(ContextSet context) {
        return ContextEmbeddingMetadata.builder()
                .project("mcp") // 필요시 ContextSet에 따라 프로젝트 필드 구분 가능
                .contextSetId(context.getId())
                .contextType("CONTEXT")
                .content(context.toContextJson())
                .embedded(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
