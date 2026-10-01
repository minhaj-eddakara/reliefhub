package com.reliefhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @Column(name = "report_id", length = 50)
    private String reportId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camp_id", referencedColumnName = "camp_id")
    private Camp camp;

    @NotBlank(message = "Report type is required")
    @Size(max = 50)
    @Column(name = "report_type", length = 50, nullable = false)
    private String reportType; // 'Incident Report', 'Camp Occupancy', 'Request Summary', 'Resource Allocation'

    @NotNull(message = "Report date is required")
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @NotBlank(message = "Details are required")
    @Column(name = "details", columnDefinition = "TEXT", nullable = false)
    private String details;

    public Report() {
    }

    public Report(String reportId, Camp camp, String reportType, LocalDate reportDate, String details) {
        this.reportId = reportId;
        this.camp = camp;
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.details = details;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
