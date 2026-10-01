package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.CampDto;
import com.reliefhub.service.AuthService;
import com.reliefhub.service.CampService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camps")
public class CampController {

    private final CampService campService;

    public CampController(CampService campService) {
        this.campService = campService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CampDto.CampResponse>>> getAllCamps() {
        List<CampDto.CampResponse> camps = campService.getAllCamps();
        return ResponseEntity.ok(ApiResponse.ok("Camps retrieved", camps));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> getCampById(@PathVariable String id) {
        ApiResponse<CampDto.CampResponse> resp = campService.getCampById(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(404).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/my-camp")
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> getMyCamp(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthService.SESSION_USER_ID);
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }

        ApiResponse<CampDto.CampResponse> resp = campService.getCampByManager(userId);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(404).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> createCamp(@Valid @RequestBody CampDto.CampCreateRequest req) {
        ApiResponse<CampDto.CampResponse> resp = campService.createCamp(req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> masterUpdateCamp(
            @PathVariable String id,
            @Valid @RequestBody CampDto.CampUpdateRequest req) {
        ApiResponse<CampDto.CampResponse> resp = campService.masterUpdateCamp(id, req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/supplies")
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> updateSupplies(
            @PathVariable String id,
            @Valid @RequestBody CampDto.CampSuppliesUpdateRequest req) {
        ApiResponse<CampDto.CampResponse> resp = campService.updateSupplies(id, req.getSupplies());
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/capacity")
    public ResponseEntity<ApiResponse<CampDto.CampResponse>> updateCapacity(
            @PathVariable String id,
            @Valid @RequestBody CampDto.CampCapacityUpdateRequest req) {
        ApiResponse<CampDto.CampResponse> resp = campService.updateCapacity(id, req.getCapacity(), req.getAvailableSpace());
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }
}
