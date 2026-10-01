package com.reliefhub.repository;

import com.reliefhub.model.Camp;
import com.reliefhub.model.Request;
import com.reliefhub.model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
    List<Request> findByVictimOrderByRequestDateDesc(Victim victim);
    List<Request> findByVictim_VictimIdOrderByRequestDateDesc(String victimId);
    List<Request> findByCampOrderByRequestDateDesc(Camp camp);
    List<Request> findByCamp_CampIdOrderByRequestDateDesc(String campId);
    List<Request> findByStatus(String status);
    long countByStatus(String status);
    List<Request> findAllByOrderByRequestDateDesc();
}
