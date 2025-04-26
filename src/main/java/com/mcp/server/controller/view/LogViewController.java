package com.mcp.server.controller.view;

import com.mcp.server.domain.log.Log;
import com.mcp.server.domain.log.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ✅ LogViewController
 * - 관리자 웹에서 추론 로그를 렌더링하는 전용 컨트롤러
 * - 제공자 / 모델명 조건으로 필터링 기능 포함
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LogViewController {

    private final LogRepository logRepository;

    /**
     * ✅ 로그 리스트 화면 렌더링
     * - 필터 조건이 있을 경우 해당 기준으로 조회
     * - 대소문자 무시 (IgnoreCase)
     */
    @GetMapping("/logs")
    public String logs(@RequestParam(required = false) String provider,
                       @RequestParam(required = false) String model,
                       Model modelAttr) {

        List<Log> logs;

        if (provider != null && !provider.isEmpty() && model != null && !model.isEmpty()) {
            logs = logRepository.findByProviderIgnoreCaseAndModelIgnoreCase(provider, model);
        } else if (provider != null && !provider.isEmpty()) {
            logs = logRepository.findByProviderIgnoreCaseOrderByCreatedAtDesc(provider);
        } else if (model != null && !model.isEmpty()) {
            logs = logRepository.findByModelIgnoreCase(model);
        } else {
            logs = logRepository.findAllByOrderByCreatedAtDesc();
        }

        modelAttr.addAttribute("logs", logs);
        modelAttr.addAttribute("provider", provider); // 필터 입력값 유지
        modelAttr.addAttribute("model", model);       // 필터 입력값 유지

        return "admin/logs";
    }
}
