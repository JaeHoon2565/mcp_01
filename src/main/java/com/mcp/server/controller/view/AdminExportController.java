package com.mcp.server.controller.view;

import com.mcp.server.dto.ApiUsageSummary;
import com.mcp.server.service.ApiUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * ✅ 관리자 다운로드 페이지 컨트롤러
 */
@Controller
@RequestMapping("/admin/export")
@RequiredArgsConstructor
public class AdminExportController {

    private final ApiUsageService apiUsageService;

    @GetMapping
    public String exportPage(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            Model model
    ) {
        if (from != null && to != null) {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            List<ApiUsageSummary> summaries = apiUsageService.getUsageSummary(fromDate, toDate);
            model.addAttribute("summaries", summaries);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
        }

        return "admin/export";
    }
}