package com.projet.gestionStock.controller;

import com.projet.gestionStock.dto.response.DashboardStatsResponse;
import com.projet.gestionStock.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        // Fetch real-time aggregated metrics from the service layer
        DashboardStatsResponse stats = dashboardService.getDashboardStats();
        
        // Return HTTP 200 OK along with the compiled analytics payload
        return ResponseEntity.ok(stats);
    }


    // We will write the endpoint method here in the next step
}