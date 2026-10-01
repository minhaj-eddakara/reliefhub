package com.reliefhub.repository;

import com.reliefhub.model.Camp;
import com.reliefhub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampRepository extends JpaRepository<Camp, String> {
    Optional<Camp> findByManager(User manager);
    Optional<Camp> findByManager_UserId(Integer managerId);
    List<Camp> findByAvailableSpaceGreaterThanOrderByAvailableSpaceDesc(Integer minSpace);
}
