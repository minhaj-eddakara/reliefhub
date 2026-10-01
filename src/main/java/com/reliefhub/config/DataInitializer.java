package com.reliefhub.config;

import com.reliefhub.model.*;
import com.reliefhub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final VictimRepository victimRepository;
    private final CampRepository campRepository;
    private final RequestRepository requestRepository;
    private final ReportRepository reportRepository;

    public DataInitializer(UserRepository userRepository,
                           VictimRepository victimRepository,
                           CampRepository campRepository,
                           RequestRepository requestRepository,
                           ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.victimRepository = victimRepository;
        this.campRepository = campRepository;
        this.requestRepository = requestRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already initialized. Skipping seed data.");
            return;
        }

        log.info("Seeding ReliefHub initial data...");

        // 1. Users
        User admin = new User(null, "State Disaster Admin", "admin@reliefhub.org", "9876543210", "admin123", "ADMIN");
        User mgr1 = new User(null, "Rahul Sharma", "rahul.manager@reliefhub.org", "9845012345", "manager123", "CAMP_MANAGER");
        User mgr2 = new User(null, "Priya Nair", "priya.manager@reliefhub.org", "9845067890", "manager123", "CAMP_MANAGER");
        User vicUser1 = new User(null, "Anand Kumar", "anand.k@example.com", "9712345678", "victim123", "VICTIM");
        User vicUser2 = new User(null, "Fathima Beevi", "fathima.s@example.com", "9723456789", "victim123", "VICTIM");

        admin = userRepository.save(admin);
        mgr1 = userRepository.save(mgr1);
        mgr2 = userRepository.save(mgr2);
        vicUser1 = userRepository.save(vicUser1);
        vicUser2 = userRepository.save(vicUser2);

        // 2. Victims
        Victim vic1 = new Victim("VIC-101", vicUser1, 34, "Male", "Edivanna, Nilambur", "House #14, River View Ward");
        Victim vic2 = new Victim("VIC-102", vicUser2, 42, "Female", "Munderi, Nilambur", "Bait-ul-Salam, Forest Gate");
        vic1 = victimRepository.save(vic1);
        vic2 = victimRepository.save(vic2);

        // 3. Camps
        Camp camp1 = new Camp(
                "CAMP-01",
                "Nilambur Central Relief Camp",
                "Nilambur Higher Secondary School Ground, Malappuram",
                250,
                85,
                "Food Kits: 320 packs, Bottled Water: 850L, Medical First-Aid Kits: 45, Thermal Blankets: 180, Baby Food: 60 jars",
                mgr1
        );

        Camp camp2 = new Camp(
                "CAMP-02",
                "Vazhikkadavu Community Relief Shelter",
                "Vazhikkadavu Community Hall, Nilambur Ghat Road",
                150,
                40,
                "Food Kits: 210 packs, Bottled Water: 500L, Medical First-Aid Kits: 30, Thermal Blankets: 120, Sanitation Kits: 75",
                mgr2
        );

        camp1 = campRepository.save(camp1);
        camp2 = campRepository.save(camp2);

        // 4. Requests
        Request req1 = new Request(
                "REQ-1001",
                vic1,
                camp1,
                "Rescue",
                LocalDate.now().minusDays(1),
                "Assigned",
                "Flash flood water entering residential compound. Immediate evacuation needed for 4 family members including an elderly grandparent."
        );

        Request req2 = new Request(
                "REQ-1002",
                vic2,
                camp1,
                "Medical Assistance",
                LocalDate.now(),
                "In-Progress",
                "Insulin supplies ruined by water leakage, requiring emergency refrigerated medication and basic checkup."
        );

        Request req3 = new Request(
                "REQ-1003",
                vic1,
                null,
                "Shelter",
                LocalDate.now(),
                "Pending",
                "Emergency shelter and dry food ration needed for temporary relocation due to roof collapse threat."
        );

        Request req4 = new Request(
                "REQ-1004",
                vic2,
                camp2,
                "Food",
                LocalDate.now().minusDays(2),
                "Resolved",
                "Drinking water cans and packaged dry food kits delivered to safe elevated shelter. [Receipt Confirmed: Received all dry ration packets in good order.]"
        );

        requestRepository.save(req1);
        requestRepository.save(req2);
        requestRepository.save(req3);
        requestRepository.save(req4);

        // 5. Reports
        Report rep1 = new Report(
                "REP-501",
                camp1,
                "Incident Report",
                LocalDate.now().minusDays(1),
                "Power supply generator temporarily surged due to heavy rainfall; maintenance team deployed and backup solar operational."
        );

        Report rep2 = new Report(
                "REP-502",
                camp2,
                "Camp Occupancy",
                LocalDate.now(),
                "Camp operating at 73% occupancy. 110 of 150 beds occupied. High demand for pediatric medical kits and mosquito nets."
        );

        Report rep3 = new Report(
                "REP-503",
                null,
                "Request Summary",
                LocalDate.now(),
                "District Consolidated: Total 4 assistance requests received, 2 assigned, 1 in-progress, 1 resolved successfully."
        );

        reportRepository.save(rep1);
        reportRepository.save(rep2);
        reportRepository.save(rep3);

        log.info("ReliefHub database seeding complete with default Admin, 2 Managers, 2 Camps, 2 Victims, 4 Requests, and 3 Reports.");
    }
}
