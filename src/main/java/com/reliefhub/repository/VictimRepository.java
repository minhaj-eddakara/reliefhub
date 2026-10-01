package com.reliefhub.repository;

import com.reliefhub.model.User;
import com.reliefhub.model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VictimRepository extends JpaRepository<Victim, String> {
    Optional<Victim> findByUser(User user);
    Optional<Victim> findByUser_UserId(Integer userId);
}
