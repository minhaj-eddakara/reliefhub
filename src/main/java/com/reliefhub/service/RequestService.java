package com.reliefhub.service;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.RequestDto;
import com.reliefhub.model.Camp;
import com.reliefhub.model.Request;
import com.reliefhub.model.Victim;
import com.reliefhub.repository.CampRepository;
import com.reliefhub.repository.RequestRepository;
import com.reliefhub.repository.VictimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final VictimRepository victimRepository;
    private final CampRepository campRepository;
    private final CampService campService;

    private static final AtomicInteger REQ_COUNTER = new AtomicInteger(1005);

    public RequestService(RequestRepository requestRepository,
                          VictimRepository victimRepository,
                          CampRepository campRepository,
                          CampService campService) {
        this.requestRepository = requestRepository;
        this.victimRepository = victimRepository;
        this.campRepository = campRepository;
        this.campService = campService;
    }

    public List<RequestDto.RequestResponse> getAllRequests() {
        return requestRepository.findAllByOrderByRequestDateDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApiResponse<RequestDto.RequestResponse> getRequestById(String requestId) {
        return requestRepository.findById(requestId.trim().toUpperCase())
                .map(r -> ApiResponse.ok("Request found", mapToResponse(r)))
                .orElse(ApiResponse.error("Request ID not found: " + requestId));
    }

    public List<RequestDto.RequestResponse> getRequestsByVictimUserId(Integer userId) {
        Optional<Victim> victimOpt = victimRepository.findByUser_UserId(userId);
        if (victimOpt.isEmpty()) {
            return List.of();
        }
        return requestRepository.findByVictimOrderByRequestDateDesc(victimOpt.get()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<RequestDto.RequestResponse> getRequestsByCampId(String campId) {
        return requestRepository.findByCamp_CampIdOrderByRequestDateDesc(campId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiResponse<RequestDto.RequestResponse> createRequest(RequestDto.CreateRequest req, Integer userId) {
        Optional<Victim> victimOpt = victimRepository.findByUser_UserId(userId);
        if (victimOpt.isEmpty()) {
            return ApiResponse.error("Only registered victims can submit assistance requests.");
        }

        Victim victim = victimOpt.get();
        String requestId = "REQ-" + REQ_COUNTER.getAndIncrement();

        Request request = new Request();
        request.setRequestId(requestId);
        request.setVictim(victim);
        request.setRequestType(req.getRequestType().trim());
        request.setRequestDate(LocalDate.now());
        request.setDetails(req.getDetails().trim());

        // Check if preferred camp specified or auto-allocate
        Camp assignedCamp = null;
        if (req.getPreferredCampId() != null && !req.getPreferredCampId().isBlank()) {
            assignedCamp = campRepository.findById(req.getPreferredCampId()).orElse(null);
        }

        // Automatic allocation if no specific camp selected
        if (assignedCamp == null) {
            Optional<Camp> bestCampOpt = campService.findBestCampForAllocation();
            if (bestCampOpt.isPresent()) {
                assignedCamp = bestCampOpt.get();
            }
        }

        if (assignedCamp != null) {
            request.setCamp(assignedCamp);
            request.setStatus("Assigned");
            // If shelter or aid request, adjust available space if available
            if (("Shelter".equalsIgnoreCase(req.getRequestType()) || "Rescue".equalsIgnoreCase(req.getRequestType()))
                    && assignedCamp.getAvailableSpace() > 0) {
                assignedCamp.setAvailableSpace(assignedCamp.getAvailableSpace() - 1);
                campRepository.save(assignedCamp);
            }
        } else {
            request.setStatus("Pending");
        }

        Request saved = requestRepository.save(request);
        return ApiResponse.ok("Assistance request submitted successfully with ID: " + requestId, mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<RequestDto.RequestResponse> updateStatus(String requestId, RequestDto.StatusUpdateRequest req) {
        Optional<Request> reqOpt = requestRepository.findById(requestId);
        if (reqOpt.isEmpty()) {
            return ApiResponse.error("Request not found");
        }

        Request request = reqOpt.get();
        request.setStatus(req.getStatus());

        if (req.getNotes() != null && !req.getNotes().isBlank()) {
            String currentDetails = request.getDetails() != null ? request.getDetails() : "";
            request.setDetails(currentDetails + " [Status Note: " + req.getNotes().trim() + "]");
        }

        Request saved = requestRepository.save(request);
        return ApiResponse.ok("Request status updated to " + req.getStatus(), mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<RequestDto.RequestResponse> assignCamp(String requestId, RequestDto.AssignCampRequest req) {
        Optional<Request> reqOpt = requestRepository.findById(requestId);
        if (reqOpt.isEmpty()) {
            return ApiResponse.error("Request not found");
        }

        Optional<Camp> campOpt = campRepository.findById(req.getCampId());
        if (campOpt.isEmpty()) {
            return ApiResponse.error("Target camp not found");
        }

        Request request = reqOpt.get();
        Camp camp = campOpt.get();

        request.setCamp(camp);
        request.setStatus(req.getStatus() != null && !req.getStatus().isBlank() ? req.getStatus() : "Assigned");

        Request saved = requestRepository.save(request);
        return ApiResponse.ok("Request assigned to " + camp.getCampName(), mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<RequestDto.RequestResponse> confirmReceipt(String requestId, Integer userId, RequestDto.ConfirmReceiptRequest req) {
        Optional<Request> reqOpt = requestRepository.findById(requestId);
        if (reqOpt.isEmpty()) {
            return ApiResponse.error("Request not found");
        }

        Request request = reqOpt.get();
        // Verify victim ownership
        if (userId != null) {
            Optional<Victim> victimOpt = victimRepository.findByUser_UserId(userId);
            if (victimOpt.isEmpty() || !victimOpt.get().getVictimId().equals(request.getVictim().getVictimId())) {
                return ApiResponse.error("Unauthorized: You can only confirm receipt for your own requests.");
            }
        }

        request.setStatus("Resolved");
        String note = (req != null && req.getConfirmationNotes() != null) ? req.getConfirmationNotes().trim() : "Relief received confirmed by victim.";
        request.setDetails((request.getDetails() != null ? request.getDetails() : "") + " [Receipt Confirmed: " + note + "]");

        Request saved = requestRepository.save(request);
        return ApiResponse.ok("Relief receipt confirmed successfully. Thank you for notifying us.", mapToResponse(saved));
    }

    public RequestDto.RequestResponse mapToResponse(Request req) {
        RequestDto.RequestResponse dto = new RequestDto.RequestResponse();
        dto.setRequestId(req.getRequestId());
        dto.setRequestType(req.getRequestType());
        dto.setRequestDate(req.getRequestDate());
        dto.setStatus(req.getStatus());
        dto.setDetails(req.getDetails());

        if (req.getVictim() != null) {
            dto.setVictimId(req.getVictim().getVictimId());
            dto.setVictimAge(req.getVictim().getAge());
            dto.setVictimGender(req.getVictim().getGender());
            dto.setVictimLocation(req.getVictim().getLocation());
            dto.setVictimHouse(req.getVictim().getHouse());

            if (req.getVictim().getUser() != null) {
                dto.setVictimName(req.getVictim().getUser().getName());
                dto.setVictimPhone(req.getVictim().getUser().getPhone());
            }
        }

        if (req.getCamp() != null) {
            dto.setCampId(req.getCamp().getCampId());
            dto.setCampName(req.getCamp().getCampName());
            dto.setCampLocation(req.getCamp().getLocation());
        }

        return dto;
    }
}
