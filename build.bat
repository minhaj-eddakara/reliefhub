@echo off
echo =====================================================================
echo  Building ReliefHub with Maven
echo =====================================================================
echo.
call "..\apache-maven-3.9.9\bin\mvn.cmd" clean package -DskipTests
echo.
echo Build completed. Executable JAR is located at target\reliefhub-1.0.0.jar
pause
