package com.reliefhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "camps")
public class Camp {

    @Id
    @Column(name = "camp_id", length = 50)
    private String campId;

    @NotBlank(message = "Camp name is required")
    @Size(max = 100)
    @Column(name = "camp_name", length = 100, nullable = false)
    private String campName;

    @NotBlank(message = "Location is required")
    @Size(max = 100)
    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @NotNull(message = "Capacity is required")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull(message = "Available space is required")
    @Column(name = "available_space", nullable = false)
    private Integer availableSpace;

    @NotBlank(message = "Supplies detail is required")
    @Column(name = "supplies", columnDefinition = "TEXT", nullable = false)
    private String supplies;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id")
    private User manager;

    public Camp() {
    }

    public Camp(String campId, String campName, String location, Integer capacity, Integer availableSpace, String supplies, User manager) {
        this.campId = campId;
        this.campName = campName;
        this.location = location;
        this.capacity = capacity;
        this.availableSpace = availableSpace;
        this.supplies = supplies;
        this.manager = manager;
    }

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(Integer availableSpace) {
        this.availableSpace = availableSpace;
    }

    public String getSupplies() {
        return supplies;
    }

    public void setSupplies(String supplies) {
        this.supplies = supplies;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
