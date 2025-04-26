package com.mcp.server.controller;

import com.mcp.server.domain.usage.ApiUsageLog;
import com.mcp.server.dto.UsageStatResponse;
import com.mcp.server.service.ApiUsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * ✅ ApiUsageController
 * AI API 사용량 통계 및 기록 조회를 위한 REST 컨트롤러.
 * - 오늘자 사용 기록
 * - 모델별 통계
 * - 기간별 통계 및 조회
 */
@RestController
@RequestMapping("/usage")
@RequiredArgsConstructor
public class ApiUsageController {

    private final ApiUsageService apiUsageService;

    /**
     * ✅ 오늘 전체 모델 사용 기록 조회
     */
    @Operation(
            summary = "오늘 전체 모델 사용 기록 조회",
            description = "오늘 하루 동안 호출된 모든 모델의 API 사용 기록을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 사용 기록 반환됨"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApiUsageLog> getTodayUsage() {
        return apiUsageService.getTodayLogs();
    }

    /**
     * ✅ 오늘 특정 모델의 사용 기록 조회
     */
    @Operation(
            summary = "오늘 특정 모델 사용 기록 조회",
            description = "오늘 하루 동안 특정 모델(groq, gpt 등)의 API 사용 로그를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 모델의 사용 기록 반환"),
            @ApiResponse(responseCode = "404", description = "모델 기록 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping(value = "/today/{model}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApiUsageLog> getTodayUsageByModel(
            @Parameter(description = "모델 이름 (예: groq, gpt, claude 등)", example = "groq")
            @PathVariable String model
    ) {
        return apiUsageService.getTodayLogsByModel(model);
    }

    /**
     * ✅ 오늘 모델별 통계 조회
     */
    @Operation(
            summary = "오늘 모델별 사용량 통계",
            description = "오늘 날짜 기준 모델별 호출 수, 총 토큰 사용량, 평균 응답 시간(ms)을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "모델별 통계 반환")
    @GetMapping(value = "/stats/today", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UsageStatResponse> getTodayStatsByModel() {
        return apiUsageService.getTodayStatsGroupedByModel();
    }

    /**
     * ✅ 날짜 범위로 전체 기록 조회
     */
    @Operation(
            summary = "지정된 기간의 API 호출 기록 조회",
            description = "from~to 날짜 범위 내의 전체 모델 호출 로그를 반환합니다. (YYYY-MM-DD 형식)"
    )
    @ApiResponse(responseCode = "200", description = "기간 내 모든 API 호출 로그 반환")
    @GetMapping(value = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ApiUsageLog> getUsageBetweenDates(
            @RequestParam("from") @Parameter(example = "2025-04-01") String from,
            @RequestParam("to") @Parameter(example = "2025-04-06") String to
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        return apiUsageService.getLogsBetweenDates(fromDate, toDate);
    }

    /**
     * ✅ 날짜 범위로 모델별 통계 조회
     */
    @Operation(
            summary = "기간별 모델별 통계 조회",
            description = "from~to 날짜 범위 내에서 모델별로 호출 수, 토큰 수, 평균 응답 시간(ms)을 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "모델별 통계 반환")
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UsageStatResponse> getStatsBetweenDates(
            @RequestParam("from") @Parameter(example = "2025-04-01") String from,
            @RequestParam("to") @Parameter(example = "2025-04-06") String to
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        return apiUsageService.getStatsGroupedByModelBetween(fromDate, toDate);
    }

    @Operation(
            summary = "기간별 사용 기록 CSV 다운로드",
            description = "지정한 날짜(from~to) 범위 내의 API 호출 기록을 CSV 형식으로 다운로드합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CSV 파일 다운로드 성공",
                    content = @Content(mediaType = "text/csv")),
            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식 또는 요청 파라미터"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam("from") @Parameter(example = "2025-04-01") String from,
            @RequestParam("to") @Parameter(example = "2025-04-06") String to
    ) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);

        String csv = apiUsageService.exportLogsAsCsv(fromDate, toDate);
        byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"api-usage.csv\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .body(csvBytes);
    }
}
