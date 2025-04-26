package com.mcp.server.controller.view;

import com.mcp.server.dto.InferRequest;
import com.mcp.server.dto.ModelInfo;
import com.mcp.server.domain.context.ContextSet;
import com.mcp.server.service.ContextSetService;
import com.mcp.server.service.InferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/playground")
@RequiredArgsConstructor
public class PlaygroundViewController {

    private final InferService inferService;
    private final ContextSetService contextSetService;

    @GetMapping
    public String form(Model model) {
        List<ModelInfo> models = inferService.getAvailableModels();

        // ✅ 제공자별로 그룹핑
        Map<String, List<ModelInfo>> groupedModels = models.stream()
                .collect(Collectors.groupingBy(ModelInfo::getProvider));

        // ✅ 컨텍스트 세트 목록 가져오기
        List<ContextSet> contextSets = contextSetService.getAllContextSets();

        model.addAttribute("groupedModels", groupedModels);
        model.addAttribute("contextSets", contextSets);
        model.addAttribute("request", new InferRequest());
        return "admin/playground";
    }
}
