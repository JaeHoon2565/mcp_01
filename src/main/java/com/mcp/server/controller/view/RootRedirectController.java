package com.mcp.server.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootRedirectController {

    @GetMapping("/")
    public String redirectToAdmin() {
        return "redirect:/admin/dashboard";
    }
}
