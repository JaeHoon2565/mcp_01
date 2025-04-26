package com.mcp.server.scheduler;

import com.mcp.server.service.ContextEmbeddingBackfillService;
import com.mcp.server.service.LogEmbeddingBackfillService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmbeddingScheduler {

    private final LogEmbeddingBackfillService logEmbeddingBackfillService;
    private final ContextEmbeddingBackfillService contextEmbeddingBackfillService;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void runLogEmbeddingBackfill() {
        logEmbeddingBackfillService.backfillMissingMetadata();
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void runContextEmbeddingBackfill() {
        contextEmbeddingBackfillService.backfillMissingMetadata();
    }

    /*
     log_metadata는 추론 결과 자체의 품질 비교, 벡터 검색, 히스토리 관리에 좋다
     context_metadata는 문맥 추천, 유사 인물 페르소나 매칭, 상황 기반 설명에 활용

    이처럼 역할을 분리해

    특정 contextSet의 추론 품질이 떨어졌을 때 해당 context 자체를 개선하거나,

    prompt/result의 품질을 서로 다른 기준으로 분석하거나,

    context 임베딩만 교체하고 로그는 유지하는 식의 유연한 설계가 가능.

    context_json이 어떤 식으로 구성되는지 더 정제하거나, 향후 tone이나 goal 단위로도 세분화해서 벡터화할 계획이 있다라면, 그에 맞춰 추가 확장도 가능
    */
}