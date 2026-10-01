-- =============================================================================
-- ReliefHub: A Web-Based Disaster Management System
-- Database Schema Definition & Initial Seed Data
-- Compatible with MySQL 8.x and H2 (MySQL Mode)
-- Amal College of Advanced Studies, Nilambur - FYUGP Computer Science
-- =============================================================================

-- Drop tables in reverse order of foreign key dependencies
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS camps;
DROP TABLE IF EXISTS victims;
DROP TABLE IF EXISTS users;

-- -----------------------------------------------------------------------------
-- 1. users Table
-- -----------------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL -- 'ADMIN', 'CAMP_MANAGER', 'VICTIM'
);

-- -----------------------------------------------------------------------------
-- 2. victims Table
-- -----------------------------------------------------------------------------
CREATE TABLE victims (
    victim_id VARCHAR(50) PRIMARY KEY,
    user_id INT NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    location VARCHAR(100) NOT NULL,
    house VARCHAR(100) NOT NULL,
    CONSTRAINT fk_victim_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- 3. camps Table
-- -----------------------------------------------------------------------------
CREATE TABLE camps (
    camp_id VARCHAR(50) PRIMARY KEY,
    camp_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    available_space INT NOT NULL,
    supplies TEXT NOT NULL,
    manager_id INT NULL,
    CONSTRAINT fk_camp_manager FOREIGN KEY (manager_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- -----------------------------------------------------------------------------
-- 4. requests Table
-- -----------------------------------------------------------------------------
CREATE TABLE requests (
    request_id VARCHAR(50) PRIMARY KEY,
    victim_id VARCHAR(50) NOT NULL,
    camp_id VARCHAR(50) NULL,
    request_type VARCHAR(50) NOT NULL,
    request_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL, -- 'Pending', 'Assigned', 'In-Progress', 'Resolved'
    details TEXT,
    CONSTRAINT fk_request_victim FOREIGN KEY (victim_id) REFERENCES victims(victim_id) ON DELETE CASCADE,
    CONSTRAINT fk_request_camp FOREIGN KEY (camp_id) REFERENCES camps(camp_id) ON DELETE SET NULL
);

-- -----------------------------------------------------------------------------
-- 5. reports Table
-- -----------------------------------------------------------------------------
CREATE TABLE reports (
    report_id VARCHAR(50) PRIMARY KEY,
    camp_id VARCHAR(50) NULL,
    report_type VARCHAR(50) NOT NULL, -- 'Incident Report', 'Camp Occupancy', 'Request Summary', 'Resource Allocation'
    report_date DATE NOT NULL,
    details TEXT NOT NULL,
    CONSTRAINT fk_report_camp FOREIGN KEY (camp_id) REFERENCES camps(camp_id) ON DELETE SET NULL
);

-- =============================================================================
-- SEED DATA
-- =============================================================================

-- Default Users (1 Admin, 2 Camp Managers, 2 Victims)
INSERT INTO users (user_id, name, email, phone, password, role) VALUES
(1, 'State Disaster Admin', 'admin@reliefhub.org', '9876543210', 'admin123', 'ADMIN'),
(2, 'Rahul Sharma', 'rahul.manager@reliefhub.org', '9845012345', 'manager123', 'CAMP_MANAGER'),
(3, 'Priya Nair', 'priya.manager@reliefhub.org', '9845067890', 'manager123', 'CAMP_MANAGER'),
(4, 'Anand Kumar', 'anand.k@example.com', '9712345678', 'victim123', 'VICTIM'),
(5, 'Fathima Beevi', 'fathima.s@example.com', '9723456789', 'victim123', 'VICTIM');

-- Victims profile records
INSERT INTO victims (victim_id, user_id, age, gender, location, house) VALUES
('VIC-101', 4, 34, 'Male', 'Edivanna, Nilambur', 'House #14, River View Ward'),
('VIC-102', 5, 42, 'Female', 'Munderi, Nilambur', 'Bait-ul-Salam, Forest Gate');

-- Relief Camps (2 initial camps with manager assignments and supplies)
INSERT INTO camps (camp_id, camp_name, location, capacity, available_space, supplies, manager_id) VALUES
('CAMP-01', 'Nilambur Central Relief Camp', 'Nilambur Higher Secondary School Ground, Malappuram', 250, 85, 'Food Kits: 320 packs, Bottled Water: 850L, Medical First-Aid Kits: 45, Thermal Blankets: 180, Baby Food: 60 jars', 2),
('CAMP-02', 'Vazhikkadavu Community Relief Shelter', 'Vazhikkadavu Community Hall, Nilambur Ghat Road', 150, 40, 'Food Kits: 210 packs, Bottled Water: 500L, Medical First-Aid Kits: 30, Thermal Blankets: 120, Sanitation Kits: 75', 3);

-- Assistance Requests
INSERT INTO requests (request_id, victim_id, camp_id, request_type, request_date, status, details) VALUES
('REQ-1001', 'VIC-101', 'CAMP-01', 'Rescue', '2026-09-28', 'Assigned', 'Flash flood water entering residential compound. Immediate evacuation needed for 4 family members including an elderly grandparent.'),
('REQ-1002', 'VIC-102', 'CAMP-01', 'Medical Assistance', '2026-09-29', 'In-Progress', 'Insulin supplies ruined by water leakage, requiring emergency refrigerated medication and basic checkup.'),
('REQ-1003', 'VIC-101', NULL, 'Shelter', '2026-09-29', 'Pending', 'Emergency shelter and dry food ration needed for temporary relocation due to roof collapse threat.'),
('REQ-1004', 'VIC-102', 'CAMP-02', 'Food', '2026-09-27', 'Resolved', 'Drinking water cans and packaged dry food kits delivered to safe elevated shelter.');

-- Reports
INSERT INTO reports (report_id, camp_id, report_type, report_date, details) VALUES
('REP-501', 'CAMP-01', 'Incident Report', '2026-09-28', 'Power supply generator temporarily surged due to heavy rainfall; maintenance team deployed and backup solar operational.'),
('REP-502', 'CAMP-02', 'Camp Occupancy', '2026-09-29', 'Camp operating at 73% occupancy. 110 of 150 beds occupied. High demand for pediatric medical kits and mosquito nets.'),
('REP-503', NULL, 'Request Summary', '2026-09-29', 'District Consolidated: Total 4 assistance requests received, 2 assigned, 1 in-progress, 1 resolved successfully.');
