@echo off
echo =====================================================================
echo  Starting ReliefHub: Web-Based Disaster Management System
echo =====================================================================
echo.
echo Application will run at: http://localhost:8080/
echo Default Admin: admin@reliefhub.org / admin123
echo Default Manager: rahul.manager@reliefhub.org / manager123
echo Default Victim: anand.k@example.com / victim123
echo.
start "" http://localhost:8080/
java -jar target\reliefhub-1.0.0.jar
pause
