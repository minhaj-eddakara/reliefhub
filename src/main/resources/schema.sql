-- =============================================================================
-- ReliefHub: A Web-Based Disaster Management System
-- Database Schema Definition & Initial Seed Data
-- Compatible with MySQL 8.x and H2 (MySQL Mode)
-- Amal College of Advanced Studies, Nilambur - FYUGP Computer Science
-- =============================================================================

DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS camps;
DROP TABLE IF EXISTS victims;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE victims (
    victim_id VARCHAR(50) PRIMARY KEY,
    user_id INT NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    location VARCHAR(100) NOT NULL,
    house VARCHAR(100) NOT NULL,
    CONSTRAINT fk_victim_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

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

CREATE TABLE requests (
    request_id VARCHAR(50) PRIMARY KEY,
    victim_id VARCHAR(50) NOT NULL,
    camp_id VARCHAR(50) NULL,
    request_type VARCHAR(50) NOT NULL,
    request_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    details TEXT,
    CONSTRAINT fk_request_victim FOREIGN KEY (victim_id) REFERENCES victims(victim_id) ON DELETE CASCADE,
    CONSTRAINT fk_request_camp FOREIGN KEY (camp_id) REFERENCES camps(camp_id) ON DELETE SET NULL
);

CREATE TABLE reports (
    report_id VARCHAR(50) PRIMARY KEY,
    camp_id VARCHAR(50) NULL,
    report_type VARCHAR(50) NOT NULL,
    report_date DATE NOT NULL,
    details TEXT NOT NULL,
    CONSTRAINT fk_report_camp FOREIGN KEY (camp_id) REFERENCES camps(camp_id) ON DELETE SET NULL
);
