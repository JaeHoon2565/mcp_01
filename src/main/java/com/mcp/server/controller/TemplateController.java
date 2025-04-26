package com.mcp.server.controller;

import com.mcp.server.domain.template.TemplateRepository;
import com.mcp.server.dto.TemplateRequest;
import com.mcp.server.dto.TemplateResponse;
import com.mcp.server.util.TemplateMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
// ✅ 14. TemplateController
// 템플릿을 등록하고 조회하는 API
// ✅ 15. TemplateController 리팩토링 (DTO 기반)
// TemplateRequest → 저장, TemplateResponse → 반환
*/
@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateRepository templateRepository;

    @Operation(summary = "모든 템플릿 조회", description = "저장된 모든 context 템플릿을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TemplateResponse.class)))
    @GetMapping
    public List<TemplateResponse> findAll() {
        return TemplateMapper.toResponseList(templateRepository.findAll());
    }

    @Operation(summary = "템플릿 등록", description = "새로운 context 템플릿을 저장합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 저장됨",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TemplateResponse.class)))
    @PostMapping
    public TemplateResponse save(@RequestBody TemplateRequest request) {
        return TemplateMapper.toResponse(
                templateRepository.save(TemplateMapper.toEntity(request))
        );
    }

    @Operation(summary = "프로젝트별 템플릿 조회", description = "특정 프로젝트에 속한 템플릿 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회됨",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TemplateResponse.class)))
    @GetMapping("/project/{project}")
    public List<TemplateResponse> findByProject(@PathVariable String project) {
        return TemplateMapper.toResponseList(templateRepository.findByProject(project));
    }

    @Operation(summary = "템플릿 삭제", description = "템플릿 ID를 기준으로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공적으로 삭제됨"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 ID")
    })
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        if (!templateRepository.existsById(id)) {
            throw new IllegalArgumentException("Template not found with id = " + id);
        }
        templateRepository.deleteById(id);
    }
}
