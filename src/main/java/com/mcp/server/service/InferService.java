package com.mcp.server.service;

import com.mcp.server.client.AiModelClient;
import com.mcp.server.dto.InferRequest;
import com.mcp.server.dto.InferResponse;
import com.mcp.server.dto.ModelInfo;

import com.mcp.server.util.PromptFormatter;
import com.mcp.server.util.TokenCounter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ✅ InferService
 * - 사용자 요청을 기반으로 프롬프트를 구성하고,
 * - 적절한 AI 모델을 호출하며,
 * - 결과를 로그로 저장하고 통계를 기록하는 핵심 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InferService {

    private final List<AiModelClient> modelClients;      // ✅ 지원하는 모델 클라이언트 목록 (Groq, Together 등)
    private final LogService logService;                 // ✅ 로그 저장용 서비스
    private final ApiUsageService apiUsageService;       // ✅ 사용량 기록용 서비스
    private final ModelRateLimiter modelRateLimiter;     // ✅ 호출 제한 체크
    private final ContextSetService contextSetService;   // ✅ ContextSet을 조회하는 서비스

    /**
     * ✅ handleInference
     * - 사용자의 추론 요청을 받아 모델 호출 → 결과 저장까지 처리하는 핵심 메서드
     *
     * @param request      사용자 요청 DTO (contextSetId 기반)
     * @param httpRequest  클라이언트 IP 추출용 서블릿 요청
     * @return             모델 응답 결과 + 부가 정보 포함 응답 객체
     */
    public InferResponse handleInference(InferRequest request, HttpServletRequest httpRequest) {
        log.info("📩 요청 수신: {}", request); // 요청 로그

        // 1. 모델별 호출 제한 검사 (제한 초과 시 예외 발생)
        modelRateLimiter.checkQuota(request.getModel());

        // 2. contextSetId 기반으로 JSON 형태 context 구성
        String contextJson = contextSetService
                .getContextSet(request.getContextSetId()) // ID로 조회 (예외 포함)
                .toContextJson(); // → contextJson 문자열 반환

        // 3. 프롬프트 생성 (contextJson + query → 모델 입력값)
        String prompt = PromptFormatter.formatPrompt(
                request.getProject(),
                contextJson,
                request.getQuery()
        );

        // 4. 토큰 수 계산 (단순 추정)
        int tokensUsed = TokenCounter.countForPrompt(prompt);

        // 5. 모델명으로 클라이언트 선택 (ex: groq-llama3)
        AiModelClient client = modelClients.stream()
                .filter(c -> c.supports(request.getModel()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("❌ 지원하지 않는 모델입니다: " + request.getModel()));

        // 6. 모델 호출
        long start = System.currentTimeMillis();
        String result = client.call(request.getModel(), prompt); // 실제 호출
        long elapsed = System.currentTimeMillis() - start;

        log.info("✅ 모델 응답: {}", result);

        // 7. 로그 저장
        logService.saveLog(
                request.getProject(),
                request.getProvider(),
                request.getModel(),
                prompt,
                request.getQuery(),
                result,
                elapsed + "ms"
        );

        // 8. 사용량 통계 기록
        String clientIp = httpRequest.getRemoteAddr();
        apiUsageService.record(request.getModel(), tokensUsed, elapsed, clientIp);

        // 9. 최종 응답 반환
        return InferResponse.builder()
                .result(result)
                .prompt(prompt)
                .model(request.getModel())
                .elapsed(elapsed + "ms")
                .tokensUsed(tokensUsed)
                .build();
    }

    /**
     * ✅ getAvailableModels
     * - 현재 사용 가능한 모델 목록 조회 (Groq / Together 등 통합 관리)
     */
    public List<ModelInfo> getAvailableModels() {
        return apiUsageService.getAvailableModels();
    }
}
