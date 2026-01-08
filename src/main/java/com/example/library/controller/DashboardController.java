package com.example.library.controller;

import com.example.library.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", dashboardService.countBooks());
        model.addAttribute("totalCopies", dashboardService.countCopies());
        model.addAttribute("borrowed", dashboardService.countBorrowed());
        model.addAttribute("overdue", dashboardService.countOverdue());
        model.addAttribute("members", dashboardService.countMembers());
        return "dashboard";
    }
}