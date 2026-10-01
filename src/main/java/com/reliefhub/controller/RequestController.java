package com.reliefhub.controller;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.RequestDto;
import com.reliefhub.service.AuthService;
import com.reliefhub.service.RequestService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RequestDto.RequestResponse>>> getAllRequests(
            @RequestParam(required = false) String campId,
            @RequestParam(required = false) String status) {
        List<RequestDto.RequestResponse> list;
        if (campId != null && !campId.isBlank()) {
            list = requestService.getRequestsByCampId(campId);
        } else {
            list = requestService.getAllRequests();
        }

        if (status != null && !status.isBlank()) {
            list = list.stream()
                    .filter(r -> r.getStatus().equalsIgnoreCase(status.trim()))
                    .toList();
        }
        return ResponseEntity.ok(ApiResponse.ok("Requests retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> getRequestById(@PathVariable String id) {
        ApiResponse<RequestDto.RequestResponse> resp = requestService.getRequestById(id);
        if (!resp.isSuccess()) {
            return ResponseEntity.status(404).body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse<List<RequestDto.RequestResponse>>> getMyRequests(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthService.SESSION_USER_ID);
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Not authenticated"));
        }

        List<RequestDto.RequestResponse> list = requestService.getRequestsByVictimUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok("My requests retrieved", list));
    }

    @GetMapping("/camp/{campId}")
    public ResponseEntity<ApiResponse<List<RequestDto.RequestResponse>>> getCampRequests(@PathVariable String campId) {
        List<RequestDto.RequestResponse> list = requestService.getRequestsByCampId(campId);
        return ResponseEntity.ok(ApiResponse.ok("Camp requests retrieved", list));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> createRequest(
            @Valid @RequestBody RequestDto.CreateRequest req,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthService.SESSION_USER_ID);
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Please login to submit an assistance request"));
        }

        ApiResponse<RequestDto.RequestResponse> resp = requestService.createRequest(req, userId);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody RequestDto.StatusUpdateRequest req) {
        ApiResponse<RequestDto.RequestResponse> resp = requestService.updateStatus(id, req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> assignCamp(
            @PathVariable String id,
            @Valid @RequestBody RequestDto.AssignCampRequest req) {
        ApiResponse<RequestDto.RequestResponse> resp = requestService.assignCamp(id, req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/{id}/confirm-receipt")
    public ResponseEntity<ApiResponse<RequestDto.RequestResponse>> confirmReceipt(
            @PathVariable String id,
            @RequestBody(required = false) RequestDto.ConfirmReceiptRequest req,
            HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthService.SESSION_USER_ID);
        ApiResponse<RequestDto.RequestResponse> resp = requestService.confirmReceipt(id, userId, req);
        if (!resp.isSuccess()) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }
}
