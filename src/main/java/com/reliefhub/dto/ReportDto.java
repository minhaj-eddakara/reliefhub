package com.reliefhub.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class ReportDto {

    public static class CreateReportRequest {
        private String campId;

        @NotBlank(message = "Report type is required")
        private String reportType;

        @NotBlank(message = "Details are required")
        private String details;

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getReportType() { return reportType; }
        public void setReportType(String reportType) { this.reportType = reportType; }

        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }

    public static class ReportResponse {
        private String reportId;
        private String campId;
        private String campName;
        private String reportType;
        private LocalDate reportDate;
        private String details;

        public String getReportId() { return reportId; }
        public void setReportId(String reportId) { this.reportId = reportId; }

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getCampName() { return campName; }
        public void setCampName(String campName) { this.campName = campName; }

        public String getReportType() { return reportType; }
        public void setReportType(String reportType) { this.reportType = reportType; }

        public LocalDate getReportDate() { return reportDate; }
        public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }

        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }

    public static class DashboardSummary {
        private long totalRequests;
        private long pendingRequests;
        private long assignedRequests;
        private long inProgressRequests;
        private long resolvedRequests;
        private long totalCamps;
        private int totalCapacity;
        private int totalAvailableSpace;
        private int totalOccupied;
        private long totalVictims;
        private long totalManagers;

        public long getTotalRequests() { return totalRequests; }
        public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

        public long getPendingRequests() { return pendingRequests; }
        public void setPendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; }

        public long getAssignedRequests() { return assignedRequests; }
        public void setAssignedRequests(long assignedRequests) { this.assignedRequests = assignedRequests; }

        public long getInProgressRequests() { return inProgressRequests; }
        public void setInProgressRequests(long inProgressRequests) { this.inProgressRequests = inProgressRequests; }

        public long getResolvedRequests() { return resolvedRequests; }
        public void setResolvedRequests(long resolvedRequests) { this.resolvedRequests = resolvedRequests; }

        public long getTotalCamps() { return totalCamps; }
        public void setTotalCamps(long totalCamps) { this.totalCamps = totalCamps; }

        public int getTotalCapacity() { return totalCapacity; }
        public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }

        public int getTotalAvailableSpace() { return totalAvailableSpace; }
        public void setTotalAvailableSpace(int totalAvailableSpace) { this.totalAvailableSpace = totalAvailableSpace; }

        public int getTotalOccupied() { return totalOccupied; }
        public void setTotalOccupied(int totalOccupied) { this.totalOccupied = totalOccupied; }

        public long getTotalVictims() { return totalVictims; }
        public void setTotalVictims(long totalVictims) { this.totalVictims = totalVictims; }

        public long getTotalManagers() { return totalManagers; }
        public void setTotalManagers(long totalManagers) { this.totalManagers = totalManagers; }
    }
}
