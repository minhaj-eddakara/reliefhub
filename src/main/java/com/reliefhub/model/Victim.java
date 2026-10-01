package com.reliefhub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "victims")
public class Victim {

    @Id
    @Column(name = "victim_id", length = 50)
    private String victimId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Age is required")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotBlank(message = "Gender is required")
    @Size(max = 20)
    @Column(name = "gender", length = 20, nullable = false)
    private String gender;

    @NotBlank(message = "Location is required")
    @Size(max = 100)
    @Column(name = "location", length = 100, nullable = false)
    private String location;

    @NotBlank(message = "House/Address is required")
    @Size(max = 100)
    @Column(name = "house", length = 100, nullable = false)
    private String house;

    public Victim() {
    }

    public Victim(String victimId, User user, Integer age, String gender, String location, String house) {
        this.victimId = victimId;
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.house = house;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }
}
