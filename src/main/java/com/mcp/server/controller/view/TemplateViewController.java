package com.mcp.server.controller.view;

import com.mcp.server.domain.template.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/templates")
@RequiredArgsConstructor
public class TemplateViewController {

    private final TemplateRepository templateRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("templates", templateRepository.findAll());
        return "admin/templates";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        templateRepository.deleteById(id);
        return "redirect:/admin/templates";
    }
}