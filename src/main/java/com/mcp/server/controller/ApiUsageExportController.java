package com.mcp.server.controller;


import com.mcp.server.service.ApiUsageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class ApiUsageExportController {

    private final ApiUsageService apiUsageService;

    @GetMapping("/admin/export/download")
    public void exportCsv(String from, String to, HttpServletResponse response) {
        LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ISO_DATE);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"usage_data.csv\"");

        apiUsageService.exportUsageAsCsv(fromDate, toDate, response);
    }
}