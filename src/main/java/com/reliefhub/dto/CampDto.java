package com.reliefhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CampDto {

    public static class CampResponse {
        private String campId;
        private String campName;
        private String location;
        private Integer capacity;
        private Integer availableSpace;
        private String supplies;
        private Integer managerId;
        private String managerName;
        private String managerPhone;
        private double occupancyPercent;

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getCampName() { return campName; }
        public void setCampName(String campName) { this.campName = campName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public Integer getAvailableSpace() { return availableSpace; }
        public void setAvailableSpace(Integer availableSpace) { this.availableSpace = availableSpace; }

        public String getSupplies() { return supplies; }
        public void setSupplies(String supplies) { this.supplies = supplies; }

        public Integer getManagerId() { return managerId; }
        public void setManagerId(Integer managerId) { this.managerId = managerId; }

        public String getManagerName() { return managerName; }
        public void setManagerName(String managerName) { this.managerName = managerName; }

        public String getManagerPhone() { return managerPhone; }
        public void setManagerPhone(String managerPhone) { this.managerPhone = managerPhone; }

        public double getOccupancyPercent() { return occupancyPercent; }
        public void setOccupancyPercent(double occupancyPercent) { this.occupancyPercent = occupancyPercent; }
    }

    public static class CampCreateRequest {
        @NotBlank(message = "Camp ID is required")
        private String campId;

        @NotBlank(message = "Camp Name is required")
        private String campName;

        @NotBlank(message = "Location is required")
        private String location;

        @NotNull(message = "Capacity is required")
        private Integer capacity;

        @NotNull(message = "Available Space is required")
        private Integer availableSpace;

        @NotBlank(message = "Supplies are required")
        private String supplies;

        private Integer managerId;

        public String getCampId() { return campId; }
        public void setCampId(String campId) { this.campId = campId; }

        public String getCampName() { return campName; }
        public void setCampName(String campName) { this.campName = campName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public Integer getAvailableSpace() { return availableSpace; }
        public void setAvailableSpace(Integer availableSpace) { this.availableSpace = availableSpace; }

        public String getSupplies() { return supplies; }
        public void setSupplies(String supplies) { this.supplies = supplies; }

        public Integer getManagerId() { return managerId; }
        public void setManagerId(Integer managerId) { this.managerId = managerId; }
    }

    public static class CampUpdateRequest {
        @NotBlank(message = "Camp Name is required")
        private String campName;

        @NotBlank(message = "Location is required")
        private String location;

        @NotNull(message = "Capacity is required")
        private Integer capacity;

        @NotNull(message = "Available Space is required")
        private Integer availableSpace;

        @NotBlank(message = "Supplies are required")
        private String supplies;

        private Integer managerId;

        public String getCampName() { return campName; }
        public void setCampName(String campName) { this.campName = campName; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public Integer getAvailableSpace() { return availableSpace; }
        public void setAvailableSpace(Integer availableSpace) { this.availableSpace = availableSpace; }

        public String getSupplies() { return supplies; }
        public void setSupplies(String supplies) { this.supplies = supplies; }

        public Integer getManagerId() { return managerId; }
        public void setManagerId(Integer managerId) { this.managerId = managerId; }
    }

    public static class CampSuppliesUpdateRequest {
        @NotBlank(message = "Supplies cannot be blank")
        private String supplies;

        public String getSupplies() { return supplies; }
        public void setSupplies(String supplies) { this.supplies = supplies; }
    }

    public static class CampCapacityUpdateRequest {
        @NotNull(message = "Capacity is required")
        private Integer capacity;

        @NotNull(message = "Available Space is required")
        private Integer availableSpace;

        public Integer getCapacity() { return capacity; }
        public void setCapacity(Integer capacity) { this.capacity = capacity; }

        public Integer getAvailableSpace() { return availableSpace; }
        public void setAvailableSpace(Integer availableSpace) { this.availableSpace = availableSpace; }
    }
}
