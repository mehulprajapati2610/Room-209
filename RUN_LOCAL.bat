@echo off
REM Room 209 Local Development Server

echo.
echo ================================
echo Room 209 Backend - Local Mode
echo ================================
echo.
echo Your IP: 192.168.0.41
echo Backend: http://192.168.0.41:8080
echo.
echo Make sure phones are on SAME WiFi network!
echo.

REM Navigate to backend
cd /d "%~dp0\backend"

REM Set Supabase connection (using JDBC format with SSL for Supabase)
REM REPLACE WITH YOUR ACTUAL SUPABASE CREDENTIALS!
set DATABASE_URL=jdbc:postgresql://db.aokmhgciexlsdlaytlxf.supabase.co:5432/postgres?sslmode=require
set DATABASE_USER=postgres
set DATABASE_PASSWORD=Room-209Mehul
set SPRING_PROFILES_ACTIVE=default
set FIREBASE_ENABLED=false

echo Starting backend server...
echo Press Ctrl+C to stop

call gradlew bootRun

pause
