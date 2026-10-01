package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.ReportDto;
import com.reliefhub.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportDto.ReportResponse>>> getAllReports() {
        List<ReportDto.ReportResponse> list = reportService.getAllReports();
        return ResponseEntity.ok(ApiResponse.ok("Reports retrieved", list));
    }

    @GetMapping("/camp/{campId}")
    public ResponseEntity<ApiResponse<List<ReportDto.ReportResponse>>> getCampReports(@PathVariable String campId) {
        List<ReportDto.ReportResponse> list = reportService.getReportsByCampId(campId);
        return ResponseEntity.ok(ApiResponse.ok("Camp reports retrieved", list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReportDto.ReportResponse>> createReport(
            @Valid @RequestBody ReportDto.CreateReportRequest req) {
        ApiResponse<ReportDto.ReportResponse> resp = reportService.createReport(req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ReportDto.DashboardSummary>> getSummary() {
        ReportDto.DashboardSummary summary = reportService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.ok("Dashboard summary metrics retrieved", summary));
    }
}
