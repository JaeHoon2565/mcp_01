package com.mcp.server.service;

import com.mcp.server.client.AiModelClient;
import com.mcp.server.domain.usage.ApiUsageLog;
import com.mcp.server.domain.usage.ApiUsageLogRepository;
import com.mcp.server.dto.ApiUsageStats;
import com.mcp.server.dto.ApiUsageSummary;
import com.mcp.server.dto.ModelInfo;
import com.mcp.server.dto.UsageStatResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ✅ ApiUsageService
 *
 * Groq / GPT / Claude 등 다양한 AI 모델에 대한 사용량을 저장 및 조회하는 서비스.
 *
 * [주요 기능]
 * - API 호출 기록(`ApiUsageLog`) 저장
 * - 오늘 날짜 및 날짜 범위 기준 사용 기록 조회
 * - 모델별 사용 통계 계산 (총 호출 수, 총 토큰 수, 평균 응답 시간)
 */
@Service
@RequiredArgsConstructor
public class ApiUsageService {

    private final ApiUsageLogRepository repo;

    // ✅ 등록된 모델 클라이언트들 (Groq, GPT 등)
    private final List<AiModelClient> modelClients;

    /**
     * ✅ record
     * API 호출 시 사용된 모델, 토큰 수, 소요 시간, IP 주소를 저장합니다.
     *
     * @param model      호출된 모델 이름 (예: groq, gpt, claude 등)
     * @param tokensUsed 사용된 토큰 수
     * @param elapsedMs  응답 시간 (ms)
     * @param ipAddress  요청자의 IP 주소
     */
    public void record(String model, int tokensUsed, long elapsedMs, String ipAddress) {
        ApiUsageLog log = new ApiUsageLog(model, tokensUsed, elapsedMs, ipAddress);
        repo.save(log);
    }

    /**
     * ✅ getTodayLogs
     * 오늘 하루 동안 발생한 모든 모델의 API 호출 로그를 반환합니다.
     *
     * @return 전체 로그 리스트
     */
    public List<ApiUsageLog> getTodayLogs() {
        return repo.findByDate(LocalDate.now());
    }

    /**
     * ✅ getTodayLogsByModel
     * 오늘 하루 동안 특정 모델에 대해 호출된 API 로그만 반환합니다.
     *
     * @param model 모델 이름
     * @return 해당 모델의 로그 리스트
     */
    public List<ApiUsageLog> getTodayLogsByModel(String model) {
        return repo.findByDateAndModel(LocalDate.now(), model);
    }

    /**
     * ✅ getTodayStatsGroupedByModel
     * 오늘 날짜 기준으로 모델별 사용 통계를 계산하여 반환합니다.
     * - 총 호출 수
     * - 총 사용 토큰 수
     * - 평균 응답 시간(ms)
     *
     * @return 모델별 통계 리스트
     */
    public List<UsageStatResponse> getTodayStatsGroupedByModel() {
        List<ApiUsageLog> todayLogs = repo.findByDate(LocalDate.now());

        return todayLogs.stream()
                .collect(Collectors.groupingBy(ApiUsageLog::getModel))  // 모델별 그룹화
                .entrySet()
                .stream()
                .map(entry -> {
                    String model = entry.getKey();
                    List<ApiUsageLog> logs = entry.getValue();

                    int totalTokens = logs.stream().mapToInt(ApiUsageLog::getTokensUsed).sum();
                    long avgElapsed = (long) logs.stream().mapToLong(ApiUsageLog::getElapsedTimeMs).average().orElse(0);

                    return new UsageStatResponse(model, logs.size(), totalTokens, avgElapsed);
                })
                .collect(Collectors.toList());
    }

    /**
     * ✅ getLogsBetweenDates
     * 지정된 날짜 범위 내의 모든 API 호출 로그를 반환합니다.
     *
     * @param from 시작 날짜 (포함)
     * @param to   끝 날짜 (포함)
     * @return 로그 리스트
     */
    public List<ApiUsageLog> getLogsBetweenDates(LocalDate from, LocalDate to) {
        return repo.findByDateBetween(from, to);
    }

    /**
     * ✅ getStatsGroupedByModelBetween
     * 지정된 날짜 범위 내에서 모델별 사용 통계를 계산하여 반환합니다.
     * - 총 호출 수
     * - 총 사용 토큰 수
     * - 평균 응답 시간(ms)
     *
     * @param from 시작 날짜 (포함)
     * @param to   끝 날짜 (포함)
     * @return 모델별 통계 리스트
     */
    public List<UsageStatResponse> getStatsGroupedByModelBetween(LocalDate from, LocalDate to) {
        List<ApiUsageLog> logs = repo.findByDateBetween(from, to);

        return logs.stream()
                .collect(Collectors.groupingBy(ApiUsageLog::getModel))  // 모델별 그룹화
                .entrySet()
                .stream()
                .map(entry -> {
                    String model = entry.getKey();
                    List<ApiUsageLog> modelLogs = entry.getValue();

                    int totalTokens = modelLogs.stream().mapToInt(ApiUsageLog::getTokensUsed).sum();
                    long avgElapsed = (long) modelLogs.stream().mapToLong(ApiUsageLog::getElapsedTimeMs).average().orElse(0);

                    return new UsageStatResponse(model, modelLogs.size(), totalTokens, avgElapsed);
                })
                .collect(Collectors.toList());
    }

    /**
     * ✅ exportLogsAsCsv
     * 주어진 날짜 범위(from~to)의 API 사용 로그를 CSV 문자열로 반환합니다.
     *
     * @param from 시작 날짜
     * @param to   끝 날짜
     * @return CSV 형식 문자열
     */
    public String exportLogsAsCsv(LocalDate from, LocalDate to) {
        List<ApiUsageLog> logs = getLogsBetweenDates(from, to);

        StringBuilder csv = new StringBuilder();
        csv.append("Model,TokensUsed,ElapsedTimeMs,IP,Date,CreatedAt\n");

        for (ApiUsageLog log : logs) {
            csv.append(log.getModel()).append(",")
                    .append(log.getTokensUsed()).append(",")
                    .append(log.getElapsedTimeMs()).append(",")
                    .append(log.getIpAddress()).append(",")
                    .append(log.getDate()).append(",")
                    .append(log.getCreatedAt()).append("\n");
        }

        return csv.toString();
    }

    /**
     * ✅ getAvailableModels
     * 현재 등록된 클라이언트 기준으로 사용할 수 있는 모델 목록을 반환합니다.
     * - Groq, TogetherAI, OpenAI 등 클라이언트가 자동 반환한 목록 기반
     *
     * @return 모델 정보 리스트
     */
    public List<ModelInfo> getAvailableModels() {
        List<ModelInfo> result = new ArrayList<>();

        for (AiModelClient client : modelClients) {
            // ✅ 제공자 이름 추출 (ex: GroqAiModelClientImpl → Groq)
            String className = client.getClass().getSimpleName(); // 예: GroqAiModelClientImpl
            String provider = className.replace("AiModelClientImpl", ""); // Groq, Together 등
            String source = provider + "에서 자동 등록된 모델";

            // ✅ 클라이언트가 반환한 모델 목록 순회
            for (String modelId : client.getSupportedModels()) {
                result.add(new ModelInfo(modelId, provider, source));
            }
        }

        return result;
    }


    /**
     * ✅ exportUsageAsCsv
     * 통계 기반으로 요약 정보를 CSV로 출력 (날짜별 사용량)
     */
    public void exportUsageAsCsv(LocalDate from, LocalDate to, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            writer.println("날짜,모델명,요청 수,총 토큰 수,평균 응답 시간(ms)");

            List<ApiUsageStats> usages = repo.findStatsBetween(from, to);
            for (ApiUsageStats usage : usages) {
                writer.printf("%s,%s,%d,%d,%.2f%n",
                        usage.getDate(),
                        usage.getModel(),
                        usage.getCount(),
                        usage.getTotalTokens(),
                        usage.getAverageElapsed());
            }

        } catch (IOException e) {
            throw new RuntimeException("CSV 생성 실패", e);
        }
    }

    public List<ApiUsageSummary> getUsageSummary(LocalDate from, LocalDate to) {
        return repo.findSummaryBetween(from, to); // JPQL에서 DTO 직접 리턴해도 됨
    }
}
