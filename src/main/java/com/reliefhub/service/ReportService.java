package com.reliefhub.service;

import com.reliefhub.dto.ApiResponse;
import com.reliefhub.dto.ReportDto;
import com.reliefhub.model.Camp;
import com.reliefhub.model.Report;
import com.reliefhub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CampRepository campRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final VictimRepository victimRepository;

    private static final AtomicInteger REP_COUNTER = new AtomicInteger(504);

    public ReportService(ReportRepository reportRepository,
                         CampRepository campRepository,
                         RequestRepository requestRepository,
                         UserRepository userRepository,
                         VictimRepository victimRepository) {
        this.reportRepository = reportRepository;
        this.campRepository = campRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.victimRepository = victimRepository;
    }

    public List<ReportDto.ReportResponse> getAllReports() {
        return reportRepository.findAllByOrderByReportDateDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ReportDto.ReportResponse> getReportsByCampId(String campId) {
        return reportRepository.findByCamp_CampIdOrderByReportDateDesc(campId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApiResponse<ReportDto.ReportResponse> createReport(ReportDto.CreateReportRequest req) {
        String reportId = "REP-" + REP_COUNTER.getAndIncrement();
        Report report = new Report();
        report.setReportId(reportId);
        report.setReportType(req.getReportType());
        report.setReportDate(LocalDate.now());
        report.setDetails(req.getDetails().trim());

        if (req.getCampId() != null && !req.getCampId().isBlank()) {
            Optional<Camp> campOpt = campRepository.findById(req.getCampId());
            campOpt.ifPresent(report::setCamp);
        }

        Report saved = reportRepository.save(report);
        return ApiResponse.ok("Report logged successfully with ID: " + reportId, mapToResponse(saved));
    }

    public ReportDto.DashboardSummary getDashboardSummary() {
        ReportDto.DashboardSummary summary = new ReportDto.DashboardSummary();

        long totalReq = requestRepository.count();
        long pendingReq = requestRepository.countByStatus("Pending");
        long assignedReq = requestRepository.countByStatus("Assigned");
        long inProgressReq = requestRepository.countByStatus("In-Progress");
        long resolvedReq = requestRepository.countByStatus("Resolved");

        summary.setTotalRequests(totalReq);
        summary.setPendingRequests(pendingReq);
        summary.setAssignedRequests(assignedReq);
        summary.setInProgressRequests(inProgressReq);
        summary.setResolvedRequests(resolvedReq);

        List<Camp> camps = campRepository.findAll();
        summary.setTotalCamps(camps.size());

        int totalCap = camps.stream().mapToInt(c -> c.getCapacity() != null ? c.getCapacity() : 0).sum();
        int totalAvail = camps.stream().mapToInt(c -> c.getAvailableSpace() != null ? c.getAvailableSpace() : 0).sum();
        summary.setTotalCapacity(totalCap);
        summary.setTotalAvailableSpace(totalAvail);
        summary.setTotalOccupied(Math.max(0, totalCap - totalAvail));

        summary.setTotalVictims(victimRepository.count());
        summary.setTotalManagers(userRepository.findByRole("CAMP_MANAGER").size());

        return summary;
    }

    public ReportDto.ReportResponse mapToResponse(Report rep) {
        ReportDto.ReportResponse dto = new ReportDto.ReportResponse();
        dto.setReportId(rep.getReportId());
        dto.setReportType(rep.getReportType());
        dto.setReportDate(rep.getReportDate());
        dto.setDetails(rep.getDetails());

        if (rep.getCamp() != null) {
            dto.setCampId(rep.getCamp().getCampId());
            dto.setCampName(rep.getCamp().getCampName());
        } else {
            dto.setCampName("District Consolidated / Global");
        }

        return dto;
    }
}
