package com.mcp.server.controller;

import com.mcp.server.dto.InferRequest;
import com.mcp.server.dto.InferResponse;
import com.mcp.server.service.InferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 // ✅ 4. InferController
// 추론 요청을 처리하는 REST 컨트롤러
// - POST /infer 요청을 받고 DTO 검증 후 서비스로 위임
 */

@RestController
@RequestMapping("/infer")
@RequiredArgsConstructor
public class InferController {

    private final InferService inferService;

    @Operation(
            summary = "AI 추론 요청",
            description = "Query 및 context 기반으로 AI 모델에 프롬프트 요청 후 응답 반환"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상 응답",
                    content = @Content(schema = @Schema(implementation = InferResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<InferResponse> infer(
            @Valid @RequestBody InferRequest request,
            HttpServletRequest httpRequest
    ) {
        InferResponse response = inferService.handleInference(request, httpRequest);
        return ResponseEntity.ok(response);
    }
}