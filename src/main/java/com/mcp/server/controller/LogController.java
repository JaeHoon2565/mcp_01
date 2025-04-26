package com.mcp.server.controller;

import com.mcp.server.domain.log.Log;
import com.mcp.server.domain.log.LogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ✅ LogController
 * - 저장된 AI 추론 로그 데이터를 조회하기 위한 REST API 컨트롤러
 * - 관리자 또는 개발자용 로그 확인 목적
 */
@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogRepository logRepository;

    /** ✅ 전체 로그 목록 조회 */
    @Operation(summary = "모든 추론 로그 조회", description = "DB에 저장된 모든 추론 요청 및 응답 기록을 조회합니다.")
    @GetMapping
    public List<Log> findAll() {
        return logRepository.findAll();
    }

    /** ✅ 단일 로그 조회 */
    @Operation(summary = "단일 로그 조회", description = "ID로 특정 추론 로그를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Log.class))),
            @ApiResponse(responseCode = "404", description = "로그를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public Log findById(@PathVariable Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Log not found with id = " + id));
    }

    /** ✅ 제공자 기준 조회 */
    @Operation(summary = "제공자별 로그 조회", description = "제공자 이름(Groq 등)으로 로그를 조회합니다.")
    @GetMapping("/provider/{provider}")
    public List<Log> findByProvider(@PathVariable String provider) {
        return logRepository.findByProviderIgnoreCase(provider);
    }

    /** ✅ 모델 기준 조회 */
    @Operation(summary = "모델별 로그 조회", description = "모델 이름으로 로그를 조회합니다.")
    @GetMapping("/model/{model}")
    public List<Log> findByModel(@PathVariable String model) {
        return logRepository.findByModelIgnoreCase(model);
    }

    /** ✅ 프로젝트 기준 조회 */
    @Operation(summary = "프로젝트별 로그 조회", description = "프로젝트 이름으로 로그를 조회합니다.")
    @GetMapping("/project/{project}")
    public List<Log> findByProject(@PathVariable String project) {
        return logRepository.findByProjectIgnoreCase(project);
    }

    /** ✅ 제공자 + 모델 조합 필터링 */
    @Operation(summary = "제공자+모델 조합 로그 조회", description = "제공자와 모델로 로그를 조회합니다.")
    @GetMapping("/provider/{provider}/model/{model}")
    public List<Log> findByProviderAndModel(@PathVariable String provider, @PathVariable String model) {
        return logRepository.findByProviderIgnoreCaseAndModelIgnoreCase(provider, model);
    }

    /** ✅ 전체 최신순 */
    @Operation(summary = "전체 로그 최신순 조회", description = "로그를 생성일 기준으로 내림차순 정렬합니다.")
    @GetMapping("/latest")
    public List<Log> findAllOrderByCreatedAtDesc() {
        return logRepository.findAllByOrderByCreatedAtDesc();
    }

    /** ✅ 제공자 기준 최신순 */
    @Operation(summary = "제공자별 최신 로그 조회", description = "제공자별 로그를 최신순으로 조회합니다.")
    @GetMapping("/provider/{provider}/latest")
    public List<Log> findByProviderOrderByCreatedAtDesc(@PathVariable String provider) {
        return logRepository.findByProviderIgnoreCaseOrderByCreatedAtDesc(provider);
    }

    /** ✅ 프로젝트 + 제공자 기준 최신순 */
    @Operation(summary = "프로젝트+제공자 최신 로그 조회", description = "프로젝트와 제공자 기준으로 최신순 조회합니다.")
    @GetMapping("/project/{project}/provider/{provider}/latest")
    public List<Log> findByProjectAndProviderOrderByCreatedAtDesc(@PathVariable String project, @PathVariable String provider) {
        return logRepository.findByProjectIgnoreCaseAndProviderIgnoreCaseOrderByCreatedAtDesc(project, provider);
    }
}
