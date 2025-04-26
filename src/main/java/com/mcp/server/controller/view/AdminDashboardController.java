package com.mcp.server.controller.view;

import com.mcp.server.dto.ModelInfo;
import com.mcp.server.service.ApiUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ✅ AdminDashboardController
 * 관리자 대시보드 첫 화면을 반환하는 컨트롤러
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final ApiUsageService apiUsageService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ✅ 모델을 provider별로 그룹화
        List<ModelInfo> models = apiUsageService.getAvailableModels();

        Map<String, List<ModelInfo>> groupedModels = models.stream()
                .collect(Collectors.groupingBy(ModelInfo::getProvider));

        model.addAttribute("modelGroups", groupedModels);  // 👈 변경된 attribute
        model.addAttribute("stats", apiUsageService.getTodayStatsGroupedByModel());

        return "admin/dashboard";
    }

}