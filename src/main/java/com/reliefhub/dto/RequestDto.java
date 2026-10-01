package com.reliefhub.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class RequestDto {

    public static class CreateRequest {
        @NotBlank(message = "Request type is required")
        private String requestType;

        @NotBlank(message = "Details are required")
        private String details;

        private String preferredCampId;

        public String getRequestType() { return requestType; }
        public void setRequestType(String requestType) { this.requestType = requestType; }

        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }

        public String getPreferredCampId() { return preferredCampId; }
        public void setPreferredCampId(String preferredCampId) { this.preferredCampId = preferredCampId; }
    }

    public static class StatusUpdateRequest {
        @NotBlank(message = "Status is required")
        private String status;

        private String notes;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class AssignCampRequest {
        @NotBlank(message = "Camp ID is required")
        private String campId;

        private String status;

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class ConfirmReceiptRequest {
        private String confirmationNotes;

        public String getConfirmationNotes() { return confirmationNotes; }
        public void setConfirmationNotes(String confirmationNotes) { this.confirmationNotes = confirmationNotes; }
    }

    public static class RequestResponse {
        private String requestId;
        private String victimId;
        private String victimName;
        private String victimPhone;
        private String victimLocation;
        private String victimHouse;
        private Integer victimAge;
        private String victimGender;
        private String campId;
        private String campName;
        private String campLocation;
        private String requestType;
        private LocalDate requestDate;
        private String status;
        private String details;

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }

        public String getVictimId() { return victimId; }
        public void setVictimId(String victimId) { this.victimId = victimId; }

        public String getVictimName() { return victimName; }
        public void setVictimName(String victimName) { this.victimName = victimName; }

        public String getVictimPhone() { return victimPhone; }
        public void setVictimPhone(String victimPhone) { this.victimPhone = victimPhone; }

        public String getVictimLocation() { return victimLocation; }
        public void setVictimLocation(String victimLocation) { this.victimLocation = victimLocation; }

        public String getVictimHouse() { return victimHouse; }
        public void setVictimHouse(String house) { this.victimHouse = house; }

        public Integer getVictimAge() { return victimAge; }
        public void setVictimAge(Integer victimAge) { this.victimAge = victimAge; }

        public String getVictimGender() { return victimGender; }
        public void setVictimGender(String victimGender) { this.victimGender = victimGender; }

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getCampName() { return campName; }
        public void setCampName(String campName) { this.campName = campName; }

        public String getCampLocation() { return campLocation; }
        public void setCampLocation(String campLocation) { this.campLocation = campLocation; }

        public String getRequestType() { return requestType; }
        public void setRequestType(String requestType) { this.requestType = requestType; }

        public LocalDate getRequestDate() { return requestDate; }
        public void setRequestDate(LocalDate requestDate) { this.requestDate = requestDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }
    }
}
