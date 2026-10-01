# ReliefHub: A Web-Based Disaster Management System

**ReliefHub** is a production-ready, full-stack disaster management and emergency coordination platform designed to accelerate relief operations during crises. The platform bridges **Disaster Administrators**, **Camp Managers**, and **Affected Victims** into a synchronized real-time web ecosystem.

Developed for **CSC5CJ302 Object-Oriented Programming (Java)**, Fifth Semester FYUGP, Department of Computer Science, **Amal College of Advanced Studies, Nilambur (Autonomous)**.

---

## 1. Tech Stack Architecture

- **Backend**: Java 21+ / Spring Boot 3.3.4 REST API & MVC Static Resource Server
- **ORM & Data Layer**: Spring Data JPA / Hibernate with Bean Validation (`jakarta.validation`)
- **Database**: Dual compatibility:
  - **H2 In-Memory/File Database (Default)**: Preconfigured for zero-setup instant local testing with MySQL compatibility mode and pre-seeded live data.
  - **MySQL 8.x (Production)**: Full schema scripts provided in `schema.sql` and profile `application-mysql.properties`.
- **Frontend**: Semantic HTML5, Custom Responsive Modern CSS3 (Grid & Flexbox, accessible design, emergency palettes, status badges, progress bars, modal windows), and Vanilla JavaScript (AJAX / Fetch API for asynchronous non-blocking interactions).
- **Authentication & Security**: Role-Based Access Control (**ADMIN**, **CAMP_MANAGER**, **VICTIM**) via HTTP Session & State Guards.

---

## 2. Default Seed Credentials (1-Click Demo Available)

| Role | Name | Email | Password | Assigned Context |
| :--- | :--- | :--- | :--- | :--- |
| **Administrator** | State Disaster Admin | `admin@reliefhub.org` | `admin123` | Full District Control |
| **Camp Manager** | Rahul Sharma | `rahul.manager@reliefhub.org` | `manager123` | CAMP-01 (Nilambur Central Relief Camp) |
| **Camp Manager** | Priya Nair | `priya.manager@reliefhub.org` | `manager123` | CAMP-02 (Vazhikkadavu Community Shelter) |
| **Victim** | Anand Kumar | `anand.k@example.com` | `victim123` | VIC-101 (Edivanna, Nilambur) |
| **Victim** | Fathima Beevi | `fathima.s@example.com` | `victim123` | VIC-102 (Munderi, Nilambur) |

---

## 3. Database Schema (`schema.sql`)

The system implements the five core relational tables strictly conforming to the project specifications:

```
+--------------------+        1:1       +--------------------+
|       users        |<-----------------|      victims       |
| (user_id PK)       |                  | (victim_id PK)     |
+--------------------+                  +--------------------+
          | 1:N (Manager)                         | 1:N
          v                                       v
+--------------------+        1:N       +--------------------+
|       camps        |<-----------------|      requests      |
| (camp_id PK)       |                  | (request_id PK)    |
+--------------------+                  +--------------------+
          | 1:N
          v
+--------------------+
|      reports       |
| (report_id PK)     |
+--------------------+
```

### Table Definitions:
1. **`users`**:
   - `user_id` (INT, PK, Auto Increment)
   - `name` (VARCHAR(50), NOT NULL)
   - `email` (VARCHAR(50), NOT NULL, UNIQUE)
   - `phone` (VARCHAR(20), NOT NULL)
   - `password` (VARCHAR(100), NOT NULL)
   - `role` (VARCHAR(50), NOT NULL: 'ADMIN', 'CAMP_MANAGER', 'VICTIM')

2. **`victims`**:
   - `victim_id` (VARCHAR(50), PK)
   - `user_id` (INT, FK -> users.user_id, ON DELETE CASCADE)
   - `age` (INT, NOT NULL)
   - `gender` (VARCHAR(20), NOT NULL)
   - `location` (VARCHAR(100), NOT NULL)
   - `house` (VARCHAR(100), NOT NULL)

3. **`camps`**:
   - `camp_id` (VARCHAR(50), PK)
   - `camp_name` (VARCHAR(100), NOT NULL)
   - `location` (VARCHAR(100), NOT NULL)
   - `capacity` (INT, NOT NULL)
   - `available_space` (INT, NOT NULL)
   - `supplies` (TEXT, NOT NULL)
   - `manager_id` (INT, FK -> users.user_id, ON DELETE SET NULL)

4. **`requests`**:
   - `request_id` (VARCHAR(50), PK)
   - `victim_id` (VARCHAR(50), FK -> victims.victim_id, ON DELETE CASCADE)
   - `camp_id` (VARCHAR(50), FK -> camps.camp_id, NULLABLE, ON DELETE SET NULL)
   - `request_type` (VARCHAR(50), NOT NULL: Rescue, Shelter, Food, Medical Assistance, Aid)
   - `request_date` (DATE, NOT NULL)
   - `status` (VARCHAR(50), NOT NULL: 'Pending', 'Assigned', 'In-Progress', 'Resolved')
   - `details` (TEXT)

5. **`reports`**:
   - `report_id` (VARCHAR(50), PK)
   - `camp_id` (VARCHAR(50), FK -> camps.camp_id, NULLABLE)
   - `report_type` (VARCHAR(50), NOT NULL: Incident Report, Camp Occupancy, Request Summary, Resource Allocation)
   - `report_date` (DATE, NOT NULL)
   - `details` (TEXT, NOT NULL)

---

## 4. User Roles & Feature Workflows

### 1. Administrator (`admin-dashboard.html`)
- **Executive KPI Dashboard**: Live monitoring of total requests, pending vs resolved ratios, shelter bed capacities, and real-time occupancy percentages.
- **User Management**: Add new Camp Managers, edit profiles, review victim registries, and remove accounts with automatic dependency cleanups.
- **Request Oversight & Allocation**: View all citizen emergency requests, filter by status, prioritize urgent rescues, and assign / re-assign requests to any relief camp.
- **Camp Oversight**: Master edit camp details, location, bed capacity, live vacancies, supplies text, and assign camp managers.
- **Consolidated Reports**: Generate and view official Request Summary Reports, Camp Occupancy Reports, and Incident Reports with 1-click Print/PDF export.
- **Account Settings**: Update administrator credentials and change password.

### 2. Camp Manager (`manager-dashboard.html`)
- **Assigned Camp Command**: Real-time overview of the assigned shelter (bed occupancy, vacancies, location, inventory).
- **Capacity & Space Tracking**: Quick update of total capacity and available beds as evacuees arrive or get discharged.
- **Supplies & Inventory Management**: Live tracking and updating of essential supplies (food packs, drinking water liters, medical kits, thermal blankets, sanitation packs).
- **Victim & Request Handling**: View all assigned evacuee requests, verify arrival, and update statuses (`Assigned` -> `In-Progress / Accommodated` -> `Resolved`) with manager progress notes.
- **Incident Reporting**: Prepare, log, and submit emergency incident reports (Medical Emergencies, Supply Shortages, Structural Hazards, Weather Influx) to notify District Command.
- **Camp Analytics**: Review incident logs and print camp operation summaries.

### 3. Victim (`victim-dashboard.html`)
- **Registration & Login**: Fast self-registration capturing personal and physical location details (age, gender, village/ward, house/landmark).
- **Emergency Assistance Request**: One-click submission for Rescue, Shelter, Food, Medical Care, or General Aid.
- **Intelligent Auto-Allocation**: System automatically allocates incoming shelter requests to the camp with the most available space, preventing overload.
- **Live Status Tracker**: Visual 4-step progress stepper (`Submitted` -> `Assigned to Camp` -> `In-Progress` -> `Resolved`) showing camp details and responder phone numbers.
- **Camp & Services Directory**: Search and filter public camps by available relief goods (Food, Water, Medical, Blankets) with live capacity bars.
- **Relief Receipt Confirmation**: One-click confirmation button allowing victims to confirm receipt of aid once delivered.

---

## 5. REST API Endpoints

### Authentication & Users
- `POST /api/auth/login`: Authenticate user and initialize session
- `POST /api/auth/register`: Register new victim with profile
- `GET /api/auth/me`: Get current authenticated user profile
- `POST /api/auth/change-password`: Update user password
- `POST /api/auth/logout`: Destroy session
- `GET /api/users`: Get list of users (filterable by `?role=...`)
- `POST /api/users`: Create user (Admin only)
- `PUT /api/users/{id}`: Update user profile
- `DELETE /api/users/{id}`: Delete user account

### Camps & Resources
- `GET /api/camps`: Get all registered relief camps
- `GET /api/camps/{id}`: Get specific camp details
- `GET /api/camps/my-camp`: Get assigned camp for logged-in Camp Manager
- `POST /api/camps`: Register new relief camp
- `PUT /api/camps/{id}`: Master edit camp records (Admin)
- `PUT /api/camps/{id}/capacity`: Update bed capacity & available space (Manager)
- `PUT /api/camps/{id}/supplies`: Update supplies inventory (Manager)

### Assistance Requests
- `GET /api/requests`: Get all requests (supports filters `?campId=...&status=...`)
- `GET /api/requests/{id}`: Get single request details
- `GET /api/requests/my-requests`: Get requests filed by logged-in victim
- `GET /api/requests/camp/{campId}`: Get requests assigned to specific camp
- `POST /api/requests`: Submit new assistance request (with auto-camp allocation)
- `PUT /api/requests/{id}/status`: Update request status and add progress notes
- `PUT /api/requests/{id}/assign`: Assign / re-assign request to camp
- `POST /api/requests/{id}/confirm-receipt`: Victim confirmation of relief delivery

### Reports & Public
- `GET /api/reports`: Get all filed reports
- `GET /api/reports/camp/{campId}`: Get incident reports for a specific camp
- `POST /api/reports`: File new incident or administrative report
- `GET /api/reports/summary`: Aggregated KPI counters for dashboards
- `GET /api/public/summary`: Homepage public metrics
- `GET /api/public/camps`: Public camp availability directory
- `GET /api/public/track/{requestId}`: Unauthenticated quick status lookup

---

## 6. Setup & Execution Instructions

### Option A: Instant Run with Default In-Memory H2 (Zero-Config)
The application comes pre-packaged with an executable JAR that runs instantly using Java 21+:

1. Open PowerShell or Command Prompt in the `reliefhub` directory:
   ```powershell
   cd C:\Users\Minhaj\.gemini\antigravity\scratch\reliefhub
   ```
2. Double-click `run.bat` or run:
   ```powershell
   java -jar target\reliefhub-1.0.0.jar
   ```
3. Open your browser and navigate to:
   ```
   http://localhost:8080/
   ```
4. Access the embedded H2 database console anytime at:
   ```
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:reliefhubdb
   User: sa
   Password: (blank)
   ```

### Option B: Running with MySQL 8.x
1. Start MySQL server and create the database (or let Spring Boot create it):
   ```sql
   CREATE DATABASE IF NOT EXISTS reliefhub;
   ```
2. Import the schema and seed data:
   ```bash
   mysql -u root -p reliefhub < schema.sql
   ```
3. Run the application with the MySQL Spring profile:
   ```powershell
   java -jar target\reliefhub-1.0.0.jar --spring.profiles.active=mysql
   ```

### Option C: Rebuilding from Source
To rebuild the project using Apache Maven:
```powershell
.\build.bat
# Or using maven directly:
..\apache-maven-3.9.9\bin\mvn.cmd clean package -DskipTests
```

---

## 7. Directory Structure

```
reliefhub/
├── build.bat                            # Quick build script
├── run.bat                              # One-click launch script
├── pom.xml                              # Maven configuration
├── README.md                            # Comprehensive documentation
├── schema.sql                           # Full SQL DDL & seed data script
├── src/
│   ├── main/
│   │   ├── java/com/reliefhub/
│   │   │   ├── ReliefHubApplication.java
│   │   │   ├── config/                  # DataInitializer & WebConfig
│   │   │   ├── controller/              # Auth, User, Camp, Request, Report, Public
│   │   │   ├── dto/                     # API Request & Response DTOs
│   │   │   ├── model/                   # User, Victim, Camp, Request, Report entities
│   │   │   ├── repository/              # Spring Data JPA Repositories
│   │   │   └── service/                 # Business logic & auto-allocation
│   │   └── resources/
│   │       ├── application.properties   # H2 configuration
│   │       ├── application-mysql.properties # MySQL configuration
│   │       ├── schema.sql
│   │       └── static/
│   │           ├── index.html           # Landing page & public tracker
│   │           ├── login.html           # Authentication portal
│   │           ├── register.html        # Victim registration form
│   │           ├── admin-dashboard.html # Administrator command center
│   │           ├── manager-dashboard.html# Camp manager operations portal
│   │           ├── victim-dashboard.html# Victim assistance portal
│   │           ├── css/style.css        # Responsive stylesheet
│   │           └── js/main.js           # AJAX & client logic
└── target/
    └── reliefhub-1.0.0.jar              # Ready-to-run executable JAR
```

---
*ReliefHub: A Web-Based Disaster Management System &copy; 2026*
