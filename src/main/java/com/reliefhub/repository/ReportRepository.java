package com.reliefhub.repository;

import com.reliefhub.model.Camp;
import com.reliefhub.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {
    List<Report> findByCampOrderByReportDateDesc(Camp camp);
    List<Report> findByCamp_CampIdOrderByReportDateDesc(String campId);
    List<Report> findByReportTypeOrderByReportDateDesc(String reportType);
    List<Report> findAllByOrderByReportDateDesc();
}
