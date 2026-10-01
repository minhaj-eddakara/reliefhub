package com.reliefhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "request_id", length = 50)
    private String requestId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "victim_id", referencedColumnName = "victim_id", nullable = false)
    private Victim victim;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camp_id", referencedColumnName = "camp_id")
    private Camp camp;

    @NotBlank(message = "Request type is required")
    @Size(max = 50)
    @Column(name = "request_type", length = 50, nullable = false)
    private String requestType; // Rescue, Shelter, Food, Medical Assistance, Aid

    @NotNull(message = "Request date is required")
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @NotBlank(message = "Status is required")
    @Size(max = 50)
    @Column(name = "status", length = 50, nullable = false)
    private String status; // 'Pending', 'Assigned', 'In-Progress', 'Resolved'

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    public Request() {
    }

    public Request(String requestId, Victim victim, Camp camp, String requestType, LocalDate requestDate, String status, String details) {
        this.requestId = requestId;
        this.victim = victim;
        this.camp = camp;
        this.requestType = requestType;
        this.requestDate = requestDate;
        this.status = status;
        this.details = details;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Victim getVictim() {
        return victim;
    }

    public void setVictim(Victim victim) {
        this.victim = victim;
    }

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
