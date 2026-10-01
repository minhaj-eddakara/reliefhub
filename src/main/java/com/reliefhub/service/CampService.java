package com.reliefhub.service;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.CampDto;
import com.reliefhub.model.Camp;
import com.reliefhub.model.User;
import com.reliefhub.repository.CampRepository;
import com.reliefhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampService {

    private final CampRepository campRepository;
    private final UserRepository userRepository;

    public CampService(CampRepository campRepository, UserRepository userRepository) {
        this.campRepository = campRepository;
        this.userRepository = userRepository;
    }

    public List<CampDto.CampResponse> getAllCamps() {
        return campRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApiResponse<CampDto.CampResponse> getCampById(String campId) {
        return campRepository.findById(campId)
                .map(c -> ApiResponse.ok("Camp found", mapToResponse(c)))
                .orElse(ApiResponse.error("Camp not found"));
    }

    public ApiResponse<CampDto.CampResponse> getCampByManager(Integer managerId) {
        return campRepository.findByManager_UserId(managerId)
                .map(c -> ApiResponse.ok("Assigned camp found", mapToResponse(c)))
                .orElse(ApiResponse.error("No relief camp currently assigned to this manager"));
    }

    @Transactional
    public ApiResponse<CampDto.CampResponse> createCamp(CampDto.CampCreateRequest req) {
        if (campRepository.existsById(req.getCampId().trim())) {
            return ApiResponse.error("Camp ID already exists");
        }

        Camp camp = new Camp();
        camp.setCampId(req.getCampId().trim());
        camp.setCampName(req.getCampName().trim());
        camp.setLocation(req.getLocation().trim());
        camp.setCapacity(req.getCapacity());
        camp.setAvailableSpace(req.getAvailableSpace());
        camp.setSupplies(req.getSupplies().trim());

        if (req.getManagerId() != null) {
            userRepository.findById(req.getManagerId()).ifPresent(camp::setManager);
        }

        Camp saved = campRepository.save(camp);
        return ApiResponse.ok("Camp created successfully", mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<CampDto.CampResponse> masterUpdateCamp(String campId, CampDto.CampUpdateRequest req) {
        Optional<Camp> campOpt = campRepository.findById(campId);
        if (campOpt.isEmpty()) {
            return ApiResponse.error("Camp not found");
        }

        Camp camp = campOpt.get();
        camp.setCampName(req.getCampName().trim());
        camp.setLocation(req.getLocation().trim());
        camp.setCapacity(req.getCapacity());
        camp.setAvailableSpace(req.getAvailableSpace());
        camp.setSupplies(req.getSupplies().trim());

        if (req.getManagerId() != null) {
            User manager = userRepository.findById(req.getManagerId()).orElse(null);
            camp.setManager(manager);
        } else {
            camp.setManager(null);
        }

        Camp saved = campRepository.save(camp);
        return ApiResponse.ok("Camp updated successfully by Admin", mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<CampDto.CampResponse> updateSupplies(String campId, String supplies) {
        Optional<Camp> campOpt = campRepository.findById(campId);
        if (campOpt.isEmpty()) {
            return ApiResponse.error("Camp not found");
        }

        Camp camp = campOpt.get();
        camp.setSupplies(supplies.trim());
        Camp saved = campRepository.save(camp);
        return ApiResponse.ok("Camp supplies updated successfully", mapToResponse(saved));
    }

    @Transactional
    public ApiResponse<CampDto.CampResponse> updateCapacity(String campId, Integer capacity, Integer availableSpace) {
        Optional<Camp> campOpt = campRepository.findById(campId);
        if (campOpt.isEmpty()) {
            return ApiResponse.error("Camp not found");
        }

        Camp camp = campOpt.get();
        if (availableSpace > capacity) {
            return ApiResponse.error("Available space cannot exceed total capacity");
        }

        camp.setCapacity(capacity);
        camp.setAvailableSpace(availableSpace);
        Camp saved = campRepository.save(camp);
        return ApiResponse.ok("Camp capacity and available beds updated successfully", mapToResponse(saved));
    }

    /**
     * Automatic Camp Allocation algorithm:
     * Finds the relief camp with the highest available space to prevent overcrowding and ensure balanced distribution.
     */
    public Optional<Camp> findBestCampForAllocation() {
        List<Camp> campsWithSpace = campRepository.findByAvailableSpaceGreaterThanOrderByAvailableSpaceDesc(0);
        if (campsWithSpace.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(campsWithSpace.get(0));
    }

    public CampDto.CampResponse mapToResponse(Camp camp) {
        CampDto.CampResponse dto = new CampDto.CampResponse();
        dto.setCampId(camp.getCampId());
        dto.setCampName(camp.getCampName());
        dto.setLocation(camp.getLocation());
        dto.setCapacity(camp.getCapacity());
        dto.setAvailableSpace(camp.getAvailableSpace());
        dto.setSupplies(camp.getSupplies());

        if (camp.getManager() != null) {
            dto.setManagerId(camp.getManager().getUserId());
            dto.setManagerName(camp.getManager().getName());
            dto.setManagerPhone(camp.getManager().getPhone());
        }

        if (camp.getCapacity() != null && camp.getCapacity() > 0) {
            int occupied = camp.getCapacity() - (camp.getAvailableSpace() != null ? camp.getAvailableSpace() : 0);
            double pct = (double) occupied / camp.getCapacity() * 100.0;
            dto.setOccupancyPercent(Math.round(pct * 10.0) / 10.0);
        }
        return dto;
    }
}
