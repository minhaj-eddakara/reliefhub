package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.CampDto;
import com.reliefhub.dto.ReportDto;
import com.reliefhub.dto.RequestDto;
import com.reliefhub.service.CampService;
import com.reliefhub.service.ReportService;
import com.reliefhub.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CampService campService;
    private final ReportService reportService;
    private final RequestService requestService;

    public PublicController(CampService campService, ReportService reportService, RequestService requestService) {
        this.campService = campService;
        this.reportService = reportService;
        this.requestService = requestService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ReportDto.DashboardSummary>> getPublicSummary() {
        return ResponseEntity.ok(ApiResponse.ok("Public summary retrieved", reportService.getDashboardSummary()));
    }

    @GetMapping("/camps")
    public ResponseEntity<ApiResponse<List<CampDto.CampResponse>>> getPublicCamps() {
        return ResponseEntity.ok(ApiResponse.ok("Public relief camps retrieved", campService.getAllCamps()));
    }

    @GetMapping("/track/{requestId}")
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> trackRequest(@PathVariable String requestId) {
        ApiResponse<RequestDto.RequestResponse> resp = requestService.getRequestById(requestId);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(404).body(resp);
        }
        return ResponseEntity.ok(resp);
    }
}
